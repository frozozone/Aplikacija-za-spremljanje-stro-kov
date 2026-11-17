package com.example.backend.storitve;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class IzracunDnevnegaLimitaTest {
    private static BigDecimal znesek(String vrednost) {
        return new BigDecimal(vrednost);
    }

    @Test
    void uspesenPrimer() { //AC1: priliv 600, do vceraj 300, danes 12 -> limit 20, se 8
        DnevniLimitOdgovor o = IzracunDnevnegaLimita.izracunaj(znesek("600.00"), znesek("300.00"), znesek("12.00"), LocalDate.of(2026, 11, 16));
        assertEquals(15, o.preostaliDnevi());
        assertEquals(znesek("20.00"), o.dnevniLimit());
        assertEquals(znesek("8.00"), o.danesLahkoPorabisSe());
        assertEquals(znesek("312.00"), o.porabljenoTaMesec());
        assertEquals(znesek("0.00"), o.presezenoDanes());
        assertEquals(znesek("0.00"), o.presezenoTaMesec());
    }

    @Test
    void limitDanesPresezen() { //AC2: danes porabljeno 25 pri limitu 20 -> preseceno 5
        DnevniLimitOdgovor o = IzracunDnevnegaLimita.izracunaj(znesek("600.00"), znesek("300.00"), znesek("25.00"), LocalDate.of(2026, 11, 16));
        assertEquals(znesek("0.00"), o.danesLahkoPorabisSe());
        assertEquals(znesek("5.00"), o.presezenoDanes());
    }

    @Test
    void prilivPresezen() { //AC3: poraba 650 pri prilivu 600 -> preseceno ta mesec 50, limit 0
        DnevniLimitOdgovor o = IzracunDnevnegaLimita.izracunaj(znesek("600.00"), znesek("640.00"), znesek("10.00"), LocalDate.of(2026, 11, 16));
        assertEquals(znesek("0.00"), o.dnevniLimit());
        assertEquals(znesek("50.00"), o.presezenoTaMesec());
    }

    @Test
    void zadnjiDanVMesecu() { //AC6: 30. 11. -> en dan ostane, ves preostali znesek je na voljo
        DnevniLimitOdgovor o = IzracunDnevnegaLimita.izracunaj(znesek("600.00"), znesek("570.00"), znesek("0.00"), LocalDate.of(2026, 11, 30));
        assertEquals(1, o.preostaliDnevi());
        assertEquals(znesek("30.00"), o.danesLahkoPorabisSe());
    }

    @Test
    void limitSeZaokrozaNavzdol() { //100 / 3 dni = 33.333... -> 33.33 (navzdol)
        DnevniLimitOdgovor o = IzracunDnevnegaLimita.izracunaj(znesek("100.00"), znesek("0.00"), znesek("0.00"), LocalDate.of(2026, 11, 28)); //3 dnevi: 33.333...
        assertEquals(znesek("33.33"), o.dnevniLimit());
    }
}
