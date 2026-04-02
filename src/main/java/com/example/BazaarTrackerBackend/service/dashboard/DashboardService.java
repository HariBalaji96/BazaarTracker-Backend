package com.example.BazaarTrackerBackend.service.dashboard;

import com.example.BazaarTrackerBackend.dto.dashboard.DashboardResponse;

import com.example.BazaarTrackerBackend.model.entity.Sale;
import com.example.BazaarTrackerBackend.model.entity.Payment;
import com.example.BazaarTrackerBackend.model.entity.Expense;

import com.example.BazaarTrackerBackend.model.enums.SaleType;

import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;

import com.example.BazaarTrackerBackend.constants.CollectionNames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    // ✅ GET DASHBOARD DATA
    public DashboardResponse getDashboard() throws Exception {

        // 🔹 Fetch all data
        List<Sale> sales = firestoreRepository.findAll(
                CollectionNames.SALES, Sale.class);

        List<Payment> payments = firestoreRepository.findAll(
                CollectionNames.PAYMENTS, Payment.class);

        List<Expense> expenses = firestoreRepository.findAll(
                CollectionNames.EXPENSES, Expense.class);

        double totalSales = 0;
        double totalCredit = 0;
        double totalPayments = 0;
        double totalExpenses = 0;

        // 🔹 Calculate sales + credit
        for (Sale sale : sales) {
            totalSales += sale.getTotalAmount();

            if (sale.getSaleType() == SaleType.CREDIT) {
                totalCredit += sale.getTotalAmount();
            }
        }

        // 🔹 Calculate payments
        for (Payment payment : payments) {
            totalPayments += payment.getAmount();
        }

        // 🔹 Calculate expenses
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount();
        }

        // 🔥 Profit calculation
        double profit = totalSales - totalExpenses;

        // 🔹 Build response
        DashboardResponse response = new DashboardResponse();

        response.setTotalSales(totalSales);
        response.setTotalCredit(totalCredit);
        response.setTotalPayments(totalPayments);
        response.setTotalExpenses(totalExpenses);
        response.setProfit(profit);

        return response;
    }
}