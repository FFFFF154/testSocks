package ru.alekseev.testsocks.controller;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alekseev.testsocks.exceptions.FileProcessingException;
import ru.alekseev.testsocks.exceptions.InvalidFormatException;
import ru.alekseev.testsocks.exceptions.NegativeValueException;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileProcessingException(FileProcessingException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(NegativeValueException.class)
    public ResponseEntity<String> handleNegativeValueException(NegativeValueException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
    }
}
