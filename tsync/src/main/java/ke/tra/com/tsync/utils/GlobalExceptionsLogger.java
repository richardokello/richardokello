package ke.tra.com.tsync.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;

public class GlobalExceptionsLogger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GlobalExceptionsLogger.class);
    //@Before("execution(* ke.tra.com.tsync.repository.*.*(..))")
    @Before("execution(public * *(..))")
    public void before(JoinPoint joinPoint){
        //Advice
       // logger.error(" Test xxxxx ");
        logger.error("GlobalExceptionsLogger JoinPoint execution for {}", joinPoint);
    }

}
