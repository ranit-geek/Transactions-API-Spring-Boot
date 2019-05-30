package com.n26.entities;

import com.n26.CustomExeptions.InvalidParseException;
import com.n26.CustomExeptions.OldTransactionException;
import com.n26.helpers.BigDecimalConverter;
import com.n26.helpers.DateTimeConverter;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import javaslang.control.Option;
import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Autowired
    private DateTimeConverter dateTimeConverter;

    private static final int MAX_TIME_DIFFERENCE = 60000;

    private String amount;
    private String timestamp;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



    public Option<Transactions> isTxnTimeInLastSixtySeconds(long currentMillis) throws InvalidParseException, OldTransactionException {
        long diff = (currentMillis - dateTimeConverter.convertToTimeStamp(timestamp));
        System.out.println(currentMillis +"   ----  "+ timestamp);
        if (diff<0) {throw new InvalidParseException("Transaction Time is in future");}

        if (diff>MAX_TIME_DIFFERENCE) {throw new OldTransactionException("Transaction ss older than 60 Seconds");}

        return (diff > 0 && diff <= MAX_TIME_DIFFERENCE ? Option.of(this) : Option.none());
    }


}
