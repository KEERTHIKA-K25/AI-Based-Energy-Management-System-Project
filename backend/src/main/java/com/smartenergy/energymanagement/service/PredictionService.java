package com.smartenergy.energymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class PredictionService {

    @Autowired
    private RestTemplate restTemplate;

    private final String AI_PREDICTION_API = "https://api-wpkw.onrender.com/predict";

    public String getPrediction(MultipartFile file, Map<String, Object> requestData) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Prepare request body
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        body.add("data", requestData);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(AI_PREDICTION_API, requestEntity, String.class);
        return response.getBody();
    }
}
