package com.nabokab.shift_crm.service;

import com.nabokab.shift_crm.exception.ResourceNotFoundException;
import com.nabokab.shift_crm.model.Seller;
import com.nabokab.shift_crm.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerService sellerService;

    @Test
    void testGetAllSellers() {
        when(sellerRepository.findAll()).thenReturn(List.of(new Seller(), new Seller()));
        List<Seller> result = sellerService.getAllSellers();
        assertEquals(2, result.size());
    }

    @Test
    void testGetSellerById_Success() {
        Seller seller = Seller.builder().id(1L).name("Ivanov").build();
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        Seller result = sellerService.getSellerById(1L);

        assertNotNull(result);
        assertEquals("Ivanov", result.getName());
    }

    @Test
    void testGetSellerById_NotFound() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> sellerService.getSellerById(1L));
    }

    @Test
    void testCreateSeller() {
        Seller seller = Seller.builder().name("New Seller").build();
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        Seller result = sellerService.createSeller(seller);

        assertNotNull(result);
        assertEquals("New Seller", result.getName());
        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    void testUpdateSeller() {
        Long sellerId = 1L;
        Seller existingSeller = Seller.builder().id(sellerId).name("Old Name").build();
        Seller details = Seller.builder().name("New Name").contactInfo("new@mail.ru").build();

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.of(existingSeller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(existingSeller);

        Seller result = sellerService.updateSeller(sellerId, details);

        assertEquals("New Name", result.getName());
        verify(sellerRepository).save(existingSeller);
    }

    @Test
    void testDeleteSeller() {
        Long sellerId = 1L;
        doNothing().when(sellerRepository).deleteById(sellerId);

        sellerService.deleteSeller(sellerId);

        verify(sellerRepository, times(1)).deleteById(sellerId);
    }
}