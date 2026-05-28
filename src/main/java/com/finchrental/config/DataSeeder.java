package com.finchrental.config;

import com.finchrental.entity.Equipment;
import com.finchrental.entity.Reservation;
import com.finchrental.entity.ReservationItem;
import com.finchrental.entity.ReservationStatus;
import com.finchrental.repository.EquipmentRepository;
import com.finchrental.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EquipmentRepository equipmentRepository;
    private final ReservationRepository reservationRepository;

    public DataSeeder(EquipmentRepository equipmentRepository, ReservationRepository reservationRepository) {
        this.equipmentRepository = equipmentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (equipmentRepository.count() > 0) {
            return;
        }

        Equipment sonyA7 = Equipment.builder()
                .name("Sony A7IV")
                .category("Aparat")
                .description("Pelnoklatkowy aparat bezlusterkowy z przetwornikiem obrazu Exmor R CMOS. Wyposazony w procesor BIONZ XR, zaawansowany autofokus Real-time Tracking oraz stabilizacje matrycy 5.5 stopnia. Nagrywanie wideo w 4K 60p.\n\nSpecyfikacja techniczna:\n• Rozdzielczosc: 33.0 MP\n• Bagnet obiektywu: Sony E-mount\n• Stabilizacja: 5-osiowa, do 5.5 stopni\n• System AF: 759 punktow detekcji fazy\n• Zapis wideo: 4K 60p (Super 35), 4K 30p (pelna klatka, nadprobkowanie z 7K)")
                .pricePerDay(BigDecimal.valueOf(150.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Sony A7IV"))
                .build();

        Equipment canonR6 = Equipment.builder()
                .name("Canon EOS R6 Mark II")
                .category("Aparat")
                .description("Wszechstronny aparat bezlusterkowy z matryca CMOS i doskonala stabilizacja obrazu. Umozliwia superszybkie zdjecia seryjne oraz nagrywanie wideo 4K 60p bez przyciecia, nadprobkowane z 6K.\n\nSpecyfikacja techniczna:\n• Rozdzielczosc: 24.2 MP\n• Bagnet obiektywu: Canon RF-mount\n• Zdjecia seryjne: do 40 kl./s (migawka elektroniczna)\n• Stabilizacja: do 8 stopni (w polaczeniu z obiektywami OIS)\n• System AF: Dual Pixel CMOS AF II")
                .pricePerDay(BigDecimal.valueOf(180.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("Canon EOS R6 Mark II"))
                .build();

        Equipment sonyFX3 = Equipment.builder()
                .name("Sony FX3")
                .category("Kamera")
                .description("Profesjonalna, kompaktowa kamera kinowa z serii Cinema Line. Wyjatkowa czulosc ISO, zakres dynamiczny 15+ stopni, profil kolorow S-Cinetone oraz aktywny uklad chlodzenia pozwalajacy na dluga prace bez przegrzewania.\n\nSpecyfikacja techniczna:\n• Rozdzielczosc (wideo): 10.2 MP\n• Bagnet obiektywu: Sony E-mount\n• Czulosc ISO: do 409 600 (Dual Base ISO 800 / 12800)\n• Klatkarz: do 4K 120 kl./s\n• Profile obrazu: S-Log3, S-Cinetone, HLG")
                .pricePerDay(BigDecimal.valueOf(250.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("Sony FX3"))
                .build();

        Equipment bmpcc6k = Equipment.builder()
                .name("Blackmagic Pocket 6K Pro")
                .category("Kamera")
                .description("Zaawansowana cyfrowa kamera filmowa z sensorem Super 35. Posiada wbudowane, zmotoryzowane filtry ND, uchylny ekran dotykowy HDR o jasnosci 1500 nitow oraz profesjonalne zlacza audio XLR.\n\nSpecyfikacja techniczna:\n• Rozdzielczosc: 6144 x 3456 (6K)\n• Bagnet obiektywu: Canon EF-mount\n• Filtry ND: wbudowane (2, 4, 6 stopni)\n• Zapis: Blackmagic RAW, ProRes\n• Porty: USB-C (zapis na SSD), 2x mini XLR (48V)")
                .pricePerDay(BigDecimal.valueOf(220.00))
                .available(true)
                .quantity(1)
                .imageUrl(findImageForProduct("Blackmagic Pocket 6K Pro"))
                .build();

        Equipment sony2470 = Equipment.builder()
                .name("Sony FE 24-70mm f/2.8 GM II")
                .category("Obiektyw")
                .description("Profesjonalny, jasny obiektyw zmiennoogniskowy klasy G Master o stalym swietle f/2.8. Wybitna ostrosc i kontrast w calym zakresie ogniskowych, wyposazony w 4 silniki liniowe XD zapewniajacce blyskawiczny i cichy autofokus.\n\nSpecyfikacja techniczna:\n• Ogniskowa: 24-70 mm\n• Maksymalna przeslona: f/2.8 (stala)\n• Bagnet obiektywu: Sony E-mount\n• Minimalna odleglosc ostrzenia: 21 cm\n• Waga: 695 g (najlzejszy w swojej klasie)")
                .pricePerDay(BigDecimal.valueOf(80.00))
                .available(true)
                .quantity(4)
                .imageUrl(findImageForProduct("Sony FE 24-70mm f/2.8 GM II"))
                .build();

        Equipment sigma1835 = Equipment.builder()
                .name("Sigma 18-35mm f/1.8 Art")
                .category("Obiektyw")
                .description("Legendarny, niezwykle jasny obiektyw zmiennoogniskowy zaprojektowany do matryc APS-C. Oferuje unikalne, stale swiatlo f/1.8 w calym zakresie ogniskowych, dajac wyjatkowa plastyke i piekny efekt bokeh.\n\nSpecyfikacja techniczna:\n• Ogniskowa: 18-35 mm (odpowiednik 27-52.5 mm dla pelnej klatki)\n• Maksymalna przeslona: f/1.8 (stala)\n• Bagnet obiektywu: Canon EF-mount\n• Seria: Art (najwyzsza jakosc optyczna)\n• Minimalna odleglosc ostrzenia: 28 cm")
                .pricePerDay(BigDecimal.valueOf(50.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Sigma 18-35mm f/1.8 Art"))
                .build();

        Equipment sandiskSd = Equipment.builder()
                .name("SanDisk Extreme Pro SDXC 128GB")
                .category("Pamięci")
                .description("Szybka i niezawodna karta pamieci SDXC o pojemnosci 128 GB. Idealna do nagrywania wideo w rozdzielczosci 4K UHD oraz szybkich zdjec seryjnych.\n\nSpecyfikacja techniczna:\n• Pojemnosc: 128 GB\n• Klasa predkosci: V30, UHS-I U3\n• Predkosc odczytu: do 200 MB/s\n• Predkosc zapisu: do 90 MB/s")
                .pricePerDay(BigDecimal.valueOf(15.00))
                .available(true)
                .quantity(5)
                .imageUrl(findImageForProduct("SanDisk Extreme Pro SDXC 128GB"))
                .build();

        Equipment lexarCf = Equipment.builder()
                .name("Lexar Professional CFexpress Type A 160GB")
                .category("Pamięci")
                .description("Profesjonalna karta CFexpress typu A stworzona z mysla o wymagajacych aparatach (np. Sony A7IV, FX3). Gwarantuje płynne nagrywanie wideo 8K i 4K w najwyzszych klatkarzach.\n\nSpecyfikacja techniczna:\n• Pojemnosc: 160 GB\n• Standard karty: CFexpress Type A\n• Predkosc odczytu: do 900 MB/s\n• Predkosc zapisu: do 800 MB/s")
                .pricePerDay(BigDecimal.valueOf(40.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Lexar Professional CFexpress Type A 160GB"))
                .build();

        Equipment samsungSsd = Equipment.builder()
                .name("Samsung Portable SSD T7 Shield 2TB")
                .category("Pamięci")
                .description("Niezwykle wytrzymaly i szybki przenosny dysk SSD o pojemnosci 2 TB. Obudowa odporna na upadki z wysokosci do 3 metrow oraz pyl i wode (certyfikat IP65). Doskonaly do bezposredniego zapisu z kamer Blackmagic.\n\nSpecyfikacja techniczna:\n• Pojemnosc: 2 TB\n• Predkosc odczytu: do 1050 MB/s\n• Predkosc zapisu: do 1000 MB/s\n• Klasa odpornosci: IP65 (pyloszczelnosc i wodoodpornosc)\n• Interfejs: USB 3.2 Gen 2 (USB-C)")
                .pricePerDay(BigDecimal.valueOf(30.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Samsung Portable SSD T7 Shield 2TB"))
                .build();

        Equipment aputure300d = Equipment.builder()
                .name("Aputure LS 300d II")
                .category("Lighting")
                .description("Profesjonalna lampa LED o duzej mocy i temperaturze barwowej swiatla dziennego 5600K. Wspolczynnik CRI/TLCI na poziomie 96+ gwarantuje idealne odwzorowanie barw.\n\nSpecyfikacja techniczna:\n• Temperatura barwowa: 5600K (swiatlo dzienne)\n• Jasnosc: do 80 000 luksow (z czasza Hyper Reflector z 1m)\n• Mocowanie modyfikatorow: Bowens S-mount\n• Sterowanie: DMX512, aplikacja Sidus Link\n• Wspolczynniki: CRI 96+, TLCI 96+")
                .pricePerDay(BigDecimal.valueOf(70.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("Aputure LS 300d II"))
                .build();

        Equipment zoomH6 = Equipment.builder()
                .name("Zoom H6")
                .category("Audio")
                .description("Zaawansowany, 6-sciezkowy rejestrator dzwieku z systemem wymiennych kapsul mikrofonowych. Idealny do nagrywania podcastow, sciezek filmowych oraz koncertow.\n\nSpecyfikacja techniczna:\n• Sciezki nagrywania: do 6 jednoczesnie\n• Wejscia audio: 4x combo XLR/TRS z zasilaniem phantom\n• Kapsuly w zestawie: XY oraz Mid-Side\n• Zasilanie: baterie 4x AA lub zasilacz USB")
                .pricePerDay(BigDecimal.valueOf(40.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Zoom H6"))
                .build();

        Equipment djiRonin = Equipment.builder()
                .name("DJI RS 3 Pro")
                .category("Grip")
                .description("Flagowy, trzyosiowy gimbal stabilizacyjny przeznaczony do profesjonalnych zestawow filmowych o wadze do 4.5 kg. Wyposazony w ramiona z wlokna weglowego i automatyczne blokady osi.\n\nSpecyfikacja techniczna:\n• Udzwig: do 4.5 kg\n• Waga gimbala: 1.5 kg\n• Ekran: dotykowy OLED 1.8 cala\n• Blokada osi: automatyczna\n• Czas pracy baterii: do 12 godzin")
                .pricePerDay(BigDecimal.valueOf(60.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("DJI RS 3 Pro"))
                .build();

        Equipment amaran60 = Equipment.builder()
                .name("Aputure Amaran 60d")
                .category("Lighting")
                .description("Ultrakompaktowa i lekka lampa LED typu spotlight o mocy 65W z temperatura barwowa 5600K. Idealna do mobilnego studia, nagran vlogow czy oswietlenia wypelnijacego.\n\nSpecyfikacja techniczna:\n• Moc: 65W (odpowiednik 300W wolframu)\n• Temperatura barwowa: 5600K\n• Sterowanie: aplikacja Sidus Link na telefonie\n• Wspolczynniki: CRI 96+, TLCI 96+\n• Zasilanie: zasilacz sieciowy, akumulatory NP-F lub D-Tap")
                .pricePerDay(BigDecimal.valueOf(45.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("Aputure Amaran 60d"))
                .build();

        Equipment sennheiserMke = Equipment.builder()
                .name("Sennheiser MKE 600")
                .category("Audio")
                .description("Profesjonalny mikrofon kierunkowy typu shotgun, idealny do pracy na planie filmowym. Charakteryzuje sie wysoka kierunkowoscia, doskonale eliminuje szumy z otoczenia oraz posiada filtr gornoprzepustowy.\n\nSpecyfikacja techniczna:\n• Charakterystyka: superkardioidalna/lobar\n• Pasmo przenoszenia: 40 Hz - 20 000 Hz\n• Zasilanie: phantom 48V lub bateria 1.5V AA\n• Filtr: tlumienie niskich czestotliwosci (low-cut)")
                .pricePerDay(BigDecimal.valueOf(50.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Sennheiser MKE 600"))
                .build();

        Equipment canon2470 = Equipment.builder()
                .name("Canon EF 24-70mm f2.8L II USM")
                .category("Obiektyw")
                .description("Legendarny, profesjonalny obiektyw zoom z kultowej serii L Canona. Oferuje bezkompromisowa ostrosc obrazu w calym zakresie ogniskowych, stale swiatlo f/2.8 i niezwykle szybki silnik USM.\n\nSpecyfikacja techniczna:\n• Ogniskowa: 24-70 mm\n• Maksymalna przeslona: f/2.8 (stala)\n• Bagnet obiektywu: Canon EF-mount\n• Konstrukcja: uszczelniona przed kurzem i wilgocia\n• Silnik AF: pierscieniowy USM (ultradzwiekowy)")
                .pricePerDay(BigDecimal.valueOf(75.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("Canon EF 24-70mm f2.8L II USM"))
                .build();

        equipmentRepository.saveAll(List.of(
                sonyA7, canonR6, sonyFX3, bmpcc6k, sony2470, sigma1835,
                sandiskSd, lexarCf, samsungSsd,
                aputure300d, zoomH6, djiRonin,
                amaran60, sennheiserMke, canon2470
        ));

        LocalDate today = LocalDate.now();

        Reservation res1 = Reservation.builder()
                .startDate(today.plusDays(2))
                .endDate(today.plusDays(4))
                .customerName("Jan Kowalski")
                .customerEmail("jan.kowalski@example.com")
                .customerPhone("500600700")
                .status(ReservationStatus.PENDING)
                .build();

        long days1 = ChronoUnit.DAYS.between(res1.getStartDate(), res1.getEndDate()) + 1;
        BigDecimal item1Price = sonyA7.getPricePerDay();
        BigDecimal item2Price = sony2470.getPricePerDay();
        BigDecimal totalCost1 = item1Price.add(item2Price).multiply(BigDecimal.valueOf(days1));
        res1.setTotalPrice(totalCost1);

        List<ReservationItem> items1 = new ArrayList<>();
        items1.add(ReservationItem.builder().reservation(res1).equipment(sonyA7).pricePerDay(item1Price).build());
        items1.add(ReservationItem.builder().reservation(res1).equipment(sony2470).pricePerDay(item2Price).build());
        res1.setItems(items1);

        Reservation res2 = Reservation.builder()
                .startDate(today.plusDays(10))
                .endDate(today.plusDays(14))
                .customerName("Anna Nowak")
                .customerEmail("anna.nowak@example.com")
                .customerPhone("600700800")
                .status(ReservationStatus.APPROVED)
                .build();

        long days2 = ChronoUnit.DAYS.between(res2.getStartDate(), res2.getEndDate()) + 1;
        BigDecimal res2Item1Price = sonyFX3.getPricePerDay();
        BigDecimal res2Item2Price = djiRonin.getPricePerDay();
        BigDecimal totalCost2 = res2Item1Price.add(res2Item2Price).multiply(BigDecimal.valueOf(days2));
        res2.setTotalPrice(totalCost2);

        List<ReservationItem> items2 = new ArrayList<>();
        items2.add(ReservationItem.builder().reservation(res2).equipment(sonyFX3).pricePerDay(res2Item1Price).build());
        items2.add(ReservationItem.builder().reservation(res2).equipment(djiRonin).pricePerDay(res2Item2Price).build());
        res2.setItems(items2);

        Reservation res3 = Reservation.builder()
                .startDate(today.plusDays(5))
                .endDate(today.plusDays(7))
                .customerName("Piotr Wisniewski")
                .customerEmail("piotr.wisniewski@example.com")
                .customerPhone("501502503")
                .status(ReservationStatus.APPROVED)
                .build();

        long days3 = ChronoUnit.DAYS.between(res3.getStartDate(), res3.getEndDate()) + 1;
        BigDecimal res3Item1Price = aputure300d.getPricePerDay();
        BigDecimal res3Item2Price = amaran60.getPricePerDay();
        BigDecimal totalCost3 = res3Item1Price.add(res3Item2Price).multiply(BigDecimal.valueOf(days3));
        res3.setTotalPrice(totalCost3);

        List<ReservationItem> items3 = new ArrayList<>();
        items3.add(ReservationItem.builder().reservation(res3).equipment(aputure300d).pricePerDay(res3Item1Price).build());
        items3.add(ReservationItem.builder().reservation(res3).equipment(amaran60).pricePerDay(res3Item2Price).build());
        res3.setItems(items3);

        reservationRepository.saveAll(List.of(res1, res2, res3));
    }

    private String findImageForProduct(String productName) {
        String baseName = productName.trim().replaceAll("[^a-zA-Z0-9.-]", "_").replaceAll("_+", "_");
        java.io.File uploadDir = new java.io.File("uploads");
        if (uploadDir.exists() && uploadDir.isDirectory()) {
            java.io.File[] files = uploadDir.listFiles();
            if (files != null) {
                for (java.io.File file : files) {
                    if (file.isFile()) {
                        String name = file.getName();
                        int lastDot = name.lastIndexOf('.');
                        if (lastDot > 0) {
                            String nameWithoutExt = name.substring(0, lastDot);
                            if (nameWithoutExt.equalsIgnoreCase(baseName)) {
                                return "/uploads/" + name;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}

