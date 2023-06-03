package com.vincentcrop.vshop.APIGateway.Http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.vincentcrop.vshop.APIGateway.util.Http.HttpExceptionResponse;

public class HttpResponseTest {
    
    @Test
    void extractMessageTest() {
        var message = "401 UNAUTHORIZED \"[401] during [GET] to [http://AUTHENTICATOR-SERVICE/users] [AuthenticatorClient#getLoginUser(String)]: [{\"status\":{\"value\":401,\"informational\":false,\"successful\":false,\"redirection\":false,\"clientError\":true,\"serverError\":false,\"error\":true},\"message\":\"401 UNAUTHORIZED \\\"JWT token is invalid or expired\\\"\"}]\"";
        message = HttpExceptionResponse.extractMessage(message);

        assertEquals("JWT token is invalid or expired", message);
    }
}
