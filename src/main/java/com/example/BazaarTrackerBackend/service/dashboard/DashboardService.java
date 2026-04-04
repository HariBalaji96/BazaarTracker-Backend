package com.example.BazaarTrackerBackend.service.dashboard;

import com.example.BazaarTrackerBackend.dto.dashboard.DashboardResponse;
import com.example.BazaarTrackerBackend.model.entity.DashboardCache;
import com.example.BazaarTrackerBackend.model.enums.SaleType;
import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;
import com.google.cloud.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    private static final String COLLECTION = "dashboard_cache";

    public DashboardResponse getDashboard() throws Exception {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DashboardCache cache = getOrCreateCache(userId);

        DashboardResponse response = new DashboardResponse();
        response.setTotalSales(cache.getTotalSales());
        response.setTotalCredit(cache.getTotalCredit());
        response.setTotalPayments(cache.getTotalPayments());
        response.setTotalExpenses(cache.getTotalExpenses());
        response.setProfit(cache.getProfit());

        return response;
    }

    public synchronized void recordSale(String userId, double amount, SaleType saleType) throws Exception {
        DashboardCache cache = getOrCreateCache(userId);
        cache.setTotalSales(cache.getTotalSales() + amount);
        if (saleType == SaleType.CREDIT) {
            cache.setTotalCredit(cache.getTotalCredit() + amount);
        }
        updateProfitAndSave(cache);
    }

    public synchronized void reverseSale(String userId, double amount, SaleType saleType) throws Exception {
        DashboardCache cache = getOrCreateCache(userId);
        cache.setTotalSales(cache.getTotalSales() - amount);
        if (saleType == SaleType.CREDIT) {
            cache.setTotalCredit(cache.getTotalCredit() - amount);
        }
        updateProfitAndSave(cache);
    }

    public synchronized void recordPayment(String userId, double amount) throws Exception {
        DashboardCache cache = getOrCreateCache(userId);
        cache.setTotalPayments(cache.getTotalPayments() + amount);
        updateProfitAndSave(cache);
    }

    public synchronized void recordExpense(String userId, double amount) throws Exception {
        DashboardCache cache = getOrCreateCache(userId);
        cache.setTotalExpenses(cache.getTotalExpenses() + amount);
        updateProfitAndSave(cache);
    }

    public synchronized void reverseExpense(String userId, double amount) throws Exception {
        DashboardCache cache = getOrCreateCache(userId);
        cache.setTotalExpenses(cache.getTotalExpenses() - amount);
        updateProfitAndSave(cache);
    }

    private DashboardCache getOrCreateCache(String userId) throws Exception {
        DashboardCache cache = firestoreRepository.findById(COLLECTION, userId, DashboardCache.class);
        if (cache == null) {
            cache = new DashboardCache();
            cache.setId(userId);
            cache.setUserId(userId);
            cache.setTotalSales(0);
            cache.setTotalCredit(0);
            cache.setTotalPayments(0);
            cache.setTotalExpenses(0);
            cache.setProfit(0);
            cache.setLastUpdated(Timestamp.now());
        }
        return cache;
    }

    private void updateProfitAndSave(DashboardCache cache) throws Exception {
        cache.setProfit(cache.getTotalSales() - cache.getTotalExpenses());
        cache.setLastUpdated(Timestamp.now());
        firestoreRepository.save(COLLECTION, cache);
    }
}