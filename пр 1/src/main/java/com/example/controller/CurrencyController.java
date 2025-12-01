package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class CurrencyController {

    // Примерные курсы валют (рубли к иностранной валюте)
    private static final Map<String, Double> EXCHANGE_RATES = new LinkedHashMap<>();
    private static final Map<String, String> CURRENCY_NAMES = new LinkedHashMap<>();
    
    static {
        EXCHANGE_RATES.put("USD", 90.0);   // 1 USD = 90 RUB
        EXCHANGE_RATES.put("EUR", 98.0);   // 1 EUR = 98 RUB
        EXCHANGE_RATES.put("GBP", 114.0);  // 1 GBP = 114 RUB
        EXCHANGE_RATES.put("CNY", 12.5);   // 1 CNY = 12.5 RUB
        EXCHANGE_RATES.put("JPY", 0.6);    // 1 JPY = 0.6 RUB
        
        CURRENCY_NAMES.put("USD", "Доллар США");
        CURRENCY_NAMES.put("EUR", "Евро");
        CURRENCY_NAMES.put("GBP", "Фунт стерлингов");
        CURRENCY_NAMES.put("CNY", "Китайский юань");
        CURRENCY_NAMES.put("JPY", "Японская иена");
    }

    @GetMapping("/converter")
    public String converter(Model model) {
        model.addAttribute("currencies", CURRENCY_NAMES);
        return "converter";
    }

    @PostMapping("/converter")
    public String convert(
            @RequestParam("amount") double amount,
            @RequestParam("currency") String currency,
            Model model) {
        
        model.addAttribute("currencies", CURRENCY_NAMES);
        model.addAttribute("amount", amount);
        model.addAttribute("selectedCurrency", currency);
        
        if (EXCHANGE_RATES.containsKey(currency)) {
            double rate = EXCHANGE_RATES.get(currency);
            double result = amount / rate;
            model.addAttribute("result", result);
            model.addAttribute("currencyName", CURRENCY_NAMES.get(currency));
            model.addAttribute("rate", rate);
        } else {
            model.addAttribute("error", "Неизвестная валюта");
        }
        
        return "converter";
    }
}

