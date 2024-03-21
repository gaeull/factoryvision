package webproject.factoryvision.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class SecurityExceptionDto {
    private int statusCode;
    private String msg;
}
