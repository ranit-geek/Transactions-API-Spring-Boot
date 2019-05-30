package com.n26.services;


import com.n26.CustomExeptions.InvalidParseException;
import com.n26.CustomExeptions.OldTransactionException;
import com.n26.dao.TransactionCache;
import com.n26.entities.Statistics;
import com.n26.entities.Transactions;
import com.n26.helpers.BigDecimalConverter;
import com.n26.helpers.DateTimeConverter;
import com.sun.org.apache.xpath.internal.operations.Bool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

import javaslang.concurrent.Future;
import javaslang.control.Option;

@Service
public class TransactionService {

    @Autowired
    private DateTimeConverter dateTimeConverter;
    @Autowired
    private  StatisticsService statisticsService;
    @Autowired
    private TransactionCache transactionCache;
    @Autowired
    private BigDecimalConverter bigDecimalConverter;

    public Option<Statistics> saveTransaction(Transactions transactions,Long currentTime) throws InvalidParseException, OldTransactionException {

        // check whether the transaction is from last sixty seconds which returns a Option<T>
        // if Option<T> is a success, asynchronously save the stats data using Future abstraction by passing it to stats service layer
        // if Option<T> is a None, signifies it is a older transaction, so return Option.none


        bigDecimalConverter.covertStringToBigDecimal(transactions.getAmount());  //@TODO check
        return transactions.isTxnTimeInLastSixtySeconds(currentTime)
                .flatMap(txn ->Future.of(() -> statisticsService.saveStats(txn)).toOption())
                .orElse(() -> Option.none());
    }

    public Boolean deleteTxnFromCache()
    {
        statisticsService.clearStatsSummery();
        return transactionCache.clearCache();
    }

}
