package com.bht.assetmanagement.shared.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
@Setter
public class ErrorMessage {
    private String message;
    private HttpStatus status;
}
