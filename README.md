# FinchRental - Wypożyczalnia Sprzętu Foto-Video

FinchRental to aplikacja webowa typu Single Page Application (SPA) do zarządzania rezerwacjami wypożyczania sprzętu fotograficznego oraz filmowego. 
Projekt łączy architekturę Spring Boot na backendzie z frontendem stworzonym w Angularze.


## Główne funkcjonalności aplikacji:
*   **Katalog produktów**:
    *   Podział sprzętu na kategorie z nawigacją w headerze.
    *   Prezentacja zdjęcia, opisu i ceny produktu w jednolitym formacie.
    *   Wygodny dostęp do szczegółowej karty każdego z produktu.
*   **Karta produktu**:
    *   Dedykowany widok z opisem technicznym.
    *   Dynamiczny kalendarz rezerwacyjny z wbudowaną walidacją blokującą wybór dat z przeszłości oraz wybiegających powyżej 3 miesięcy w przód.
    *   Integracja z systemem sprawdzania dostępności - w czasie rzeczywistym podaje liczbę wolnych sztuk wybranego modelu w wybranym przedziale czasowym.
    *   Blokada liczby dodawanych sztuk zabezpieczająca przed przekroczeniem fizycznych stanów magazynowych w wybranym terminie.
*   **Koszyk**:
    *   Dodawanie wielu egzemplarzy tego samego sprzętu oraz łączenie różnych modeli w jedno zamówienie.
    *   Automatyczne wyliczanie całkowitego kosztu wypożyczenia.
    *   Możliwość edycji ilości i przedziału dat bezpośrednio z widoku koszyka z ponowną walidacją stanów magazynowych w locie.
    *   Formularz zamówienia zbierający dane kontaktowe klienta (imię, nazwisko, e-mail, telefon) i przesyłający rezerwację do bazy danych.
*   **Panel Administracyjny**:
    *   Logowanie za pomocą Spring Security HTTP Basic dla roli administratora.
    *   **Zarządzanie katalogiem sprzętu**:
        *   Dodawanie i edycja sprzętów (nazwa, kategoria, opis, cena za dzień, ilość na stanie, dostępność).
        *   Przesyłanie zdjęć produktów na serwer ze zautomatyzowanym generowaniem nazw plików (przykładowo: `Sony_FX3.jpg`).
    *   **Zarządzanie Rezerwacjami**:
        *   Podgląd wszystkich rezerwacji w systemie.
        *   Zatwierdzanie lub odrzucanie statusu rezerwacji za pomocą jednego kliknięcia.
        *   Możliwość całkowitego usuwania rezerwacji z bazy danych.
    *   **Dzienny Raport Zapotrzebowania**:
        *   Możliwość sprawdzenia aktywnych wypożyczeń na konkretny dzień.
        *   **Sumowanie ilości zarezerwowanego sprzętu** - Czytelne plakietki sumujące zapotrzebowanie ilościowe na poszczególne modele w wybranym dniu.
        *   **Moduł wydruku** - Możliwość wygenerowania raportu zapotrzebowania w formie wydruku A4/pliku PDF.


## Spełnienie kryteriów oceny projektu

Projekt realizuje wszystkie wymagania techniczne oraz funkcjonalne:

*   **Działający Spring Boot** - Backend aplikacji zbudowany jest w oparciu o framework Spring Boot 4.
*   **Połączenie z bazą danych**-  Projekt wykorzystuje bazę danych H2, w pełni skonfigurowaną w pliku `application.properties` (konsola H2 dostępna pod adresem `/h2-console`).
*   **CRUD dla encji** - Zaimplementowano kompletne operacje CRUD dla powiązanych encji:
    *   `Equipment` - Zarządzanie katalogiem sprzętu.
    *   `Reservation` - Zarządzanie koszykiem, rezerwacjami i ich statusami.
*   **Struktura warstwowa** - Backend posiada przejrzysty i poprawny podział na warstwy:
    *   `Controller` - obsługa żądań HTTP REST.
    *   `Service` - logika biznesowa oraz walidacja.
    *   `Repository` - dostęp do danych przez Spring Data JPA.
    *   `Entity` - modele bazodanowe Hibernate.
    *   `DTO` - separacja modeli API od bazy danych.
