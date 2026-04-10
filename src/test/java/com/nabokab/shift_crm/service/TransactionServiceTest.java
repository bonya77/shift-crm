package com.nabokab.shift_crm.service;

import com.nabokab.shift_crm.model.Seller;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testGetTransactionById() {
        Transaction t = Transaction.builder().id(1L).amount(new BigDecimal("500")).build();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(t));

        Transaction result = transactionService.getTransactionById(1L);

        assertEquals(new BigDecimal("500"), result.getAmount());
    }

    @Test
    void testCreateTransaction() {
        Transaction t = new Transaction();
        when(transactionRepository.save(t)).thenReturn(t);

        Transaction result = transactionService.createTransaction(t);

        assertNotNull(result);
        verify(transactionRepository).save(t);
    }

    @Test
    void testGetMostProductiveSeller() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        Seller topSeller = Seller.builder().id(1L).name("Top").build();

        when(transactionRepository.findMostProductiveSellers(start, end))
                .thenReturn(List.of(topSeller));

        Seller result = transactionService.getMostProductiveSeller(start, end);

        assertNotNull(result);
        assertEquals("Top", result.getName());
    }

    @Test
    void testGetSellersWithSalesLessThan() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        BigDecimal limit = new BigDecimal("1000");
        Seller lowSeller = Seller.builder().id(2L).name("Low").build();

        when(transactionRepository.findSellersWithSumLessThan(start, end, limit))
                .thenReturn(List.of(lowSeller));

        List<Seller> result = transactionService.getSellersWithSalesLessThan(start, end, limit);

        assertFalse(result.isEmpty());
        assertEquals("Low", result.getFirst().getName());
    }

    @Test
    void testGetBestPeriodByCount() {
        Long sellerId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Transaction t1 = Transaction.builder().transactionDate(now).amount(new BigDecimal("100")).build();
        Transaction t2 = Transaction.builder().transactionDate(now.plusHours(2)).amount(new BigDecimal("200")).build();
        Transaction t3 = Transaction.builder().transactionDate(now.plusDays(5)).amount(new BigDecimal("300")).build();

        when(transactionRepository.findAllBySellerId(sellerId))
                .thenReturn(new java.util.ArrayList<>(List.of(t1, t2, t3)));

        Map<String, Object> result = transactionService.getBestPeriod(sellerId, 24);

        assertNotNull(result);
        assertEquals(2, result.get("transactionCount"));
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