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
                .description("Pelnoklatkowy aparat bezlusterkowy z przetwornikiem obrazu Exmor R CMOS o rozdzielczosci 33.0 MP. Wyposazony w procesor BIONZ XR, system Real-time Tracking AF do wykrywania oczu ludzi/zwierzat/ptakow oraz stabilizacje matrycy 5.5 stopnia. Nagrywanie wideo w 4K 60p. Bagnet obiektywu: Sony E-mount.")
                .pricePerDay(BigDecimal.valueOf(150.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Sony A7IV"))
                .build();

        Equipment canonR6 = Equipment.builder()
                .name("Canon EOS R6 Mark II")
                .category("Aparat")
                .description("Wszechstronny aparat bezlusterkowy z matryca CMOS o rozdzielczosci 24.2 MP. Umozliwia superszybkie zdjecia seryjne do 40 kl./s z migawka elektroniczna oraz nagrywanie wideo 4K 60p bez przyciecia nadprobkowane z 6K. Wyposazony w hybrydowa stabilizacje obrazu (IBIS + OIS) do 8 stopni oraz system Dual Pixel CMOS AF II. Bagnet obiektywu: Canon RF-mount.")
                .pricePerDay(BigDecimal.valueOf(180.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("Canon EOS R6 Mark II"))
                .build();

        Equipment sonyFX3 = Equipment.builder()
                .name("Sony FX3")
                .category("Kamera")
                .description("Profesjonalna, kompaktowa kamera kinowa z serii Cinema Line. Wyposazona w pelnoklatkowa matryce CMOS Exmor R o rozdzielczosci 10.2 MP (efektywna dla wideo) z wyjatkowa czuloscia ISO do 409600 i zakresem dynamicznym 15+ stopni. Oferuje nagrywanie 4K do 120 kl./s, profil kolorow S-Cinetone, wyjscie RAW przez HDMI oraz aktywny uklad chlodzenia zapobiegajacy przegrzaniu. Bagnet obiektywu: Sony E-mount.")
                .pricePerDay(BigDecimal.valueOf(250.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("Sony FX3"))
                .build();

        Equipment bmpcc6k = Equipment.builder()
                .name("Blackmagic Pocket 6K Pro")
                .category("Kamera")
                .description("Zaawansowana cyfrowa kamera filmowa z sensorem Super 35 o rozdzielczosci 6144 x 3456 (6K). Posiada wbudowane, zmotoryzowane filtry ND (2, 4 i 6 stopni), uchylny ekran dotykowy HDR o jasnosci 1500 nitow oraz dwa wejscia audio mini XLR z zasilaniem fantomowym 48V. Zapisuje bezposrednio na dyski SSD przez port USB-C lub karty CFast/SD. Bagnet obiektywu: Canon EF-mount.")
                .pricePerDay(BigDecimal.valueOf(220.00))
                .available(true)
                .quantity(1)
                .imageUrl(findImageForProduct("Blackmagic Pocket 6K Pro"))
                .build();

        Equipment sony2470 = Equipment.builder()
                .name("Sony FE 24-70mm f/2.8 GM II")
                .category("Obiektyw")
                .description("Profesjonalny, jasny obiektyw zmiennoogniskowy klasy G Master o stalym swietle f/2.8. Wybitna ostrosc i kontrast w calym zakresie ogniskowych, zminimalizowana aberracja chromatyczna i astygmatyzm. Wyposazony w 4 silniki liniowe XD zapewniajacce blyskawiczny i cichy autofokus. Konstrukcja odporna na pyl i wilgoc. Bagnet obiektywu: Sony E-mount.")
                .pricePerDay(BigDecimal.valueOf(80.00))
                .available(true)
                .quantity(4)
                .imageUrl(findImageForProduct("Sony FE 24-70mm f/2.8 GM II"))
                .build();

        Equipment sigma1835 = Equipment.builder()
                .name("Sigma 18-35mm f/1.8 Art")
                .category("Obiektyw")
                .description("Legendarny, niezwykle jasny obiektyw zmiennoogniskowy zaprojektowany do lustrzanek i bezlusterkowcow z matryca APS-C. Oferuje unikalne, stale swiatlo f/1.8 w calym zakresie ogniskowych (odpowiednik 27-52.5mm dla pelnej klatki), dajac wyjatkowa plastyke obrazu i piekny efekt bokeh. Konstrukcja z serii Art gwarantuje najwyzsza jakosc optyczna. Bagnet obiektywu: Canon EF-mount.")
                .pricePerDay(BigDecimal.valueOf(50.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Sigma 18-35mm f/1.8 Art"))
                .build();

        Equipment sandiskSd = Equipment.builder()
                .name("SanDisk Extreme Pro SDXC 128GB")
                .category("Pamięci")
                .description("Szybka i niezawodna karta pamieci SDXC o pojemnosci 128 GB. Predkosc odczytu do 200 MB/s, zapis do 90 MB/s. Klasa predkosci V30, idealna do nagrywania wideo w rozdzielczosci 4K UHD oraz szybkich zdjec seryjnych. Standard UHS-I U3.")
                .pricePerDay(BigDecimal.valueOf(15.00))
                .available(true)
                .quantity(5)
                .imageUrl(findImageForProduct("SanDisk Extreme Pro SDXC 128GB"))
                .build();

        Equipment lexarCf = Equipment.builder()
                .name("Lexar Professional CFexpress Type A 160GB")
                .category("Pamięci")
                .description("Profesjonalna karta CFexpress typu A stworzona z mysla o wymagajacych aparatach (np. Sony A7IV, FX3). Pojemnosc 160 GB. Predkosc odczytu do 900 MB/s, zapis do 800 MB/s. Gwarantuje płynne nagrywanie wideo 8K i 4K w najwyzszych klatkarzach oraz natychmiastowe oproznianie bufora.")
                .pricePerDay(BigDecimal.valueOf(40.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Lexar Professional CFexpress Type A 160GB"))
                .build();

        Equipment samsungSsd = Equipment.builder()
                .name("Samsung Portable SSD T7 Shield 2TB")
                .category("Pamięci")
                .description("Niezwykle wytrzymaly i szybki przenosny dysk SSD o pojemnosci 2 TB. Interfejs USB 3.2 Gen 2 zapewniający transfer do 1050 MB/s. Obudowa odporna na upadki z wysokosci do 3 metrow oraz pyl i wode (certyfikat IP65). Doskonaly do bezposredniego zapisu z kamer Blackmagic Pocket 6K Pro.")
                .pricePerDay(BigDecimal.valueOf(30.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Samsung Portable SSD T7 Shield 2TB"))
                .build();

        Equipment aputure300d = Equipment.builder()
                .name("Aputure LS 300d II")
                .category("Lighting")
                .description("Profesjonalna lampa LED o duzej mocy i temperaturze barwowej swiatla dziennego 5600K. Generuje strumien swietlny o natezeniu do 80 000 luksow z reflektorem Hyper Reflector. Wspolczynnik CRI/TLCI na poziomie 96+ gwarantuje idealne odwzorowanie barw. Sterowanie bezprzewodowe przez aplikacje Sidus Link lub DMX. Mocowanie modyfikatorow: Bowens S-mount.")
                .pricePerDay(BigDecimal.valueOf(70.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("Aputure LS 300d II"))
                .build();

        Equipment zoomH6 = Equipment.builder()
                .name("Zoom H6")
                .category("Audio")
                .description("Zaawansowany, 6-sciezkowy rejestrator dzwieku z systemem wymiennych kapsul mikrofonowych. W zestawie kapsula XY (XH-6) oraz Mid-Side (MSH-6). Posiada 4 wejscia combo XLR/TRS z niezaleznymi pokretlami regulacji wzmocnienia (gain) i zasilaniem phantom. Idealny do nagrywania podcastow, sciezek filmowych oraz koncertow.")
                .pricePerDay(BigDecimal.valueOf(40.00))
                .available(true)
                .quantity(3)
                .imageUrl(findImageForProduct("Zoom H6"))
                .build();

        Equipment djiRonin = Equipment.builder()
                .name("DJI RS 3 Pro")
                .category("Grip")
                .description("Flagowy, trzyosiowy gimbal stabilizacyjny przeznaczony do profesjonalnych zestawow filmowych o wadze do 4.5 kg. Wyposazony w ramiona z wlokna weglowego, automatyczne blokady osi, kolorowy ekran dotykowy OLED 1.8 cala oraz trzecia generacje algorytmow stabilizacji DJI. Umozliwia bezprzewodowa kontrole nad ostroscia i migawka aparatu.")
                .pricePerDay(BigDecimal.valueOf(60.00))
                .available(true)
                .quantity(2)
                .imageUrl(findImageForProduct("DJI RS 3 Pro"))
                .build();

        equipmentRepository.saveAll(List.of(
                sonyA7, canonR6, sonyFX3, bmpcc6k, sony2470, sigma1835,
                sandiskSd, lexarCf, samsungSsd,
                aputure300d, zoomH6, djiRonin
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

        reservationRepository.saveAll(List.of(res1, res2));
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

