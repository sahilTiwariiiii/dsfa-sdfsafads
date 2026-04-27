package com.example.samrat.core.exception;

import com.example.samrat.core.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiErrorFallbackController {

    @GetMapping("/api-error/401")
    public ResponseEntity<BaseResponse<Void>> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new BaseResponse<>(false, "Please login to access this API.", "UNAUTHORIZED", null));
    }

    @GetMapping("/api-error/403")
    public ResponseEntity<BaseResponse<Void>> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new BaseResponse<>(false, "You don't have permission to do this action.", "FORBIDDEN", null));
    }
}
