package com.example.backend.storitve;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

//racuna dnevni limit iz podanih zneskov
//izracun brez baze, da ga je lahko testirati
public final class IzracunDnevnegaLimita {

    private IzracunDnevnegaLimita(){} //samo staticne metode, objektov ne ustvarjamo

    public static DnevniLimitOdgovor izracunaj( //iz priliva, porabe do vceraj, porabe danes in danasnjega datuma sestavi celoten odgovor
            BigDecimal priliv,
            BigDecimal porabljenoDoVceraj,
            BigDecimal porabljenoDanes,
            LocalDate danes
    ){
        BigDecimal porabljenoTaMesec = porabljenoDoVceraj.add(porabljenoDanes);
        int preostaliDnevi = izracunajPreostaleDni(danes);
        BigDecimal dnevniLimit = izracunajDnevniLimit(priliv, porabljenoDoVceraj, preostaliDnevi);

        return new DnevniLimitOdgovor(
                zaokrozi(priliv),
                zaokrozi(porabljenoDoVceraj),
                zaokrozi(porabljenoDanes),
                zaokrozi(porabljenoTaMesec),
                preostaliDnevi,
                dnevniLimit, //ce je danes porabljeno manj od limita, se kaze koliko lahko se porabi, sicer koliko je presezeno
                //pozitivniDel poskrbi, da je eden od obeh zneskov vedno 0
                zaokrozi(pozitivniDel(dnevniLimit.subtract(porabljenoDanes))),
                zaokrozi(pozitivniDel(porabljenoDanes.subtract(dnevniLimit))),
                zaokrozi(pozitivniDel(porabljenoTaMesec.subtract(priliv)))
        );
    }

    //zadnji dan v mesecu - danasnji dan + 1 (danasnji dan se steje zraven); +1 ker danasnjega dneva se ni konec, ga stejemo zraven
    static int izracunajPreostaleDni(LocalDate danes) {
        return danes.lengthOfMonth() - danes.getDayOfMonth() + 1;
    }

    //dnevni limit = kar je ostalo od priliva do vceraj, enakomerno razdeljeno na preostale dni
    //zaokrozeno navzdol, da priporocilo nikoli ni previsoko
    static BigDecimal izracunajDnevniLimit(BigDecimal priliv, BigDecimal porabljenoDoVceraj, int preostaliDnevi) {
        if (porabljenoDoVceraj.compareTo(priliv) >= 0) {
            return zaokrozi(BigDecimal.ZERO); //priliv je ze porbaljen, negativnega limita ne prikazujemo
        }
        return priliv.subtract(porabljenoDoVceraj)
                .divide(BigDecimal.valueOf(preostaliDnevi), 2, RoundingMode.DOWN);
    }

    private static BigDecimal pozitivniDel(BigDecimal znesek) { //vrne znesek, ce je pozitiven, sicer 0 (max(znesek, 0))
        return znesek.max(BigDecimal.ZERO);
    }

    private static BigDecimal zaokrozi(BigDecimal znesek) { //vsi zneski v odgovoru imajo natanko 2 decimalki
        return znesek.setScale(2, RoundingMode.DOWN);
    }
}