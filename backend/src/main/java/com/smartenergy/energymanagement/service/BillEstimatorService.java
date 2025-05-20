package com.smartenergy.energymanagement.service;

import org.apache.commons.csv.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class BillEstimatorService {

    // Map user emails to corresponding CSV filenames
    private static final Map<String, String> emailToCsvMap = new HashMap<>();

    static {
        emailToCsvMap.put("innovatech@gmail.com", "innova_tech.csv");
        emailToCsvMap.put("greenpower@gmail.com", "green_power_ltd.csv");
        emailToCsvMap.put("energymaster@gmail.com", "energy_masters.csv");
        emailToCsvMap.put("techiesolutions@gmail.com", "techie_solutions.csv");
    }

    // Electricity pricing per kWh for each category
    private static final Map<String, Double> categoryRates = new HashMap<>();

    static {
        categoryRates.put("Light (kWh)", 5.0);
        categoryRates.put("AC (kWh)", 8.5);
        categoryRates.put("Computers (kWh)", 6.0);
        categoryRates.put("Lift (kWh)", 7.0);
        categoryRates.put("Fan (kWh)", 4.5);
        categoryRates.put("Other (kWh)", 5.5);
    }

    // Method to estimate bill based on email
    public Map<String, Object> estimateBill(String email) throws IOException {
        String fileName = emailToCsvMap.get(email);
        if (fileName == null) {
            throw new IOException("No CSV file found for email: " + email);
        }

        // Locate the CSV file in resources folder
        Resource resource = new ClassPathResource("data/" + fileName);
        if (!resource.exists()) {
            throw new IOException("CSV file not found: " + fileName);
        }

        // Read data from CSV and sum all values
        Map<String, Double> usageData = new HashMap<>();
        for (String category : categoryRates.keySet()) {
            usageData.put(category, 0.0); // Initialize with 0
        }

        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                for (String category : categoryRates.keySet()) {
                    String value = record.get(category);
                    if (value != null && !value.isEmpty()) {
                        try {
                            double currentValue = usageData.getOrDefault(category, 0.0);
                            usageData.put(category, currentValue + Double.parseDouble(value)); // SUM instead of replace
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number format for " + category + ": " + value);
                        }
                    }
                }
            }
        }

        // Calculate bill for each category
        Map<String, Object> billDetails = new LinkedHashMap<>();
        double totalUnits = 0;
        
        for (Map.Entry<String, Double> entry : usageData.entrySet()) {
            double cost = entry.getValue() * categoryRates.get(entry.getKey());
            billDetails.put(entry.getKey(), String.format("%.2f kWh | ₹%.2f", entry.getValue(), cost));
            totalUnits += entry.getValue();
        }

        // Apply Tamil Nadu slab-based calculation
        double totalBill = calculateTamilNaduBill(totalUnits);
        billDetails.put("Total Units Consumed", String.format("%.2f kWh", totalUnits));
        billDetails.put("Total Bill", "₹" + String.format("%.2f", totalBill));

        return billDetails;
    }

    // Tamil Nadu slab-wise billing calculation
    private double calculateTamilNaduBill(double totalUnits) {
        double bill = 0.0;

        if (totalUnits > 400) {
            bill += (totalUnits - 400) * 6.30;
            totalUnits = 400;
        }
        if (totalUnits > 200) {
            bill += (totalUnits - 200) * 4.70;
            totalUnits = 200;
        }
        if (totalUnits > 100) {
            bill += (totalUnits - 100) * 2.35;
            totalUnits = 100;
        }

        // First 100 units are free (so we don't add any cost)
        return bill;
    }
}
