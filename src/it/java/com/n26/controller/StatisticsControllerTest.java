package unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.Application;
import com.n26.controllers.StatisticsController;
import com.n26.entities.Statistics;
import com.n26.services.StatisticsService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class StatisticsControllerTest {
    @InjectMocks
    private StatisticsController statsController;

    @Mock
    private
    StatisticsService statsService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(statsController).build();
    }

    @Test
    public void aggrigateSatisticsSummery() throws Exception {

        Statistics statsSummary = new Statistics(new BigDecimal(124.01).setScale(2,BigDecimal.ROUND_HALF_UP),
                new BigDecimal(22.14).setScale(2,BigDecimal.ROUND_HALF_UP),
                new BigDecimal(34.11).setScale(2,BigDecimal.ROUND_HALF_UP),
                new BigDecimal(67.22).setScale(2,BigDecimal.ROUND_HALF_UP),9l);
        when(statsService.getSummary()).thenReturn(statsSummary);

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statsSummary)));
    }

    @Test
    public void shouldReturnDefaultStatsSummaryIfThereAreNoTransactionsInCache() throws Exception {

        Statistics statsSummary = new Statistics(new BigDecimal(00.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                new BigDecimal(00.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                new BigDecimal(00.00).setScale(2,BigDecimal.ROUND_HALF_UP),
                new BigDecimal(00.00).setScale(2,BigDecimal.ROUND_HALF_UP),0l);
        when(statsService.getSummary()).thenReturn(statsSummary);

        mockMvc.perform(get("/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statsSummary)));
    }


}
