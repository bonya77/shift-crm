package com.nabokab.shift_crm.controller;

import com.nabokab.shift_crm.model.Transaction;
import com.nabokab.shift_crm.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
