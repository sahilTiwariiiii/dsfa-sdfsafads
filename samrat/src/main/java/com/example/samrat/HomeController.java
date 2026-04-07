package com.example.samrat;

import com.example.samrat.core.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<BaseResponse<String>> home() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Welcome to Samrat API", null, "Service is running successfully."));
    }
}
