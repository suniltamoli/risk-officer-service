package com.sg.loan.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.loan.TestHelper;
import com.sg.loan.entity.LoanRequestEntity;
import com.sg.loan.model.LoanRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;

@RunWith(MockitoJUnitRunner.class)
public class LoanRequestTransformerTest extends TestHelper {
    private ObjectMapper objectMapper;
    private LoanRequestTransformer loanRequestTransformer;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
        loanRequestTransformer = new LoanRequestTransformer();

    }

    @Test
    public void transformLoanReqTest() throws IOException, URISyntaxException {
        String request = getPayloadAsString("/request.json");
        LoanRequest loanRequest = objectMapper.readValue(request, LoanRequest.class);
        LoanRequestEntity loanRequestEntity = loanRequestTransformer.transformCustomerDetails(loanRequest);
        Assert.assertNotNull(loanRequestEntity);
    }
}
