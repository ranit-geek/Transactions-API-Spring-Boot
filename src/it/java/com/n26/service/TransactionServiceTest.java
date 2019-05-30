package com.n26.service;

import com.n26.CustomExeptions.InvalidParseException;
import com.n26.dao.TransactionCache;
import com.n26.entities.Statistics;
import com.n26.entities.Transactions;
import com.n26.helpers.BigDecimalSummaryStatistics;
import com.n26.helpers.DateTimeConverter;
import com.n26.services.StatisticsService;
import com.n26.services.TransactionService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.DoubleSummaryStatistics;

import javaslang.control.Option;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

//    @Mock
//    private DateTimeConverter dateTimeConverter;
//
//    @Mock
//    private StatisticsService statisticsService;
//    @Mock
//    private TransactionCache transactionCache;
//    @Mock
//    private Transactions transactions;
//
//
//    @InjectMocks
//    private
//    TransactionService transactionService;

//    @Test
//    public void shouldSaveAValidTransactionSuccessfully() throws Exception {
//
//        Transactions validTransaction = new Transactions("44.00", "2019-05-30T07:14:34.755Z");
//        when(transactions.isTxnTimeInLastSixtySeconds(1458251153L)).thenReturn(Option.of(validTransaction));
//        when(statisticsService.saveStats(validTransaction)).thenReturn(new Statistics());
//        Option<Statistics> maybeStatSummary = transactionService.saveTransaction(validTransaction,1458251153L);
//        assertTrue(maybeStatSummary.isDefined());
//    }
//
//    @Test
//    public void shouldReturnNoneMonadAndNotSaveTheTransactionIfTransactionIsOlder() throws Exception {
//
//        Transactions inValidTransaction = new Transactions("44.00", "2019-05-30T07:14:34.755Z");
//        verify(statisticsService, times(0)).saveStats(inValidTransaction);
//        Option<Statistics> maybeStatSummary = transactionService.saveTransaction(inValidTransaction,1559251153L);
//        assertTrue(maybeStatSummary.isDefined());
//    }
}
