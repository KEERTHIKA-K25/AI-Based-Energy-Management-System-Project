package com.smartenergy.energymanagement.controller;

import com.smartenergy.energymanagement.service.PredictionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/predictions")
@CrossOrigin(origins = "http://localhost:3000")
public class PredictionController {

    private static final Logger logger = Logger.getLogger(PredictionController.class.getName());

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping
    public ResponseEntity<?> getPredictions(
            @RequestParam("email") String email,
            @RequestParam("date") String date,
            @RequestParam("hour") String hour) {

        logger.info("Received prediction request for email: " + email);
        try {
            Map result = predictionService.getPredictions(email, date, hour);
            logger.info("Prediction successful for email: " + email);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            logger.warning("CSV not found for email: " + email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CSV file not found: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Prediction failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Prediction failed: " + e.getMessage());
        }
    }
}
