package br.sergio.tardis_awards_questionary.exc;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@StandardException
public class InvalidAnswerException extends RuntimeException {
}
