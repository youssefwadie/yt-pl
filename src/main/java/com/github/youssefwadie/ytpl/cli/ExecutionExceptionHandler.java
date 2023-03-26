package com.github.youssefwadie.ytpl.cli;

import lombok.val;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;

import java.io.PrintWriter;
import java.util.Map;

public class ExecutionExceptionHandler implements IExecutionExceptionHandler {
    @Override
    public int handleExecutionException(Exception ex, CommandLine cmd, CommandLine.ParseResult parseResult) {
        PrintWriter err = cmd.getErr();
        if ("DEBUG".equalsIgnoreCase(System.getProperty("picocli.trace"))) {
            err.println(cmd.getColorScheme().stackTraceText(ex));
        }
        err.println(cmd.getColorScheme().errorText(errorMessage(ex))); // bold red
        return 1;
    }

    // a bit aggressive but to be safe
    private String errorMessage(Exception ex) {
        String message = ex.getMessage();
        try {
            if (ex instanceof HttpClientErrorException.Forbidden forbidden) {
                val errorBody = forbidden.getResponseBodyAs(new ParameterizedTypeReference<Map<String, Map<String, Object>>>() {});
                if (errorBody == null) return message;
                message = String.valueOf(errorBody.get("error").get("message"));
            }
        } catch (Exception ignored) { }

        return message;
    }
}

