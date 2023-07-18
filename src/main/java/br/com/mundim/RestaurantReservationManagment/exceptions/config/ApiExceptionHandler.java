package br.com.mundim.RestaurantReservationManagment.exceptions.config;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.INCORRECT_OPERATING_HOUR_FORMAT;
import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.INCORRECT_WEEK_DAY;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                e.getMessage(),
                responseStatus,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, responseStatus);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        String message = "Unknow HttpMessageNotReadableException.";
        if(e.getMessage().contains("WeekDay"))
            message = INCORRECT_WEEK_DAY.getMessage();

        ApiException apiException = new ApiException(
                message,
                responseStatus,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, responseStatus);
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException e) {
        HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        String message = "Unknow DateTimeParseException.";
        if(Arrays.toString(e.getStackTrace()).contains("OperatingHours")){
            message = INCORRECT_OPERATING_HOUR_FORMAT.getMessage();
        }

        ApiException apiException = new ApiException(
                message,
                responseStatus,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, responseStatus);
    }

}
