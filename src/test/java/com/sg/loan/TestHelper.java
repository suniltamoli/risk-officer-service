package com.sg.loan;

import com.sg.loan.model.LoanRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestHelper {

    public String getPayloadAsString(String s) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(LoanRequest.class.getResource(s).toURI())));
    }

    public String getErrorPayloadAsString(String s) throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(Error.class.getResource(s).toURI())));
    }
}
