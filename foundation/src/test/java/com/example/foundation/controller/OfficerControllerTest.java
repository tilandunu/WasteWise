package com.example.foundation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.foundation.model.Bin;
import com.example.foundation.service.OfficerService;

class OfficerControllerTest {
    @Mock
    private OfficerService officerService;
    @InjectMocks
    private OfficerController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignTagToBin() {
        Bin bin = mock(Bin.class);
        when(officerService.assignTagToBin("bin1", "tag1")).thenReturn(bin);
        ResponseEntity<Bin> response = controller.assignTagToBin("bin1", "tag1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bin, response.getBody());
    }

    @Test
    void testAssignBinToResident() {
        Bin bin = mock(Bin.class);
        when(officerService.assignBinToResident("bin1", "resident1")).thenReturn(bin);
        ResponseEntity<Bin> response = controller.assignBinToResident("bin1", "resident1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bin, response.getBody());
    }
}
