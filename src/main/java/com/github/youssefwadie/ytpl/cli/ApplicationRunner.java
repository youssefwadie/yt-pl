package com.github.youssefwadie.ytpl.cli;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import static picocli.CommandLine.IFactory;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner, ExitCodeGenerator {

    private final IFactory factory;
    private final YtPlCommand ytPlCommand;

    private int exitCode;

    @Override
    public void run(String... args) throws Exception {
        CommandLine commandLine = new CommandLine(ytPlCommand, factory);
        commandLine.setExecutionExceptionHandler(new ExecutionExceptionHandler());

        exitCode = commandLine.execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
