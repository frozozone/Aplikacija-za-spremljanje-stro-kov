package com.example.backend.storitve;

import java.math.BigDecimal;

//odgovor endpointa GET/ dnevni-limit (frontend iz njega izpise kartico z dnevnim limitom), vsi zneski so v € na dve decimalki
public record DnevniLimitOdgovor(
        BigDecimal priliv, //mesecni priliv za tekoci mesec
        BigDecimal porabljenoDoVceraj, //vsota stroskov od 1. dneva v mesecu do vceraj
        BigDecimal porabljenoDanes, //vsota stroskov z danasnjim datumom
        BigDecimal porabljenoTaMesec, //porabljenoDoVceraj + porabljenoDanes
        int preostaliDnevi, //dnevi do konca meseca, danasnji dan se steje zraven
        BigDecimal dnevniLimit, //priporocena poraba na dan (ob zacetku danasnjega dne)
        BigDecimal danesLahkoPorabisSe, //dnevniLimit - porabljenoDanes, nikoli negativno
        BigDecimal presezenoDanes, //za koliko je danes presezen limit, 0 ce ni
        BigDecimal presezenoTaMesec //za koliko je poraba presegla priliv, 0 ce ni
) {}