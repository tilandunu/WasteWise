package com.example.foundation.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.foundation.model.Bin;
import com.example.foundation.model.Resident;
import com.example.foundation.model.Tag;
import com.example.foundation.model.User;
import com.example.foundation.repository.BinRepository;
import com.example.foundation.repository.TagRepository;
import com.example.foundation.repository.UserRepository;

class OfficerServiceTest {
    @Mock
    private BinRepository binRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OfficerService officerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignTagToBin_Success() {
        Bin bin = mock(Bin.class);
        Tag tag = mock(Tag.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(tagRepository.findById("tag1")).thenReturn(Optional.of(tag));
        when(tag.isActive()).thenReturn(true);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(binRepository.save(bin)).thenReturn(bin);
        Bin result = officerService.assignTagToBin("bin1", "tag1");
        verify(bin).setTag(tag);
        verify(bin).setStatus("ASSIGNED");
        verify(tag).setActive(false);
        verify(tagRepository).save(tag);
        verify(binRepository).save(bin);
        assertEquals(bin, result);
    }

    @Test
    void testAssignTagToBin_BinNotFound() {
        when(binRepository.findById("bin1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> officerService.assignTagToBin("bin1", "tag1"));
        assertEquals("Bin not found", ex.getMessage());
    }

    @Test
    void testAssignTagToBin_TagNotFound() {
        Bin bin = mock(Bin.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(tagRepository.findById("tag1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> officerService.assignTagToBin("bin1", "tag1"));
        assertEquals("Tag not found", ex.getMessage());
    }

    @Test
    void testAssignTagToBin_TagAlreadyAssigned() {
        Bin bin = mock(Bin.class);
        Tag tag = mock(Tag.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(tagRepository.findById("tag1")).thenReturn(Optional.of(tag));
        when(tag.isActive()).thenReturn(false);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> officerService.assignTagToBin("bin1", "tag1"));
        assertEquals("Tag is already assigned", ex.getMessage());
    }

    @Test
    void testAssignBinToResident_SuccessResident() {
        Bin bin = mock(Bin.class);
        Resident resident = mock(Resident.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(userRepository.findById("res1")).thenReturn(Optional.of(resident));
        when(binRepository.save(bin)).thenReturn(bin);
        Bin result = officerService.assignBinToResident("bin1", "res1");
        verify(bin).setAssignedUser(resident);
        verify(resident).activateAccount();
        verify(userRepository).save(resident);
        verify(binRepository).save(bin);
        assertEquals(bin, result);
    }

    @Test
    void testAssignBinToResident_SuccessNonResident() {
        Bin bin = mock(Bin.class);
        User user = mock(User.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(binRepository.save(bin)).thenReturn(bin);
        Bin result = officerService.assignBinToResident("bin1", "user1");
        verify(bin).setAssignedUser(user);
        verify(binRepository).save(bin);
        // Should NOT call activateAccount or save on userRepository for non-Resident
        verify(userRepository, never()).save(user);
        assertEquals(bin, result);
    }

    @Test
    void testAssignBinToResident_BinNotFound() {
        when(binRepository.findById("bin1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> officerService.assignBinToResident("bin1", "res1"));
        assertEquals("Bin not found", ex.getMessage());
    }

    @Test
    void testAssignBinToResident_ResidentNotFound() {
        Bin bin = mock(Bin.class);
        when(binRepository.findById("bin1")).thenReturn(Optional.of(bin));
        when(userRepository.findById("res1")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> officerService.assignBinToResident("bin1", "res1"));
        assertEquals("Resident not found", ex.getMessage());
    }
}
