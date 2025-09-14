package com.example.healthcareappointmentsystem.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HealthcareLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(HealthcareLoggingAspect.class);

    @AfterReturning(
            pointcut = "@annotation(logOperation)",
            returning = "result"
    )

    public void logAppointmentBooking(JoinPoint joinPoint, LogOperation logOperation, Object result) {
        String operationType = logOperation.value();

        switch (operationType) {
            case "BOOK_APPOINTMENT":
                System.out.println("--APPOINTMENT BOOKED: " + result);
                break;

            case "CANCEL_APPOINTMENT":
                System.out.println("--APPOINTMENT CANCELLED: " + result);
                break;

            case "COMPLETE_APPOINTMENT":
                System.out.println("--APPOINTMENT COMPLETED: " + result);
                break;

            case "CREATE_PRESCRIPTION":
                System.out.println("--PRESCRIPTION CREATED: " + result);
                break;

            default:
                System.out.println("-- OPERATION: " + operationType + " - " + result);
        }
    }
}