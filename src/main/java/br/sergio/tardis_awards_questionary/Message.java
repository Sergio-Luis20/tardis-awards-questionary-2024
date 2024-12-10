package br.sergio.tardis_awards_questionary;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public record Message(int statusCode, String message) {

    public Message(HttpStatusCode statusCode, String message) {
        this(statusCode.value(), message);
    }

    public ResponseEntity<Message> responseEntity() {
        return new ResponseEntity<>(this, HttpStatusCode.valueOf(statusCode));
    }

}
