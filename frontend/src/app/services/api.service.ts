import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface Equipment {
  id?: number;
  name: string;
  category: string;
  description: string;
  pricePerDay: number;
  available: boolean;
  imageUrl?: string;
  quantity?: number;
  totalUnits?: number;
  availableUnits?: number;
}

export interface ReservationItem {
  id?: number;
  equipmentId: number;
  equipmentName: string;
  pricePerDay: number;
}

export interface Reservation {
  id?: number;
  startDate: string;
  endDate: string;
  customerName: string;
  customerEmail: string;
  customerPhone?: string;
  status?: 'PENDING' | 'APPROVED' | 'REJECTED';
  totalPrice?: number;
  items?: ReservationItem[];
  equipmentIds?: number[];
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getEquipmentList(): Observable<Equipment[]> {
    return this.http.get<Equipment[]>(`${this.baseUrl}/equipment`);
  }

  getAvailableEquipment(startDate: string, endDate: string): Observable<Equipment[]> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<Equipment[]>(`${this.baseUrl}/equipment/available`, { params });
  }

  getEquipment(id: number): Observable<Equipment> {
    return this.http.get<Equipment>(`${this.baseUrl}/equipment/${id}`);
  }

  createEquipment(equipment: Equipment): Observable<Equipment> {
    return this.http.post<Equipment>(
      `${this.baseUrl}/equipment`, 
      equipment, 
      { headers: this.authService.getAuthHeaders() }
    );
  }

  updateEquipment(id: number, equipment: Equipment): Observable<Equipment> {
    return this.http.put<Equipment>(
      `${this.baseUrl}/equipment/${id}`, 
      equipment, 
      { headers: this.authService.getAuthHeaders() }
    );
  }

  deleteEquipment(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.baseUrl}/equipment/${id}`, 
      { headers: this.authService.getAuthHeaders() }
    );
  }

  uploadImage(file: File, productName?: string): Observable<{ url: string }> {
    const formData = new FormData();
    formData.append('file', file);
    let params = new HttpParams();
    if (productName) {
      params = params.set('name', productName);
    }
    return this.http.post<{ url: string }>(
      `${this.baseUrl}/equipment/upload`, 
      formData, 
      { 
        headers: this.authService.getAuthHeaders(),
        params: params
      }
    );
  }

  getReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(
      `${this.baseUrl}/reservations`, 
      { headers: this.authService.getAuthHeaders() }
    );
  }

  createReservation(reservation: Reservation): Observable<Reservation> {
    return this.http.post<Reservation>(`${this.baseUrl}/reservations`, reservation);
  }

  updateReservationStatus(id: number, status: 'APPROVED' | 'REJECTED'): Observable<Reservation> {
    return this.http.put<Reservation>(
      `${this.baseUrl}/reservations/${id}/status`, 
      { status }, 
      { headers: this.authService.getAuthHeaders() }
    );
  }

  deleteReservation(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.baseUrl}/reservations/${id}`, 
      { headers: this.authService.getAuthHeaders() }
    );
  }
}
