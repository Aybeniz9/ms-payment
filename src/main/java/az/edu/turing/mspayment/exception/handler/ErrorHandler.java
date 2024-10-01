package az.edu.turing.mspayment.exception.handler;

import az.edu.turing.mspayment.exception.ExceptionResponse;
import az.edu.turing.mspayment.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

import static az.edu.turing.mspayment.model.constants.ExceptionConstants.UNEXPECTED_EXCEPTION_CODE;
import static az.edu.turing.mspayment.model.constants.ExceptionConstants.UNEXPECTED_EXCEPTION_MESSAGE;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private final View error;

    public ErrorHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleInternalServerError(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ExceptionResponse( UNEXPECTED_EXCEPTION_CODE,UNEXPECTED_EXCEPTION_MESSAGE);

    }
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFound(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return new ExceptionResponse(ex.getCode(), ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleValidation(MethodArgumentNotValidException exception){
        Map<String,String> errors= new HashMap<>();
exception.getBindingResult().getAllErrors().forEach(error->{
    String fieldName=((FieldError)error).getField();
    String errorMessage=error.getDefaultMessage();
    errors.put(fieldName,errorMessage);});
    return errors;
    }
}
