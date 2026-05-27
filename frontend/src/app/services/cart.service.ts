import { Injectable, signal, computed } from '@angular/core';
import { Equipment } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  cartItems = signal<Equipment[]>([]);
  
  startDate = signal<string>('');
  endDate = signal<string>('');

  daysCount = computed(() => {
    const start = this.startDate();
    const end = this.endDate();
    if (!start || !end) return 0;

    const s = new Date(start);
    const e = new Date(end);
    if (isNaN(s.getTime()) || isNaN(e.getTime()) || e < s) return 0;

    const diffTime = Math.abs(e.getTime() - s.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    return diffDays;
  });

  totalPrice = computed(() => {
    const days = this.daysCount();
    if (days <= 0) return 0;

    return this.cartItems().reduce((sum, item) => sum + (item.pricePerDay * days), 0);
  });

  setDates(start: string, end: string): void {
    this.startDate.set(start);
    this.endDate.set(end);
  }

  addToCart(item: Equipment): void {
    this.cartItems.update(items => [...items, item]);
  }

  removeFromCart(id: number): void {
    this.cartItems.update(items => {
      const idx = items.findIndex(i => i.id === id);
      if (idx !== -1) {
        const copy = [...items];
        copy.splice(idx, 1);
        return copy;
      }
      return items;
    });
  }

  clearCart(): void {
    this.cartItems.set([]);
  }
}
