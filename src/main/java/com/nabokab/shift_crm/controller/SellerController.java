package com.nabokab.shift_crm.controller;

import com.nabokab.shift_crm.model.Seller;
import com.nabokab.shift_crm.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @GetMapping
    public List<Seller> getAllSellers(){
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}")
    public Seller getSellerById(@PathVariable Long id){
        return sellerService.getSellerById(id);
    }

    @PostMapping
    public Seller createSeller(@RequestBody Seller seller){
        return sellerService.createSeller(seller);
    }

    @DeleteMapping("/{id}")
    public void deleteSeller(@PathVariable Long id){
        sellerService.deleteSeller(id);
    }
}
