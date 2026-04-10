package com.nabokab.shift_crm.service;

import com.nabokab.shift_crm.exception.ResourceNotFoundException;
import com.nabokab.shift_crm.model.Seller;
import com.nabokab.shift_crm.repository.SellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        // Проверяем, что выбрасывается наше кастомное исключение
        assertThrows(ResourceNotFoundException.class, () -> sellerService.getSellerById(1L));
    }
}