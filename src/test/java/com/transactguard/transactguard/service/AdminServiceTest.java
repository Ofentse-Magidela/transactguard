package com.transactguard.transactguard.service;

import com.transactguard.transactguard.entity.FraudFlag;
import com.transactguard.transactguard.repo.FraudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private FraudRepository repository;

    @InjectMocks
    private AdminService  adminService;

    private FraudFlag flag;
    @BeforeEach
    void setup() {
        flag = new FraudFlag();
        flag.setId(1L);
        flag.setResolved(false);
        flag.setReason("Rapid Transactions");
    }

    @Nested
    @DisplayName("getAllFlags")
    class getFlaggedTransactions{

        @Test
        @DisplayName("getAllFlags - Success")
        void getAllFlags_ReturnListOfFlaggedTransactions_whenTheyExist() {

            when(repository.findAllByResolved(false)).thenReturn(List.of(flag));

            List<FraudFlag> flags = adminService.getAllFlags();

            assertEquals(1L, flags.getFirst().getId());
            assertEquals("Rapid Transactions", flags.getFirst().getReason());
            assertFalse(flags.getFirst().isResolved());

            verify(repository, times(1)).findAllByResolved(false);
        }
    }

    @Nested
    @DisplayName("resolveFlags")
    class resolveFlag{

        @Test
        @DisplayName("resolveFlags - Success")
        void resolveFlags_SetResolvedToTrue_whenFlagged() {

            when(repository.findById(1L)).thenReturn(Optional.of(flag));

            adminService.resolveFlag(1L);

            ArgumentCaptor<FraudFlag> useCaptor = ArgumentCaptor.forClass(FraudFlag.class);
            verify(repository).save(useCaptor.capture());
            FraudFlag flagged = useCaptor.getValue();

            assertNotNull(flagged);
            assertEquals(1L, flagged.getId());
            assertEquals("Rapid Transactions", flagged.getReason());
            assertTrue(flagged.isResolved());

            verify(repository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("resolveFlags - Exception flag id not found")
        void resolveFlags_ThrowsException_whenFlagIsNotFound() {

            when(repository.findById(100L)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> adminService.resolveFlag(100L)
            );

            assertEquals("FraudFlag with ID 100 not found.", ex.getMessage());

            verify(repository, times(1)).findById(100L);
        }
    }
}