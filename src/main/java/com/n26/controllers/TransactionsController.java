package com.n26.controllers;

import com.n26.CustomExeptions.InvalidParseException;
import com.n26.CustomExeptions.OldTransactionException;
import com.n26.entities.Statistics;
import com.n26.entities.Transactions;
import com.n26.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

import javaslang.concurrent.Future;

import javaslang.control.Option;
import lombok.extern.slf4j.Slf4j;

@RestController("transactions")
@Slf4j
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;


    /**
     * @param transaction
     * @return saves the incoming transaction data
     */

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveTransaction(@RequestBody Transactions transaction) throws InvalidParseException, OldTransactionException {

        Option<Statistics> maybeStatsSummary = transactionService.saveTransaction(transaction,Instant.now().toEpochMilli());
        return maybeStatsSummary.map(summary -> new ResponseEntity(HttpStatus.CREATED))
                .getOrElse(new ResponseEntity(HttpStatus.NO_CONTENT));

    }

    /**
     * @return deletes all existing transactions
     */

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAllTxn() {
        transactionService.deleteTxnFromCache();
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }




}
