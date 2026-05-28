import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ApiService, Equipment, Reservation, ReservationItem } from '../../services/api.service';

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-panel.html',
  styleUrl: './admin-panel.css'
})
export class AdminPanel implements OnInit {
  loginData = { username: '', password: '' };
  loginError = signal<string | null>(null);

  reservations = signal<Reservation[]>([]);
  equipmentList = signal<Equipment[]>([]);
  
  reportDateSignal = signal<string>('');

  dailyReservations = computed(() => {
    const selectedDate = this.reportDateSignal();
    if (!selectedDate) return [];
    return this.reservations().filter(res => {
      if (res.status === 'REJECTED') return false;
      return res.startDate <= selectedDate && res.endDate >= selectedDate;
    });
  });

  dailyEquipmentSummary = computed(() => {
    const activeRes = this.dailyReservations();
    const summaryMap = new Map<string, number>();
    for (const res of activeRes) {
      if (res.items) {
        for (const item of res.items) {
          const name = item.equipmentName;
          summaryMap.set(name, (summaryMap.get(name) || 0) + 1);
        }
      }
    }
    return Array.from(summaryMap.entries()).map(([name, count]) => ({
      name,
      count
    }));
  });
  
  newEquipment: Equipment = {
    name: '',
    category: 'Camera',
    description: '',
    pricePerDay: 150,
    available: true,
    imageUrl: '',
    quantity: 1
  };

  editingEquipment: Equipment | null = null;
  
  isUploading = signal<boolean>(false);
  isUploadingEdit = signal<boolean>(false);
  successMessage = signal<string | null>(null);
  errorMessage = signal<string | null>(null);

