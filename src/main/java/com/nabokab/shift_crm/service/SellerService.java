package com.nabokab.shift_crm.service;

import com.nabokab.shift_crm.exception.ResourceNotFoundException;
import com.nabokab.shift_crm.model.Seller;
import com.nabokab.shift_crm.model.Transaction;
import com.nabokab.shift_crm.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public List<Seller> getAllSellers(){
        return sellerRepository.findAll();
    }

    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with id" + id + " not found"));
    }

    public Seller createSeller(Seller seller){
        return sellerRepository.save(seller);
    }

    public Seller updateSeller(Long id, Seller sellerDetails){
        Seller seller = getSellerById(id);
        seller.setName(sellerDetails.getName());
        seller.setContactInfo(sellerDetails.getContactInfo());
        return sellerRepository.save(seller);
    }

    public void deleteSeller(Long id){
        sellerRepository.deleteById(id);
    }


}
