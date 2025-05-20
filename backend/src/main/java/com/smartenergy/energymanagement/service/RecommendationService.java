package com.smartenergy.energymanagement.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecommendationService {

    // Email-to-CSV mapping
    private static final Map<String, String> emailToCsvMap = new HashMap<>();

    static {
        emailToCsvMap.put("innovatech@gmail.com", "innova_tech.csv");
        emailToCsvMap.put("greenpower@gmail.com", "green_power_ltd.csv");
        emailToCsvMap.put("energymaster@gmail.com", "energy_masters.csv");
        emailToCsvMap.put("techiesolutions@gmail.com", "techie_solutions.csv");
    }

    public Resource getCsvFile(String email) throws IOException {
        String fileName = emailToCsvMap.get(email);
        System.out.println("Looking for CSV file: " + fileName); // Debug Log
        if (fileName == null) {
            throw new IOException("CSV file not found for email: " + email);
        }

        Resource fileResource = new ClassPathResource("data/" + fileName);
        System.out.println("CSV file found: " + fileResource.exists()); // Debug Log
        if (!fileResource.exists()) {
            throw new IOException("CSV file not found: " + fileName);
        }
        return fileResource;
    }
}
