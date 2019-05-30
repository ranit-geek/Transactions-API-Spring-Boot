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

@RestController("transactions")
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveTransaction(@RequestBody Transactions transactions) throws InvalidParseException, OldTransactionException {

        // save the incoming transaction
        Option<Statistics> maybeStatsSummary = transactionService.saveTransaction(transactions,Instant.now().toEpochMilli());

        // if transaction is saved successfully based on constraints then return 201 or else return 204
        return maybeStatsSummary.map(summary -> new ResponseEntity(HttpStatus.CREATED))
                .getOrElse(new ResponseEntity(HttpStatus.NO_CONTENT));

    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteAllTxn() {
        Boolean mDelete = transactionService.deleteTxnFromCache();

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }




}
