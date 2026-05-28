import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CartService } from './services/cart.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, CommonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('FinchRental');

  constructor(
    public cartService: CartService,
    private router: Router
  ) {}

  scrollToCategory(category: string): void {
    const elementId = 'category-' + category;
    const isEquipmentPage = this.router.url === '/equipment' || this.router.url === '/' || this.router.url === '';
    
    if (isEquipmentPage) {
      this.doScroll(elementId);
    } else {
      this.router.navigate(['/equipment']).then(() => {
        setTimeout(() => {
          this.doScroll(elementId);
        }, 150);
      });
    }
  }

  private doScroll(elementId: string): void {
    const element = document.getElementById(elementId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}

