package com.bht.assetmanagement.shared.date;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DateUtils {
    public String createLocalDate() {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = localDate.format(formatter);
        return formattedDate;
    }
}
