package com.smartenergy.energymanagement.controller;

import com.smartenergy.energymanagement.service.BillEstimatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/bill-estimation")
@CrossOrigin(origins = "http://localhost:3000")
public class BillEstimatorController {

    private static final Logger logger = Logger.getLogger(BillEstimatorController.class.getName());
    private final BillEstimatorService billEstimatorService;

    public BillEstimatorController(BillEstimatorService billEstimatorService) {
        this.billEstimatorService = billEstimatorService;
    }

    @GetMapping
    public ResponseEntity<?> getBillEstimation(@RequestParam String email) {
        logger.info("Received request for bill estimation for email: " + email);
        try {
            Map<String, Object> billDetails = billEstimatorService.estimateBill(email);
            logger.info("Bill estimation successful for email: " + email);
            return ResponseEntity.ok(billDetails);
        } catch (IOException e) {
            logger.warning("CSV file not found for email: " + email);
            return ResponseEntity.status(404).body("CSV file not found for email: " + email);
        } catch (Exception e) {
            logger.severe("Failed to fetch bill estimation: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to fetch bill estimation: " + e.getMessage());
        }
    }
}
