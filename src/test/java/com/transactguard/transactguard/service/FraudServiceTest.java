package com.transactguard.transactguard.service;

import com.transactguard.transactguard.entity.FraudFlag;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.repo.FraudRepository;
import com.transactguard.transactguard.repo.TransactionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FraudServiceTest {

    @Mock
    private FraudRepository fraudRepo;
    @Mock
    private TransactionRepository transactionRepo;
    @InjectMocks
    FraudService fraudService;

    private User sender;
    @BeforeEach
    void setup() {

        sender = new User();
        sender.setBalance(20000.0);
        sender.setId(1L);
    }

    @Nested
    @DisplayName("checkFraud")
    class checkFraud {

        @Test
        @DisplayName("checkFraud - Success Triggers All Fraud Flags Simultaneously")
        void checkFraud_SavesMultipleFlags_WhenAllConditionsAreMet() {

            sender.setBalance(500.0);

            Transaction tx = new Transaction();
            tx.setAmount(15000.0);
            tx.setSender(sender);
            tx.setTimestamp(LocalDateTime.now());

            when(transactionRepo.findAllBySenderIdAndTimestampAfter(anyLong(), any(LocalDateTime.class)))
                    .thenReturn(List.of(new Transaction(), new Transaction(), new Transaction()));

            fraudService.checkFraud(tx);

            ArgumentCaptor<FraudFlag> captor = ArgumentCaptor.forClass(FraudFlag.class);
            verify(fraudRepo, times(3)).save(captor.capture());

            List<FraudFlag> flaggedItems = captor.getAllValues();

            assertNotNull(flaggedItems);
            assertEquals(3, flaggedItems.size());
            assertEquals("Amount exceeds limit", flaggedItems.get(0).getReason());
            assertEquals("High Balance Drain", flaggedItems.get(1).getReason());
            assertEquals("Rapid Transactions", flaggedItems.get(2).getReason());
        }

        @Test
        @DisplayName("checkFraud - Success Amount Above R10 000.00")
        void checkFraud_SaveTheFlaggedTransaction_whenAmountExceedTenThousand() {

            Transaction tx1 = new Transaction();
            tx1.setAmount(10001.0);
            tx1.setSender(sender);
            tx1.setTimestamp(LocalDateTime.now());

            when(transactionRepo
                    .findAllBySenderIdAndTimestampAfter(anyLong(), any(LocalDateTime.class))
            ).thenReturn(List.of(tx1));

            fraudService.checkFraud(tx1);

            ArgumentCaptor<FraudFlag> useCaptor = ArgumentCaptor.forClass(FraudFlag.class);
            verify(fraudRepo, times(1)).save(useCaptor.capture());
            FraudFlag flagged = useCaptor.getValue();

            assertNotNull(flagged);
            assertNotNull(flagged.getTransaction());
            assertEquals(10001.0, flagged.getTransaction().getAmount());
            assertEquals("Amount exceeds limit", flagged.getReason());
            assertFalse(flagged.isResolved());


            verify(transactionRepo, times(1))
                    .findAllBySenderIdAndTimestampAfter(eq(sender.getId()), any(LocalDateTime.class));
        }

        @Test
        @DisplayName("checkFraud - Success Rapid Transactions")
        void checkFraud_SaveTheFlaggedTransaction_thereIsMoreThan3TransactionsUnderAMinute() {
            Transaction tx1 = new Transaction();
            tx1.setAmount(1000.0);
            tx1.setSender(sender);
            tx1.setTimestamp(LocalDateTime.now());

            when(transactionRepo
                    .findAllBySenderIdAndTimestampAfter(anyLong(), any(LocalDateTime.class))
            ).thenReturn(List.of(new Transaction(), new Transaction(), tx1));

            fraudService.checkFraud(tx1);

            ArgumentCaptor<FraudFlag> useCaptor = ArgumentCaptor.forClass(FraudFlag.class);
            verify(fraudRepo, times(1)).save(useCaptor.capture());

            FraudFlag flagged = useCaptor.getValue();

            assertNotNull(flagged);
            assertNotNull(flagged.getTransaction());
            assertEquals(1000.0, flagged.getTransaction().getAmount());
            assertEquals("Rapid Transactions", flagged.getReason());
            assertFalse(flagged.isResolved());

            verify(transactionRepo, times(1))
                    .findAllBySenderIdAndTimestampAfter(anyLong(), any(LocalDateTime.class));
        }

        @Test
        @DisplayName("checkFraud - Success High Balance Drain")
        void checkFraud_SaveTheFlaggedTransaction_whenBigTransactionMadeInLowBalance() {
            sender.setBalance(1000.0);

            Transaction tx1 = new Transaction();
            tx1.setAmount(800.0);
            tx1.setSender(sender);
            tx1.setTimestamp(LocalDateTime.now());

            when(transactionRepo
                    .findAllBySenderIdAndTimestampAfter(anyLong(), any(LocalDateTime.class))
            ).thenReturn(List.of(tx1));

            fraudService.checkFraud(tx1);

            ArgumentCaptor<FraudFlag> useCaptor = ArgumentCaptor.forClass(FraudFlag.class);
            verify(fraudRepo, times(1)).save(useCaptor.capture());

            FraudFlag flagged = useCaptor.getValue();

            assertNotNull(flagged);
            assertNotNull(flagged.getTransaction());
            assertEquals(800.0, flagged.getTransaction().getAmount());
            assertEquals("High Balance Drain", flagged.getReason());
            assertFalse(flagged.isResolved());

            verify(transactionRepo, times(1))
                    .findAllBySenderIdAndTimestampAfter(anyLong(), any(LocalDateTime.class));
        }
    }
}