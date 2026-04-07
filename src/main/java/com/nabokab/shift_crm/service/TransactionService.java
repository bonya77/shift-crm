package com.nabokab.shift_crm.service;

import com.nabokab.shift_crm.model.Seller;
import com.nabokab.shift_crm.model.Transaction;
import com.nabokab.shift_crm.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id){
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException(("Transaction not found")));
    }

    public Transaction createTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionBySeller(Long sellerId){
        return transactionRepository.findAllBySellerId(sellerId);
    }

    public Seller getMostProductiveSeller(LocalDateTime start, LocalDateTime end){
        List<Seller> sellers = transactionRepository.findMostProductiveSellers(start, end);
        return sellers.isEmpty() ? null : sellers.getFirst();
    }
    public List<Seller> getSellersWithSalesLessThan(LocalDateTime start, LocalDateTime end, java.math.BigDecimal minAmount){
        return transactionRepository.findSellersWithSumLessThan(start, end, minAmount);
    }

    public Map<String, Object> getBestPeriod(Long sellerId, int hours){
        List<Transaction> transactions = transactionRepository.findAllBySellerId(sellerId);

        if(transactions.isEmpty()){
            return Map.of("massge", "Seller hasn't transactions");
        }

        transactions.sort(Comparator.comparing(Transaction::getTransactionDate));

        double maxAmount = 0;
        LocalDateTime bestStart = null;
        LocalDateTime bestEnd = null;

        for(int i = 0; i < transactions.size(); i++){
            double currentWindowSum = 0;
            LocalDateTime windowStart = transactions.get(i).getTransactionDate();
            LocalDateTime windowEndLimit = windowStart.plusHours(hours);

            int j = i;
            while(j < transactions.size() && !transactions.get(j).getTransactionDate().isAfter(windowEndLimit)){
                currentWindowSum += transactions.get(j).getAmount().doubleValue();
                j++;
            }
            if(currentWindowSum > maxAmount){
                maxAmount = currentWindowSum;
                bestStart = windowStart;
                bestEnd = transactions.get(j-1).getTransactionDate();
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("sellerId", sellerId);
        result.put("windowSizeHours", hours);
        result.put("bestPeriodStart", bestStart);
        result.put("bestPeriodEnd", bestEnd);
        result.put("maxAmountIn", maxAmount);

        return result;
    }

}
