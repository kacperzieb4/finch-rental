import { Injectable, signal, computed } from '@angular/core';
import { Equipment } from './api.service';

export interface CartItem {
  equipment: Equipment;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  cartItems = signal<CartItem[]>([]);
  
  startDate = signal<string>('');
  endDate = signal<string>('');

  minDate = computed(() => {
    return this.formatDate(new Date());
  });

  maxDate = computed(() => {
    const max = new Date();
    max.setMonth(max.getMonth() + 3);
    return this.formatDate(max);
  });

  private formatDate(date: Date): string {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  daysCount = computed(() => {
    const start = this.startDate();
    const end = this.endDate();
    if (!start || !end) return 0;

    const s = new Date(start);
    const e = new Date(end);
    if (isNaN(s.getTime()) || isNaN(e.getTime()) || e < s) return 0;

    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const max = new Date(today);
    max.setMonth(max.getMonth() + 3);

    const sCompare = new Date(s);
    sCompare.setHours(0, 0, 0, 0);
    const eCompare = new Date(e);
    eCompare.setHours(0, 0, 0, 0);

    if (sCompare < today || eCompare > max) return 0;

    const diffTime = Math.abs(eCompare.getTime() - sCompare.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    return diffDays;
  });

  totalItemsCount = computed(() => {
    return this.cartItems().reduce((sum, item) => sum + item.quantity, 0);
  });

  totalPrice = computed(() => {
    const days = this.daysCount();
    if (days <= 0) return 0;

    return this.cartItems().reduce((sum, item) => sum + (item.equipment.pricePerDay * item.quantity * days), 0);
  });

  setDates(start: string, end: string): void {
    this.startDate.set(start);
    this.endDate.set(end);
  }

  addToCart(item: Equipment, quantity: number = 1): void {
    this.cartItems.update(items => {
      const existing = items.find(i => i.equipment.id === item.id);
      if (existing) {
        return items.map(i => i.equipment.id === item.id 
          ? { ...i, quantity: i.quantity + quantity } 
          : i
        );
      }
      return [...items, { equipment: item, quantity }];
    });
  }

  removeFromCart(id: number): void {
    this.cartItems.update(items => items.filter(i => i.equipment.id !== id));
  }

  updateQuantity(id: number, quantity: number): void {
    this.cartItems.update(items => {
      if (quantity <= 0) {
        return items.filter(i => i.equipment.id !== id);
      }
      return items.map(i => i.equipment.id === id 
        ? { ...i, quantity } 
        : i
      );
    });
  }

  clearCart(): void {
    this.cartItems.set([]);
  }
}
