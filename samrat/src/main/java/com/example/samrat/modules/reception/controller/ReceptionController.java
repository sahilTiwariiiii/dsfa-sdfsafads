package com.example.samrat.modules.reception.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.reception.entity.VisitorLog;
import com.example.samrat.modules.reception.service.ReceptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception")
@RequiredArgsConstructor
@Tag(name = "Reception Management", description = "APIs for visitor logging and front desk operations")
public class ReceptionController {

    private final ReceptionService receptionService;

    @PostMapping("/visitor/entry")
    @PreAuthorize("hasAuthority('RECEPTION_WRITE')")
    @Operation(summary = "Log visitor entry", description = "Records a new visitor entering the hospital")
    public ResponseEntity<BaseResponse<VisitorLog>> logEntry(@RequestBody VisitorLog visitorLog) {
        VisitorLog savedLog = receptionService.logEntry(visitorLog);
        return ResponseEntity.ok(new BaseResponse<>(true, "Visitor entry logged", null, savedLog));
    }

    @PutMapping("/visitor/exit/{id}")
    @PreAuthorize("hasAuthority('RECEPTION_WRITE')")
    @Operation(summary = "Log visitor exit", description = "Records a visitor exiting the hospital")
    public ResponseEntity<BaseResponse<VisitorLog>> logExit(@PathVariable Long id) {
        VisitorLog savedLog = receptionService.logExit(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Visitor exit logged", null, savedLog));
    }

    @GetMapping("/visitors/active")
    @PreAuthorize("hasAuthority('RECEPTION_READ')")
    @Operation(summary = "Get active visitors", description = "Lists all visitors currently inside the hospital")
    public ResponseEntity<BaseResponse<List<VisitorLog>>> getActiveVisitors() {
        List<VisitorLog> visitors = receptionService.getActiveVisitors();
        return ResponseEntity.ok(new BaseResponse<>(true, "Active visitors found", null, visitors));
    }
}
