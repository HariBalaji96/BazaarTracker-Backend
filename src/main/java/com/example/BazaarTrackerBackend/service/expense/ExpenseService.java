package com.example.BazaarTrackerBackend.service.expense;

import com.example.BazaarTrackerBackend.dto.expense.ExpenseRequest;
import com.example.BazaarTrackerBackend.dto.expense.ExpenseResponse;

import com.example.BazaarTrackerBackend.mapper.ExpenseMapper;

import com.example.BazaarTrackerBackend.model.entity.Expense;

import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;

import com.example.BazaarTrackerBackend.constants.CollectionNames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    private static final String COLLECTION = CollectionNames.EXPENSES;

    // ✅ CREATE EXPENSE
    public ExpenseResponse createExpense(ExpenseRequest request) throws Exception {

        // ⚠️ Step 1: Validate
        if (request.getAmount() <= 0) {
            throw new RuntimeException("Expense amount must be greater than 0");
        }

        if (request.getCategory() == null || request.getCategory().isBlank()) {
            throw new RuntimeException("Expense category is required");
        }

        // ⚠️ Step 2: Map to entity
        Expense expense = ExpenseMapper.toEntity(request);

        expense.setCreatedAt(LocalDateTime.now());
        expense.setUpdatedAt(LocalDateTime.now());

        // ⚠️ Step 3: Save
        Expense saved = firestoreRepository.save(COLLECTION, expense);

        return ExpenseMapper.toResponse(saved);
    }

    // ✅ GET ALL EXPENSES
    public List<ExpenseResponse> getAllExpenses() throws Exception {

        return firestoreRepository.findAll(COLLECTION, Expense.class)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    // ✅ GET EXPENSE BY ID
    public ExpenseResponse getExpenseById(String id) throws Exception {

        Expense expense = firestoreRepository.findById(COLLECTION, id, Expense.class);

        if (expense == null) {
            throw new RuntimeException("Expense not found");
        }

        return ExpenseMapper.toResponse(expense);
    }
}