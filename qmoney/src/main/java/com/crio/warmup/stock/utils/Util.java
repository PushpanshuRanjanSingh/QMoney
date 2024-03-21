package com.crio.warmup.stock.utils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util {
    private static Util instance;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Util() {
        // private constructor to prevent instantiation
    }

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    public static LocalDate setPurchaseDate(Object purchaseDate) {
        if (purchaseDate instanceof CharSequence) {
            return LocalDate.parse((CharSequence) purchaseDate, formatter);
        } else {
            return (LocalDate) purchaseDate;
        }
    }
}
