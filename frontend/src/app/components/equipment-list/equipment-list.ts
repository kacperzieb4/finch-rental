import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService, Equipment } from '../../services/api.service';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-equipment-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './equipment-list.html',
  styleUrl: './equipment-list.css'
})
export class EquipmentList implements OnInit {
  equipmentList = signal<Equipment[]>([]);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);

  categories = ['Aparat', 'Kamera', 'Obiektyw', 'Lighting', 'Audio', 'Grip'];

  constructor(
    private apiService: ApiService,
    public cartService: CartService
  ) {}

  ngOnInit(): void {
    this.loadEquipment();
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

  loadEquipment(): void {
    const start = this.cartService.startDate();
    const end = this.cartService.endDate();

    this.isLoading.set(true);
    this.errorMessage.set(null);

    if (start && end && this.daysCount > 0) {
      this.apiService.getAvailableEquipment(start, end).subscribe({
        next: (data) => {
          this.equipmentList.set(data);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error(err);
          this.errorMessage.set('Nie udało się pobrać dostępności sprzętu.');
          this.isLoading.set(false);
        }
      });
    } else {
      this.apiService.getEquipmentList().subscribe({
        next: (data) => {
          this.equipmentList.set(data);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error(err);
          this.errorMessage.set('Nie udało się pobrać listy sprzętu. Upewnij się, że backend działa.');
          this.isLoading.set(false);
        }
      });
    }
  }

  onDatesChange(start: string, end: string): void {
    this.cartService.setDates(start, end);
    this.loadEquipment();
  }

  getEquipmentByCategory(category: string): Equipment[] {
    return this.equipmentList().filter(eq => eq.category === category);
  }

  getCategoryLabel(category: string): string {
    switch (category) {
      case 'Aparat': return 'Aparaty Fotograficzne';
      case 'Kamera': return 'Kamery Video';
      case 'Obiektyw': return 'Obiektywy';
      case 'Lighting': return 'Oświetlenie';
      case 'Audio': return 'Dźwięk';
      case 'Grip': return 'Statywy i Akcesoria';
      default: return category;
    }
  }

  addToCart(item: Equipment): void {
    this.cartService.addToCart(item, 1);
  }

  getCartCount(id: number): number {
    const item = this.cartService.cartItems().find(i => i.equipment.id === id);
    return item ? item.quantity : 0;
  }

  hasAvailableUnits(item: Equipment): boolean {
    if (!this.startDate || !this.endDate || this.daysCount <= 0) {
      return false;
    }
    const inCart = this.getCartCount(item.id!);
    const maxAvailable = item.availableUnits !== undefined ? item.availableUnits : (item.quantity || 1);
    return inCart < maxAvailable;
  }
}
