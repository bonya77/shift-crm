package com.nabokab.shift_crm.repository;

import com.nabokab.shift_crm.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> { }