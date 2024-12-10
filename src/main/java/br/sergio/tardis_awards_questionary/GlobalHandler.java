package br.sergio.tardis_awards_questionary;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        ex.printStackTrace();
        return ResponseEntity
                .status(statusCode)
                .headers(headers)
                .body(new Message(statusCode.value(),
                        statusCode.is5xxServerError() ? "Internal Error" : ex.getMessage()));
    }

}