  constructor(
    protected authService: AuthService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.loadAdminData();
    }
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const dd = String(today.getDate()).padStart(2, '0');
    this.reportDateSignal.set(`${yyyy}-${mm}-${dd}`);
  }

  onLogin(): void {
    this.loginError.set(null);
    const success = this.authService.login(this.loginData.username, this.loginData.password);
    if (success) {
      this.loadAdminData();
      this.loginData = { username: '', password: '' };
    } else {
      this.loginError.set('Nieprawidłowy login lub hasło.');
    }
  }

  onLogout(): void {
    this.authService.logout();
    this.reservations.set([]);
    this.equipmentList.set([]);
    this.editingEquipment = null;
  }

  loadAdminData(): void {
    this.loadReservations();
    this.loadEquipment();
  }

  loadReservations(): void {
    this.apiService.getReservations().subscribe({
      next: (data) => this.reservations.set(data),
      error: (err) => console.error('Błąd pobierania rezerwacji:', err)
    });
  }

  loadEquipment(): void {
    this.apiService.getEquipmentList().subscribe({
      next: (data) => this.equipmentList.set(data),
      error: (err) => console.error('Błąd pobierania sprzętu:', err)
    });
  }

  updateStatus(id: number, status: 'APPROVED' | 'REJECTED'): void {
    this.apiService.updateReservationStatus(id, status).subscribe({
      next: () => {
        this.successMessage.set(`Zmieniono status rezerwacji #${id} na ${status}.`);
        this.loadReservations();
        this.clearMessagesAfterDelay();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage.set('Nie udało się zmienić statusu rezerwacji.');
        this.clearMessagesAfterDelay();
      }
    });
  }

  deleteReservation(id: number): void {
    if (confirm(`Czy na pewno chcesz usunąć rezerwację #${id}?`)) {
      this.apiService.deleteReservation(id).subscribe({
        next: () => {
          this.successMessage.set(`Usunięto rezerwację #${id}.`);
          this.loadReservations();
          this.clearMessagesAfterDelay();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage.set('Nie udało się usunąć rezerwacji.');
          this.clearMessagesAfterDelay();
        }
      });
    }
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.isUploading.set(true);
      this.errorMessage.set(null);
      this.apiService.uploadImage(file, this.newEquipment.name).subscribe({
        next: (response) => {
          this.newEquipment.imageUrl = response.url;
          this.isUploading.set(false);
        },
        error: (err) => {
          console.error(err);
          this.errorMessage.set('Błąd podczas przesyłania zdjęcia. Upewnij się, że plik jest obrazem.');
          this.isUploading.set(false);
        }
      });
    }
  }

  onFileSelectedForEdit(event: any): void {
    const file: File = event.target.files[0];
    if (file && this.editingEquipment) {
      this.isUploadingEdit.set(true);
      this.errorMessage.set(null);
      this.apiService.uploadImage(file, this.editingEquipment.name).subscribe({
        next: (response) => {
          if (this.editingEquipment) {
            this.editingEquipment.imageUrl = response.url;
          }
          this.isUploadingEdit.set(false);
        },
        error: (err) => {
          console.error(err);
          this.errorMessage.set('Błąd podczas przesyłania zdjęcia w edycji.');
          this.isUploadingEdit.set(false);
        }
      });
    }
  }

  onCreateEquipment(): void {
    this.errorMessage.set(null);
    this.successMessage.set(null);

    this.apiService.createEquipment(this.newEquipment).subscribe({
      next: (created) => {
        this.successMessage.set(`Dodano nowy sprzęt: ${created.name}`);
        this.loadEquipment();
        this.newEquipment = {
          name: '',
          category: 'Camera',
          description: '',
          pricePerDay: 150,
          available: true,
          imageUrl: '',
          quantity: 1
        };
        this.clearMessagesAfterDelay();
      },
      error: (err) => {
        console.error(err);
        if (err.error && err.error.message) {
          this.errorMessage.set(err.error.message);
        } else {
          this.errorMessage.set('Błąd podczas tworzenia sprzętu.');
        }
      }
    });
  }

  deleteEquipment(id: number): void {
    if (confirm(`Czy na pewno chcesz usunąć ten sprzęt z bazy danych?`)) {
      this.apiService.deleteEquipment(id).subscribe({
        next: () => {
          this.successMessage.set(`Sprzęt został usunięty.`);
          this.loadEquipment();
          this.clearMessagesAfterDelay();
        },
        error: (err) => {
          console.error(err);
          this.errorMessage.set('Nie można usunąć sprzętu. Prawdopodobnie istnieją na niego aktywne rezerwacje.');
          this.clearMessagesAfterDelay();
        }
      });
    }
  }

  startEdit(eq: Equipment): void {
    this.editingEquipment = { ...eq };
    this.errorMessage.set(null);
    this.successMessage.set(null);
  }

  cancelEdit(): void {
    this.editingEquipment = null;
  }

  onUpdateEquipment(): void {
    if (!this.editingEquipment || !this.editingEquipment.id) return;
    this.errorMessage.set(null);
    this.successMessage.set(null);

    this.apiService.updateEquipment(this.editingEquipment.id, this.editingEquipment).subscribe({
      next: (updated) => {
        this.successMessage.set(`Zaktualizowano pomyślnie sprzęt: ${updated.name}`);
        this.loadEquipment();
        this.editingEquipment = null;
        this.clearMessagesAfterDelay();
      },
      error: (err) => {
        console.error(err);
        if (err.error && err.error.message) {
          this.errorMessage.set(err.error.message);
        } else {
          this.errorMessage.set('Błąd podczas aktualizacji sprzętu.');
        }
      }
    });
  }

  getGroupedItems(items: ReservationItem[] | undefined): { name: string; price: number; quantity: number }[] {
    if (!items) return [];
    const groups = new Map<string, { price: number; quantity: number }>();
    for (const item of items) {
      const existing = groups.get(item.equipmentName);
      if (existing) {
        existing.quantity += 1;
      } else {
        groups.set(item.equipmentName, { price: item.pricePerDay, quantity: 1 });
      }
    }
    return Array.from(groups.entries()).map(([name, val]) => ({
      name,
      price: val.price,
      quantity: val.quantity
    }));
  }

  printReport(): void {
    window.print();
  }

  private clearMessagesAfterDelay(): void {
    setTimeout(() => {
      this.successMessage.set(null);
      this.errorMessage.set(null);
    }, 4000);
  }
}
