import { Routes } from '@angular/router';
import { EquipmentList } from './components/equipment-list/equipment-list';
import { ReservationForm } from './components/reservation-form/reservation-form';
import { AdminPanel } from './components/admin-panel/admin-panel';
import { CartComponent } from './components/cart/cart.component';

export const routes: Routes = [
  { path: '', redirectTo: 'equipment', pathMatch: 'full' },
  { path: 'equipment', component: EquipmentList },
  { path: 'rent/:id', component: ReservationForm },
  { path: 'cart', component: CartComponent },
  { path: 'admin', component: AdminPanel },
  { path: '**', redirectTo: 'equipment' }
];
