package br.com.mundim.RestaurantReservationManagment.exceptions.config;

import br.com.mundim.RestaurantReservationManagment.exceptions.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;

import static br.com.mundim.RestaurantReservationManagment.exceptions.config.BaseErrorMessage.*;

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

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiException> handleSQLException(SQLException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        String message = "";
        if(e.toString().contains("cpf"))
            message = "CPF already in use";
        if(e.toString().contains("cnpj"))
            message = "CNPJ already in use";
        if(e.toString().contains("email"))
            message = "Email already in use";

        ApiException apiException = new ApiException(
                message,
                HttpStatus.BAD_REQUEST,
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime())
        );

        return new ResponseEntity<>(apiException, badRequest);
    }



}
