package com.example.BazaarTrackerBackend.controller.dashboard;

import com.example.BazaarTrackerBackend.dto.dashboard.DashboardResponse;
import com.example.BazaarTrackerBackend.service.dashboard.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() throws Exception {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }
}