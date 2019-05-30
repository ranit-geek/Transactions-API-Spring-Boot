package com.n26.helpers;

import com.n26.CustomExeptions.InvalidParseException;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class BigDecimalConverter {

    public static BigDecimal covertStringToBigDecimal(String transactionAmount) throws InvalidParseException {
        try
        {

            BigDecimal amountValDecimal = new BigDecimal(transactionAmount)
                    .setScale(2,BigDecimal.ROUND_HALF_UP);
            return amountValDecimal;

        }
        catch(NumberFormatException e) {
            throw new InvalidParseException("Not been able ro parse string to BigDecimal");
        }

    }
}
