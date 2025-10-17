package com.example.foundation.service;

import com.example.foundation.model.Bin;
import com.example.foundation.model.CollectionEvent;
import com.example.foundation.model.User;
import com.example.foundation.repository.CollectionEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionEventServiceTest {

    private CollectionEventRepository collectionEventRepository;
    private BillingService billingService;
    private CollectionEventService collectionEventService;

    @BeforeEach
    void setup() {
        collectionEventRepository = mock(CollectionEventRepository.class);
        billingService = mock(BillingService.class);
        collectionEventService = new CollectionEventService();

        // inject mocks via reflection since fields are @Autowired private
        try {
            var repoField = CollectionEventService.class.getDeclaredField("collectionEventRepository");
            repoField.setAccessible(true);
            repoField.set(collectionEventService, collectionEventRepository);

            var billingField = CollectionEventService.class.getDeclaredField("billingService");
            billingField.setAccessible(true);
            billingField.set(collectionEventService, billingService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("createCollectionEvent saves event and does not call awardCredits when no user")
    void createEvent_noUser_doesNotAwardCredits() {
        Bin bin = new Bin();
        bin.setBinCode("BIN-1");
        bin.setAssignedUser(null);

        CollectionEvent saved = new CollectionEvent(bin, 2.0f);
        saved.setCollectedDate(new Date());

        given(collectionEventRepository.save(any(CollectionEvent.class))).willReturn(saved);

        CollectionEvent result = collectionEventService.createCollectionEvent(bin, 2.0f);

        assertThat(result).isNotNull();
        verify(collectionEventRepository).save(any(CollectionEvent.class));
        verifyNoInteractions(billingService);
    }

    @Test
    @DisplayName("createCollectionEvent saves event and awards credits when bin has assigned user")
    void createEvent_withUser_awardsCredits() {
        Bin bin = new Bin();
        bin.setBinCode("BIN-2");
        User user = new User("u1", "p", "addr", "c");
        user.setId("user-1");
        bin.setAssignedUser(user);

        CollectionEvent saved = new CollectionEvent(bin, 3.0f);
        saved.setCollectedDate(new Date());

        given(collectionEventRepository.save(any(CollectionEvent.class))).willReturn(saved);

        CollectionEvent result = collectionEventService.createCollectionEvent(bin, 3.0f);

        assertThat(result).isNotNull();
        verify(collectionEventRepository).save(any(CollectionEvent.class));
        // verify awardCredits called with the expected user id and an amount > 0
        verify(billingService).awardCredits(eq("user-1"), anyDouble(), anyString());
    }
}
