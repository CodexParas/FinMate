package com.paras.FinMate.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String message;
    private Object data;
    private String error;
    private Status status;

    public static Response success (String message, Object data) {
        return Response.builder ()
                       .message (message)
                       .data (data)
                       .status (Status.SUCCESS)
                       .build ();
    }

    public static Response error (String message, String error) {
        return Response.builder ()
                       .message (message)
                       .error (error)
                       .status (Status.ERROR)
                       .build ();
    }

    private enum Status {
        SUCCESS, ERROR
    }

}
