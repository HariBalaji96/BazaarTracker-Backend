package com.example.BazaarTrackerBackend.mapper;

import com.example.BazaarTrackerBackend.dto.expense.ExpenseRequest;
import com.example.BazaarTrackerBackend.dto.expense.ExpenseResponse;
import com.example.BazaarTrackerBackend.model.entity.Expense;

public class ExpenseMapper {

    public static Expense toEntity(ExpenseRequest request) {

        Expense expense = new Expense();
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());

        if (request.getDate() != null && !request.getDate().isEmpty()) {
            try {
                expense.setDate(com.google.cloud.Timestamp.ofTimeMicroseconds(java.time.Instant.parse(request.getDate()).toEpochMilli() * 1000));
            } catch (Exception e) {
                expense.setDate(com.google.cloud.Timestamp.now());
            }
        } else {
            expense.setDate(com.google.cloud.Timestamp.now());
        }

        return expense;
    }

    public static ExpenseResponse toResponse(Expense expense) {

        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setUserId(expense.getUserId());
        response.setCategory(expense.getCategory());
        response.setAmount(expense.getAmount());
        response.setDescription(expense.getDescription());

        if (expense.getDate() != null) {
            response.setDate(expense.getDate().toDate().toString());
        }
        if (expense.getCreatedAt() != null) {
            response.setCreatedAt(expense.getCreatedAt().toDate().toString());
        }
        if (expense.getUpdatedAt() != null) {
            response.setUpdatedAt(expense.getUpdatedAt().toDate().toString());
        }

        return response;
    }
}