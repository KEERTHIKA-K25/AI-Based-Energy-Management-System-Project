package com.smartenergy.energymanagement.controller;

import com.smartenergy.energymanagement.service.RecommendationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationController {

    private static final Logger logger = Logger.getLogger(RecommendationController.class.getName());

    @Value("${ai.recommendation.url}")
    private String recommendationApiUrl;

    private final RestTemplate restTemplate;
    private final RecommendationService recommendationService;

    public RecommendationController(RestTemplate restTemplate, RecommendationService recommendationService) {
        this.restTemplate = restTemplate;
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public ResponseEntity<?> getRecommendations(@RequestParam String email) {
        logger.info("Received request for recommendations with email: " + email);

        try {
            // Fetch CSV file dynamically
            Resource fileResource = recommendationService.getCsvFile(email);
            logger.info("Found CSV file: " + fileResource.getFilename());

            // Prepare request body
            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            // Set headers for multipart request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Send request to AI API
            logger.info("Sending request to AI recommendation API: " + recommendationApiUrl);
            ResponseEntity<Map> response = restTemplate.exchange(
                recommendationApiUrl, HttpMethod.POST, requestEntity, Map.class
            );

            logger.info("Received response from AI API: " + response.getBody());
            return ResponseEntity.ok(response.getBody());
        } catch (IOException e) {
            logger.warning("Error fetching CSV file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CSV file not found for email: " + email);
        } catch (Exception e) {
            logger.severe("Failed to fetch recommendations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch recommendations: " + e.getMessage());
        }
    }
}
