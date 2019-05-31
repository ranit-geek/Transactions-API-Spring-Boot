package unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.controllers.TransactionsController;
import com.n26.entities.Statistics;
import com.n26.entities.Transactions;
import com.n26.helpers.DateTimeConverter;
import com.n26.services.TransactionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javaslang.control.Option;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    DateTimeConverter dateTimeConverter;

    @InjectMocks
    private TransactionsController transactionController;

    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    /**
     * @throws Exception
     */
    @Test
    public void positiveTransactionTest() throws Exception {

        Transactions validTransaction = new Transactions("44.00", "2019-05-30T07:14:34.755Z");

        when(dateTimeConverter.currentMillis())
                .thenReturn(1559200443L);

        when(transactionService.saveTransaction(validTransaction,
                1559200443L))
                .thenReturn(Option.of(new Statistics()));

        mockMvc.perform(
                post("/transactions")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validTransaction))).andExpect(status().isCreated());
    }

    /**
     * @throws Exception
     */
    @Test
    public void olderTransactionTest() throws Exception {

        Transactions validTransaction = new Transactions("44.00", "2019-05-30T07:14:34.755Z");

        when(dateTimeConverter.currentMillis())
                .thenReturn(1559200443L);

        when(transactionService.saveTransaction(validTransaction,
                1559200443L))
                .thenReturn(Option.none());

        mockMvc.perform(
                post("/transactions")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validTransaction))).andExpect(status().isNoContent());
    }

    @Test
    public void deleteAlltransactionTest() throws Exception {


        when(transactionService.deleteTxnFromCache())
                .thenReturn(true);

        mockMvc.perform(
                delete("/transactions")).andExpect(status().isNoContent());
    }
}
