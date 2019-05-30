package com.n26.helpers;

import com.n26.CustomExeptions.InvalidParseException;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Service
public class DateTimeConverter {

    public static final long MILLIS_FOR_ONE_SECOND = 1000;

    public static Long converToTimeStamp(String dateTime) throws InvalidParseException {
        Instant instant;
        try {
            instant = Instant.parse(dateTime);
            return instant.toEpochMilli();
        }catch(DateTimeParseException dtp){
            throw new InvalidParseException("Invalid date to parse");
        }
    }

    public static Instant converToTime(String dateTime) throws InvalidParseException {
        Instant instant;
        try {
            instant = Instant.parse(dateTime);
            return instant;
        }catch(DateTimeParseException dtp){
            throw new InvalidParseException("Invalid date to parse");
        }
    }

    public long currentMillis() {
        return Instant.now().toEpochMilli();

    }
    public long convertTimeInMillisToSeconds(long timeInMillis){ return timeInMillis/MILLIS_FOR_ONE_SECOND;}

    public long currentSeconds(){return convertTimeInMillisToSeconds(currentMillis());}
}
