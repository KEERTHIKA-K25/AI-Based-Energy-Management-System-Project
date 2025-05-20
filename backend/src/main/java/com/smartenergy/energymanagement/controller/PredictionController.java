package com.smartenergy.energymanagement.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping("/api/predictions")
@CrossOrigin(origins = "http://localhost:3000")
public class PredictionController {

    @Value("${ai.prediction.url}")
    private String predictionApiUrl;

    private final RestTemplate restTemplate;

    // Mapping user emails to CSV file paths
    private static final Map<String, String> userCsvMap = Map.of(
        "techiesolutions@gmail.com", "data/techie_solutions.csv",
        "innovatech@gmail.com", "data/innova_tech.csv",
        "greenpower@gmail.com", "data/green_power_ltd.csv",
        "energymaster@gmail.com", "data/energy_masters.csv"
    );

    public PredictionController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getPredictions(
            @RequestParam("email") String email,
            @RequestParam("date") String date,
            @RequestParam("hour") String hour) {

        try {
            // Get CSV file path based on email
            String csvFileName = userCsvMap.get(email);
            if (csvFileName == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No CSV file found for email: " + email);
            }

            // Load CSV file from src/main/resources
            ClassPathResource csvFile = new ClassPathResource(csvFileName);
            byte[] csvBytes = Files.readAllBytes(csvFile.getFile().toPath());

            // Prepare multipart form data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("date", date);
            body.add("hour", hour);
            body.add("file", new ByteArrayResource(csvBytes) {
                @Override
                public String getFilename() {
                    return csvFileName; // Correct filename for the AI API
                }
            });

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Send POST request to AI model's API
            ResponseEntity<Map> response = restTemplate.exchange(predictionApiUrl, HttpMethod.POST, requestEntity, Map.class);

            // Return AI predictions
            return ResponseEntity.ok(response.getBody());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading CSV file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Prediction failed: " + e.getMessage());
        }
    }
}
