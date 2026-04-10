package com.nabokab.shift_crm.service;

import com.nabokab.shift_crm.model.Transaction;
import com.nabokab.shift_crm.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testGetBestPeriodByCount() {

        Long sellerId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Transaction t1 = Transaction.builder()
                .transactionDate(now)
                .amount(new BigDecimal("100"))
                .build();
        Transaction t2 = Transaction.builder()
                .transactionDate(now.plusHours(2))
                .amount(new BigDecimal("200"))
                .build();
        Transaction t3 = Transaction.builder()
                .transactionDate(now.plusDays(5))
                .amount(new BigDecimal("300"))
                .build();

        when(transactionRepository.findAllBySellerId(sellerId)).thenReturn(new java.util.ArrayList<>(List.of(t1, t2, t3)));

        Map<String, Object> result = transactionService.getBestPeriod(sellerId, 24);
        assertNotNull(result);
        assertEquals(2, result.get("transactionCount"), "Должно быть найдено 2 транзакции в лучшем периоде");
        assertEquals(now, result.get("bestPeriodStart"));
    }

    @Test
    void testGetBestPeriodEmpty() {
        Long sellerId = 1L;
        when(transactionRepository.findAllBySellerId(sellerId)).thenReturn(List.of());

        Map<String, Object> result = transactionService.getBestPeriod(sellerId, 24);

        assertEquals("Seller hasn't transactions", result.get("message"));
    }
}