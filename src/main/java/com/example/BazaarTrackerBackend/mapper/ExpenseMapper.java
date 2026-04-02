package com.example.BazaarTrackerBackend.mapper;

import com.example.BazaarTrackerBackend.dto.expense.ExpenseRequest;
import com.example.BazaarTrackerBackend.dto.expense.ExpenseResponse;
import com.example.BazaarTrackerBackend.model.entity.Expense;

public class ExpenseMapper {

    public static Expense toEntity(ExpenseRequest request) {

        Expense expense = new Expense();
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());

        return expense;
    }

    public static ExpenseResponse toResponse(Expense expense) {

        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setCategory(expense.getCategory());
        response.setAmount(expense.getAmount());

        return response;
    }
}