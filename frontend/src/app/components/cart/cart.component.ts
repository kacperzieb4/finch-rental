import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { ApiService, Reservation } from '../../services/api.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  isSubmitting = signal<boolean>(false);
  successMessage = signal<string | null>(null);
  errorMessage = signal<string | null>(null);
  validationErrors = signal<string[]>([]);

  customerName = '';
  customerEmail = '';
  customerPhone = '';

  constructor(
    public cartService: CartService,
    private apiService: ApiService,
    private router: Router
  ) {}

  get cartItems() {
    return this.cartService.cartItems;
  }

  get daysCount() {
    return this.cartService.daysCount;
  }

  get totalPrice() {
    return this.cartService.totalPrice;
  }

  get startDate() {
    return this.cartService.startDate;
  }

  get endDate() {
    return this.cartService.endDate;
  }

  onDatesChange(start: string, end: string) {
    this.cartService.setDates(start, end);
  }

  removeFromCart(id: number) {
    this.cartService.removeFromCart(id);
  }

  clearCart() {
    this.cartService.clearCart();
  }

  onSubmit() {
    const items = this.cartItems();
    const start = this.startDate();
    const end = this.endDate();

    if (items.length === 0) {
      this.errorMessage.set('Twój koszyk jest pusty.');
      return;
    }

    if (!start || !end || this.daysCount() <= 0) {
      this.errorMessage.set('Proszę wybrać poprawne daty wypożyczenia.');
      return;
    }

    if (!this.customerName.trim() || !this.customerEmail.trim()) {
      this.errorMessage.set('Proszę wypełnić wymagane pola (Imię i Nazwisko oraz E-mail).');
      return;
    }

    this.isSubmitting.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);
    this.validationErrors.set([]);

    const equipmentIds = items.map(item => item.id).filter((id): id is number => id !== undefined);

    const reservation: Reservation = {
      startDate: start,
      endDate: end,
      customerName: this.customerName,
      customerEmail: this.customerEmail,
      customerPhone: this.customerPhone || undefined,
      equipmentIds: equipmentIds
    };

    this.apiService.createReservation(reservation).subscribe({
      next: (res) => {
        this.successMessage.set(`Rezerwacja została pomyślnie utworzona! ID rezerwacji: ${res.id}. Oczekuj na zatwierdzenie przez administratora.`);
        this.cartService.clearCart();
        this.customerName = '';
        this.customerEmail = '';
        this.customerPhone = '';
        this.isSubmitting.set(false);
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
          this.errorMessage.set('Wystąpił błąd podczas składania rezerwacji. Upewnij się, że wybrane modele są wolne w tym okresie.');
        }
      }
    });
  }
}
