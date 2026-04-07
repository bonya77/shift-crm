package com.nabokab.shift_crm.controller;

import com.nabokab.shift_crm.model.Seller;
import com.nabokab.shift_crm.model.Transaction;
import com.nabokab.shift_crm.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactioonController {
    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTranssactionDyId(@PathVariable Long id){
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction){
        return transactionService.createTransaction((transaction));
    }

    @GetMapping("/seller/{sellerId}")
    public List<Transaction> getTransactionBySeller(@PathVariable Long sellerId){
        return transactionService.getTransactionBySeller(sellerId);
    }

    @GetMapping("/analytics/most-productive")
    public Seller getMostProductive(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return transactionService.getMostProductiveSeller(start, end);
    }

    @GetMapping("/analytics/low-sales")
    public List<Seller> getLowSalesSellers(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam java.math.BigDecimal amount) {
        return transactionService.getSellersWithSalesLessThan(start, end, amount);
    }

    @GetMapping("/analytics/best-period/{sellerId}")
    public Map<String, Object> getBestPeriod(@PathVariable Long sellerId, @RequestParam(defaultValue = "24") int hours){
        return transactionService.getBestPeriod(sellerId, hours);
    }

}
