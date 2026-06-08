package com.smartenergy.energymanagement.controller;

import com.smartenergy.energymanagement.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationController {

    private static final Logger logger = Logger.getLogger(RecommendationController.class.getName());

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public ResponseEntity<?> getRecommendations(@RequestParam String email) {
        logger.info("Received request for recommendations with email: " + email);
        try {
            Map result = recommendationService.getRecommendations(email);
            logger.info("Recommendations fetched successfully for email: " + email);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            logger.warning("Error fetching CSV file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CSV file not found for email: " + email);
        } catch (Exception e) {
            logger.severe("Failed to fetch recommendations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch recommendations: " + e.getMessage());
        }
    }
}
