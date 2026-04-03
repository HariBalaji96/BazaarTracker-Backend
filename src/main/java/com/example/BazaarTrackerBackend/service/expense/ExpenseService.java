package com.example.BazaarTrackerBackend.service.expense;

import com.example.BazaarTrackerBackend.dto.expense.ExpenseRequest;
import com.example.BazaarTrackerBackend.dto.expense.ExpenseResponse;
import com.example.BazaarTrackerBackend.exception.CustomException;
import com.example.BazaarTrackerBackend.mapper.ExpenseMapper;
import com.example.BazaarTrackerBackend.model.entity.Expense;
import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;
import com.example.BazaarTrackerBackend.constants.CollectionNames;
import com.example.BazaarTrackerBackend.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.google.cloud.Timestamp;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    @Autowired
    private DashboardService dashboardService;

    private static final String COLLECTION = CollectionNames.EXPENSES;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public ExpenseResponse createExpense(ExpenseRequest request) throws Exception {

        if (request.getAmount() <= 0) {
            throw new CustomException("Expense amount must be greater than 0", 400);
        }

        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new CustomException("Expense category is required", 400);
        }

        Expense expense = ExpenseMapper.toEntity(request);

        String userId = getCurrentUserId();
        expense.setUserId(userId);
        expense.setCreatedAt(Timestamp.now());
        expense.setUpdatedAt(Timestamp.now());

        Expense saved = firestoreRepository.save(COLLECTION, expense);

        // Update Dashboard
        dashboardService.recordExpense(userId, request.getAmount());

        return ExpenseMapper.toResponse(saved);
    }

    public List<ExpenseResponse> getAllExpenses() throws Exception {
        return firestoreRepository.findByField(COLLECTION, "userId", getCurrentUserId(), Expense.class)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    public ExpenseResponse getExpenseById(String id) throws Exception {
        Expense expense = firestoreRepository.findById(COLLECTION, id, Expense.class);

        if (expense == null) {
            throw new CustomException("Expense not found", 404);
        }

        if (expense.getUserId() == null || !expense.getUserId().equals(getCurrentUserId())) {
            throw new CustomException("Unauthorized access", 403);
        }

        return ExpenseMapper.toResponse(expense);
    }
}
