package com.smartenergy.energymanagement.service;

import com.smartenergy.energymanagement.util.CsvMappingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class PredictionService {

    @Value("${ai.prediction.url}")
    private String predictionApiUrl;

    private final RestTemplate restTemplate;

    public PredictionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map getPredictions(String email, String date, String hour) throws IOException {
        // Step 1: Find the CSV filename for this email
        String fileName = CsvMappingUtil.getCsvFileName(email);
        if (fileName == null) {
            throw new IOException("No CSV file found for email: " + email);
        }

        // Step 2: Load the CSV file from classpath
        ClassPathResource csvFile = new ClassPathResource("data/" + fileName);
        if (!csvFile.exists()) {
            throw new IOException("CSV file not found: " + fileName);
        }
        byte[] csvBytes = Files.readAllBytes(csvFile.getFile().toPath());

        // Step 3: Build multipart form data with date, hour, and CSV file
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("date", date);
        body.add("hour", hour);
        body.add("file", new ByteArrayResource(csvBytes) {
            @Override
            public String getFilename() {
                return fileName;
            }
        });

        // Step 4: Set headers and build request entity
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Step 5: Call the AI prediction API and return the response body
        ResponseEntity<Map> response = restTemplate.exchange(
            predictionApiUrl, HttpMethod.POST, requestEntity, Map.class
        );
        return response.getBody();
    }
}