*   **DTO + Walidacja danych** - Dane wejściowe przesyłane do kontrolerów są mapowane na klasy DTO (`ReservationRequestDTO`, `EquipmentRequestDTO`) i weryfikowane za pomocą adnotacji walidacyjnych (`@NotBlank`, `@Min`, `@Size`, `@Email`).
*   **Obsługa błędów** - Globalna obsługa wyjątków zrealizowana przy użyciu `@ControllerAdvice` w klasie `GlobalExceptionHandler`, która tłumaczy błędy aplikacyjne na obiekty JSON typu `ErrorResponse` z kodami statusów HTTP (400 Bad Request, 404 Not Found).
*   **Security** - Spring Security zabezpiecza wrażliwe punkty końcowe panelu administracyjnego (tworzenie, edycja i usuwanie sprzętu oraz rezerwacji) z użyciem uwierzytelniania HTTP Basic. Dane dostępowe administratora skonfigurowane w `SecurityConfig.java`:
    *   **Login**: admin
    *   **Hasło**: admin123
*   **Unit Testy** - Napisano i przetestowano logikę biznesową w klasie `ReservationServiceTest` przy użyciu JUnit 5 oraz Mockito. Testy weryfikują m.in. limity dostępności sprzętu, blokowanie rezerwacji wstecz oraz sprawdzanie poprawności zakresu dat (max 3 miesiące w przód). Wszystkie 8 testów wykonuje się z wynikiem pomyślnym.
*   **Spring Events** - Zaimplementowano wzorzec Event Listener przy użyciu `ApplicationEventPublisher`. Utworzenie rezerwacji publikuje zdarzenie `ReservationCreatedEvent`, które jest asynchronicznie przechwytywane i logowane przez `ReservationEventListener`.
*   **Czysty Kod** - Projekt został zaimplementowany zgodnie z architekturą warstwową Spring Boot (Controller / Service / Repository), z zachowaniem czytelnego podziału odpowiedzialności pomiędzy komponentami.
*   **Frontend (Angular)** - Stworzono responsywną aplikację w technologii Angular 21 wykorzystującą architekturę komponentów standalone oraz mechanizm sygnałów (`Signals`). Konsumuje ona REST-owe API Spring Boota (pobieranie katalogu, dynamiczny koszyk, wyszukiwanie dostępności dla dat, składanie rezerwacji, autoryzowany panel admina z raportami dziennymi i wydrukiem).

## Uruchomienie projektu

### 1. Wymagania wstępne
*   **Java** - JDK 17 lub nowszy
*   **Node.js** - wersja 18 lub nowsza + npm
*   **Maven** - opcjonalnie - projekt zawiera wrapper `mvnw`

### 2. Uruchomienie Backendu
1.  Otwórz terminal w katalogu głównym projektu (`finch-rental`).
2.  Uruchom aplikację za pomocą wrappera Maven:
    ```bash
    ./mvnw spring-boot:run
    ```
3.  Serwer backendowy uruchomi się na porcie **8080** (`http://localhost:8080`).
4.  Konsola bazy danych H2 dostępna jest pod adresem: `http://localhost:8080/h2-console`
    *   **JDBC URL**: `jdbc:h2:mem:finch_rental`
    *   **User Name**: `sa`
    *   **Password**:
5.  Aplikacja automatycznie zasila bazę danych początkowymi danymi (seeder) oraz wiąże zdjęcia sprzętów z katalogu `uploads/`.

### 3. Uruchomienie Frontend (Angular)
1.  W osobnym oknie terminala przejdź do katalogu frontendu:
    ```bash
    cd frontend
    ```
2.  Zainstaluj wymagane pakiety npm:
    ```bash
    npm install
    ```
3.  Uruchom deweloperski serwer Angular:
    ```bash
    npm start
    ```
4.  Aplikacja kliencka otworzy się na porcie **4200** i jest dostępna w przeglądarce pod adresem: `http://localhost:4200`

