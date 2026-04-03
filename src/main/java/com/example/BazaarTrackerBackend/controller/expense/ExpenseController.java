package com.example.BazaarTrackerBackend.controller.expense;

import com.example.BazaarTrackerBackend.dto.expense.*;
import com.example.BazaarTrackerBackend.service.expense.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@RequestBody ExpenseRequest request) throws Exception {
        return ResponseEntity.ok(expenseService.createExpense(request));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAll() throws Exception {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }
}