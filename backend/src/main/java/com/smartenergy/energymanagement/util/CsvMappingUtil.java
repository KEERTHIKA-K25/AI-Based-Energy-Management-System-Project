package com.smartenergy.energymanagement.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that holds the single source of truth
 * for email-to-CSV filename mapping across the application.
 *
 * Previously this map was duplicated in:
 *   - BillEstimatorService.java
 *   - RecommendationService.java
 *   - PredictionController.java
 *
 * Now it lives here ONCE. All three classes call getCsvFileName().
 */
public class CsvMappingUtil {

    private static final Map<String, String> emailToCsvMap = new HashMap<>();

    static {
        emailToCsvMap.put("innovatech@gmail.com", "innova_tech.csv");
        emailToCsvMap.put("greenpower@gmail.com", "green_power_ltd.csv");
        emailToCsvMap.put("energymaster@gmail.com", "energy_masters.csv");
        emailToCsvMap.put("techiesolutions@gmail.com", "techie_solutions.csv");
    }

    /**
     * Returns the CSV filename for the given email.
     * Returns null if the email is not registered.
     */
    public static String getCsvFileName(String email) {
        return emailToCsvMap.get(email);
    }
}
