package com.nabokab.shift_crm.repository;

import com.nabokab.shift_crm.model.Seller;
import com.nabokab.shift_crm.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySellerId(Long sellerId);

    @Query("SELECT t.seller FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :start AND :end " +
            "GROUP BY t.seller " +
            "ORDER BY SUM(t.amount) DESC")
    List<Seller> findMostProductiveSellers(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query("SELECT t.seller FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :start AND :end " +
            "GROUP BY t.seller " +
            "HAVING SUM(t.amount) < :minAmount")
    List<Seller> findSellersWithSumLessThan(@Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end,
                                            @Param("minAmount") java.math.BigDecimal minAmount);

}