package com.nabokab.shift_crm.service;

import com.nabokab.shift_crm.model.Transaction;
import com.nabokab.shift_crm.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
