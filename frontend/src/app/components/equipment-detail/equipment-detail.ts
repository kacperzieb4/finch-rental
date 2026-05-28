import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiService, Equipment } from '../../services/api.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-equipment-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './equipment-detail.html',
  styleUrl: './equipment-detail.css'
})
export class EquipmentDetail implements OnInit {
  equipmentId: number | null = null;
  equipment = signal<Equipment | null>(null);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  selectedQuantity = signal<number>(1);
  availableUnits = signal<number | null>(null);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    public cartService: CartService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.equipmentId = parseInt(idParam, 10);
      this.loadEquipment();
    } else {
      this.errorMessage.set('Nieprawidłowy identyfikator sprzętu.');
      this.isLoading.set(false);
    }
  }

  get startDate() {
    return this.cartService.startDate();
  }

  get endDate() {
    return this.cartService.endDate();
  }

  get daysCount() {
    return this.cartService.daysCount();
  }

  get cartCount(): number {
    if (!this.equipmentId) return 0;
    const item = this.cartService.cartItems().find(i => i.equipment.id === this.equipmentId);
    return item ? item.quantity : 0;
  }

  loadEquipment(): void {
    if (!this.equipmentId) return;
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.apiService.getEquipment(this.equipmentId).subscribe({
      next: (data) => {
        this.equipment.set(data);
        this.isLoading.set(false);
        this.checkAvailability();
      },
      error: (err) => {
        console.error(err);
        this.errorMessage.set('Nie udało się pobrać szczegółów sprzętu.');
        this.isLoading.set(false);
      }
    });
  }

  onDatesChange(start: string, end: string): void {
    this.cartService.setDates(start, end);
    this.checkAvailability();
  }

  checkAvailability(): void {
    if (!this.equipmentId) return;
    const start = this.startDate;
    const end = this.endDate;

    if (start && end && this.daysCount > 0) {
      this.apiService.getAvailableEquipment(start, end).subscribe({
        next: (list) => {
          const matched = list.find(eq => eq.id === this.equipmentId);
          if (matched) {
            const availVal = matched.availableUnits !== undefined ? matched.availableUnits : (matched.quantity !== undefined ? matched.quantity : 1);
            this.availableUnits.set(availVal);
          } else {
            this.availableUnits.set(0);
          }
          this.validateSelectedQuantity();
        },
        error: (err) => {
          console.error(err);
          this.availableUnits.set(null);
        }
      });
    } else {
      this.availableUnits.set(null);
    }
  }

  validateSelectedQuantity(): void {
    const avail = this.getMaxToSelect();
    if (this.selectedQuantity() > avail) {
      this.selectedQuantity.set(Math.max(1, avail));
    }
  }

  getMaxToSelect(): number {
    const eq = this.equipment();
    if (!eq) return 1;
    const totalAvail = this.availableUnits() !== null ? this.availableUnits()! : (eq.quantity || 1);
    const inCart = this.cartCount;
    return Math.max(0, totalAvail - inCart);
  }

  incrementQuantity(): void {
    if (this.selectedQuantity() < this.getMaxToSelect()) {
      this.selectedQuantity.update(q => q + 1);
    }
  }

  decrementQuantity(): void {
    if (this.selectedQuantity() > 1) {
      this.selectedQuantity.update(q => q - 1);
    }
  }

  addToCart(): void {
    const eq = this.equipment();
    if (!eq || this.selectedQuantity() <= 0) return;

    this.cartService.addToCart(eq, this.selectedQuantity());
    this.successMessage.set(`Dodano ${this.selectedQuantity()} szt. do koszyka!`);
    this.selectedQuantity.set(1);
    this.checkAvailability();

    setTimeout(() => {
      this.successMessage.set(null);
    }, 3000);
  }
}
