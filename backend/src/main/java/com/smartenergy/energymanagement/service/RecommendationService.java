package com.smartenergy.energymanagement.service;

import com.smartenergy.energymanagement.util.CsvMappingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
public class RecommendationService {

    @Value("${ai.recommendation.url}")
    private String recommendationApiUrl;

    private final RestTemplate restTemplate;

    public RecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map getRecommendations(String email) throws IOException {
        // Step 1: Get CSV file Resource for this email
        Resource fileResource = getCsvFile(email);

        // Step 2: Build multipart body with the CSV file
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        // Step 3: Set headers for multipart request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Step 4: Call AI recommendation API and return response body
        ResponseEntity<Map> response = restTemplate.exchange(
            recommendationApiUrl, HttpMethod.POST, requestEntity, Map.class
        );
        return response.getBody();
    }

    private Resource getCsvFile(String email) throws IOException {
        String fileName = CsvMappingUtil.getCsvFileName(email);
        if (fileName == null) {
            throw new IOException("CSV file not found for email: " + email);
        }

        Resource fileResource = new ClassPathResource("data/" + fileName);
        if (!fileResource.exists()) {
            throw new IOException("CSV file not found: " + fileName);
        }
        return fileResource;
    }
}
