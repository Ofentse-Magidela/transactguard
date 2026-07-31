package com.transactguard.transactguard.service;

import com.transactguard.transactguard.TransactionStatus;
import com.transactguard.transactguard.entity.Transaction;
import com.transactguard.transactguard.entity.User;
import com.transactguard.transactguard.exception.RequestException;
import com.transactguard.transactguard.repo.TransactionRepository;
import com.transactguard.transactguard.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private FraudService fraudService;
    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;
    private Transaction tx1;
    private Transaction tx2;


    @BeforeEach
    void setup(){

        sender = new User();
        sender.setId(1L);
        sender.setBalance(525.00);

        receiver = new User();
        receiver.setId(2L);
        receiver.setBalance(25.00);

        tx1  = new Transaction();
        tx1.setAmount(250.0);
        tx1.setId(1L);
        tx1.setReceiver(receiver);
        tx1.setSender(sender);

        tx2  = new Transaction();
        tx2.setAmount(100.0);
        tx2.setId(2L);
        tx2.setReceiver(sender);
        tx2.setSender(receiver);
    }

    @Nested
    @DisplayName("sendMoney")
    class sendMoney{

        @Test
        @DisplayName("sendMoney - Success")
        void sendMoney_ReturnsTransaction_whenMoneyWentThrough() {

            when(userRepo.findById(1L)).thenReturn(Optional.of(sender));
            when(userRepo.findById(2L)).thenReturn(Optional.of(receiver));
            when(transactRepo.save(any(Transaction.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Transaction transaction = transactionService.sendMoney(1L, 2L, 250.0);

            assertNotNull(transaction);
            assertEquals(1L, transaction.getSender().getId());
            assertEquals(2L, transaction.getReceiver().getId());
            assertEquals(250.0, transaction.getAmount());
            assertEquals(TransactionStatus.SUCCESSFUL, transaction.getStatus());

            verify(userRepo).findById(1L);
            verify(userRepo).findById(2L);
            verify(transactRepo).save(any(Transaction.class));
            verify(fraudService).checkFraud(any(Transaction.class));
        }

        @Test
        @DisplayName("sendMoney - Exception Sender equals Receiver")
        void sendMoney_ThrowsException_WhenSenderEqualsReceiver() {
            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> transactionService.sendMoney(1L, 1L, 250.0)
            );

            assertEquals("receiverID", ex.getField());
            assertEquals("Cannot send money to yourself", ex.getMessage());

            verifyNoInteractions(userRepo, transactRepo, fraudService);
        }

        @Test
        @DisplayName("sendMoney - Exception SenderID not found")
        void sendMoney_ThrowsException_whenSenderIsNotFound() {

            when(userRepo.findById(1L)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> transactionService.sendMoney(1L, 2L, 250.0)
            );

            assertEquals("Profile with ID 1 not found.", ex.getMessage());

            verify(userRepo).findById(1L);
            verify(transactRepo, never()).save(any());
        }

        @Test
        @DisplayName("sendMoney - Exception ReceiverID not found")
        void sendMoney_ThrowsException_whenReceiverIsNotFound() {

            when(userRepo.findById(1L)).thenReturn(Optional.of(sender));
            when(userRepo.findById(2L)).thenReturn(Optional.empty());

            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> transactionService.sendMoney(1L, 2L, 250.0)
            );

            assertEquals("receiverID", ex.getField());
            assertEquals(
                    "Recipient account not found. Please check the recipient ID and try again.",
                    ex.getMessage()
            );

            verify(userRepo).findById(2L);
            verify(transactRepo, never()).save(any());
        }

        @Test
        @DisplayName("sendMoney - Exception insufficient funds")
        void sendMoney_ThrowsException_WhenBalanceIsInsufficient() {

            when(userRepo.findById(1L)).thenReturn(Optional.of(sender));
            when(userRepo.findById(2L)).thenReturn(Optional.of(receiver));

            RequestException ex = assertThrows(
                    RequestException.class,
                    () -> transactionService.sendMoney(1L, 2L, 1000.0)
            );

            assertEquals("amount", ex.getField());
            assertEquals("Insufficient funds.", ex.getMessage());

            verify(userRepo, times(1)).findById(1L);
            verify(userRepo, times(1)).findById(2L);
            verify(transactRepo, times(1)).save(any(Transaction.class));
            verify(fraudService, times(1)).checkFraud(any(Transaction.class));

        }
    }

    @Nested
    @DisplayName("getTransaction")
    class getTransactionById{

        @Test
        @DisplayName("getTransaction - Success")
        void getTransaction_getTransaction_whenTransactionIdExist() {

            when(transactRepo.findById(1L)).thenReturn(Optional.of(tx1));

            Transaction transaction = transactionService.getTransactionById(1L);

            assertNotNull(transaction);
            assertEquals(250.0, transaction.getAmount());
            assertEquals(1L, transaction.getId());
            assertEquals(receiver, transaction.getReceiver());
            assertEquals(sender, transaction.getSender());

            verify(transactRepo).findById(1L);

        }

        @Test
        @DisplayName("getTransaction - Exception invalid transaction id")
        void getTransaction_ThrowsException_whenTransactionIdIsNotFound(){

            when(transactRepo.findById(100L)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> transactionService.getTransactionById(100L)
            );

            assertEquals("Transaction with ID 100 not found.", ex.getMessage());

            verify(transactRepo).findById(100L);

        }
    }

    @Nested
    @DisplayName("getSendTransactions")
    class getSendTransactionHistory{

        @Test
        @DisplayName("getSendTransactions")
        void getSendTransactions_ListOfSendTransactions_whenSenderIdIsFound() {
            when(transactRepo.findAllBySenderId(1L)).thenReturn(new ArrayList<>(List.of(tx1)));

            List<Transaction> sentHistory = transactionService.getSendTransactionHistory(1L);

            assertNotNull(sentHistory);
            assertEquals(sender, sentHistory.getFirst().getSender());
            assertEquals(receiver, sentHistory.getFirst().getReceiver());
            assertEquals(250.0, sentHistory.getFirst().getAmount());

            verify(transactRepo).findAllBySenderId(1L);

        }
    }

    @Nested
    @DisplayName("getReceivedTransactions")
    class getReceivedTransactionHistory{

        @Test
        @DisplayName("getReceivedTransactions")
        void getReceivedTransactions_ListOfReceivedTransactions_whenReceiverIdIsFound() {
            when(transactRepo.findAllByReceiverId(2L)).thenReturn(new ArrayList<>(List.of(tx1)));

            List<Transaction> receivedHistory = transactionService.getReceivedTransactionHistory(2L);

            assertNotNull(receivedHistory);
            assertEquals(sender, receivedHistory.getFirst().getSender());
            assertEquals(receiver, receivedHistory.getFirst().getReceiver());
            assertEquals(250.0, receivedHistory.getFirst().getAmount());

            verify(transactRepo).findAllByReceiverId(2L);

        }
    }

    @Nested
    @DisplayName("geTransactionsHistory")
    class getTransactionHistory {
        @Test
        @DisplayName("getTransactionHistory")
        void getTransactionHistory_TransactionHistoryMap_whenUserIdIsFound() {

            when(transactRepo.findAllBySenderId(1L)).thenReturn(new ArrayList<>(List.of(tx1)));
            when(transactRepo.findAllByReceiverId(1L)).thenReturn(new ArrayList<>(List.of(tx2)));

            Map<String, List<Transaction>> history = transactionService.getTransactionHistory(1L);

            assertNotNull(history);
            assertTrue(history.containsKey("received"));
            assertTrue(history.containsKey("sent"));
            assertEquals(1L, history.get("sent").getFirst().getId());
            assertEquals(2L, history.get("received").getFirst().getId());
            assertEquals(sender, history.get("sent").getFirst().getSender());
            assertEquals(sender, history.get("received").getFirst().getReceiver());

            verify(transactRepo).findAllByReceiverId(1L);
            verify(transactRepo).findAllBySenderId(1L);

        }
    }
}