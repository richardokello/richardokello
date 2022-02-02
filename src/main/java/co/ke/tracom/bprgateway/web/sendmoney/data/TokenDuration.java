package co.ke.tracom.bprgateway.web.sendmoney.data;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public enum TokenDuration {
    MILLISECONDS,
    SECONDS,
    MINUTES,
    HOURS,
    DAYS;


    public static Duration getDuration(TokenDuration duration, int length) {
        if (duration == MILLISECONDS){
            return Duration.ofMillis(length);
        }
        if (duration == SECONDS){
            return Duration.ofSeconds(length);
        }
        if (duration == MINUTES){
            return Duration.ofMinutes(length);
        }
        if (duration == DAYS){
            return Duration.ofDays(length);
        }
        return Duration.ofHours(length);
    }



}