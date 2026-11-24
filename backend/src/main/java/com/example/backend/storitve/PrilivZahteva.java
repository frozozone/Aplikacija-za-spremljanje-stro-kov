package com.example.backend.storitve;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

//body zahteve PUT /priliv, znesek mora biti večji od 0 in je obvezen
public record PrilivZahteva(
    
    @NotNull(message="znesek priliva je obvezen")
    @Positive(message="priliv mora biti večji od 0")
    BigDecimal znesek) {}