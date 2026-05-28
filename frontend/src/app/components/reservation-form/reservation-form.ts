import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService, Equipment, Reservation } from '../../services/api.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-reservation-form',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './reservation-form.html',
  styleUrl: './reservation-form.css'
})
export class ReservationForm implements OnInit {
  equipment = signal<Equipment | null>(null);
  isLoading = signal<boolean>(true);
  isSubmitting = signal<boolean>(false);
  successMessage = signal<string | null>(null);
  errorMessage = signal<string | null>(null);
  validationErrors = signal<string[]>([]);

  formData = {
    customerName: '',
    customerEmail: '',
    customerPhone: '',
    startDate: '',
    endDate: ''
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    public cartService: CartService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const startParam = this.route.snapshot.queryParamMap.get('start');
    const endParam = this.route.snapshot.queryParamMap.get('end');

    if (startParam) {
      this.formData.startDate = startParam;
    }
    if (endParam) {
      this.formData.endDate = endParam;
    }

    if (idParam) {
      const id = parseInt(idParam, 10);
      this.loadEquipment(id);
    } else {
      this.errorMessage.set('Nieprawidłowy identyfikator sprzętu.');
      this.isLoading.set(false);
    }
  }

  loadEquipment(id: number): void {
    this.apiService.getEquipment(id).subscribe({
      next: (data) => {
        this.equipment.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage.set('Nie udało się pobrać szczegółów sprzętu.');
        this.isLoading.set(false);
      }
    });
  }

  onSubmit(): void {
    const eq = this.equipment();
    if (!eq || !eq.id) return;

    this.isSubmitting.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);
    this.validationErrors.set([]);

    const reservation: Reservation = {
      equipmentIds: [eq.id],
      startDate: this.formData.startDate,
      endDate: this.formData.endDate,
      customerName: this.formData.customerName,
      customerEmail: this.formData.customerEmail,
      customerPhone: this.formData.customerPhone || undefined
    };

    this.apiService.createReservation(reservation).subscribe({
      next: (res) => {
        this.successMessage.set(`Sprzęt został pomyślnie zarezerwowany! ID rezerwacji: ${res.id}. Oczekuj na akceptację przez administratora.`);
        this.isSubmitting.set(false);
        this.formData = {
          customerName: '',
          customerEmail: '',
          customerPhone: '',
          startDate: '',
          endDate: ''
        };
      },
      error: (err) => {
        console.error(err);
        this.isSubmitting.set(false);
        if (err.error && err.error.message) {
          this.errorMessage.set(err.error.message);
          if (err.error.details && err.error.details.length > 0) {
            this.validationErrors.set(err.error.details);
          }
        } else {
          this.errorMessage.set('Wystąpił błąd podczas tworzenia rezerwacji. Upewnij się, że ten model jest dostępny w wybranym przedziale czasowym.');
        }
      }
    });
  }
}
