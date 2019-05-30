package com.n26.services;


import com.n26.CustomExeptions.InvalidParseException;
import com.n26.CustomExeptions.OldTransactionException;
import com.n26.dao.TransactionCache;
import com.n26.entities.Statistics;
import com.n26.entities.Transactions;
import com.n26.helpers.BigDecimalConverter;
import com.n26.helpers.DateTimeConverter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javaslang.concurrent.Future;
import javaslang.control.Option;


@Service
@Slf4j
public class TransactionService {

    @Autowired
    private DateTimeConverter dateTimeConverter;
    @Autowired
    private  StatisticsService statisticsService;
    @Autowired
    private TransactionCache transactionCache;
    @Autowired
    private BigDecimalConverter bigDecimalConverter;

    /**
     * @return Asynchronously sends the transaction to the StatisticalService layer to save it and
     * add it in the buffer/cache.transaction is not older than 60 seconds.If its older than 60
     * seconds it rejects it.
     */
    public Option<Statistics> saveTransaction(Transactions transactions,Long currentTime) throws InvalidParseException, OldTransactionException {

        bigDecimalConverter.covertStringToBigDecimal(transactions.getAmount());
        return transactions.isTxnTimeInLastSixtySeconds(currentTime)
                .flatMap(txn ->Future.of(() -> statisticsService.saveStats(txn)).toOption())
                .orElse(() -> Option.none());
    }

    public Boolean deleteTxnFromCache()
    {
        log.info("Clearing transactions cache and statistics summery");
        statisticsService.clearStatsSummery();
        return transactionCache.clearCache();
    }

}
