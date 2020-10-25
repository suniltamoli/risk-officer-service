package com.sg.loan.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.loan.TestHelper;
import com.sg.loan.commons.LoanException;
import com.sg.loan.commons.Validator;
import com.sg.loan.entity.LoanRequestEntity;
import com.sg.loan.middleware.MessageProducer;
import com.sg.loan.model.LoanRequest;
import com.sg.loan.repository.CustomerLoanRepository;
import com.sg.loan.transformer.LoanRequestTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;

@RunWith(MockitoJUnitRunner.class)
public class RiskOfficerLoanServiceImplTest extends TestHelper {

    @Mock
    private CustomerLoanRepository customerRepository;
    @Mock
    private LoanRequestTransformer loanRequestTransformer;
    private Validator validator;
    private  MessageProducer messageProducer;
    private  ObjectMapper objectMapper;
    @Mock
    private  KafkaTemplate kafkaTemplate;
    private  String topicName = "topic";
    private  String disbursementTopicName = "topic";
    private RiskOfficerLoanService riskOfficerLoanService;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
        validator = new Validator();
        messageProducer = new MessageProducer(kafkaTemplate, topicName, disbursementTopicName, objectMapper);
        riskOfficerLoanService = new RiskOfficerLoanServiceImpl(customerRepository, loanRequestTransformer, validator, messageProducer, objectMapper);
    }

    @Test
    public void saveCustomerDataTest() throws IOException, URISyntaxException, LoanException {
        String request = getPayloadAsString("/request.json");
        LoanRequest loanRequest = objectMapper.readValue(request, LoanRequest.class);
        LoanRequestEntity loanRequestEntity = new LoanRequestEntity();
        BeanUtils.copyProperties(loanRequest, loanRequestEntity);
        Timestamp approvedOnLoanOfficer = new Timestamp(System.currentTimeMillis());
        loanRequestEntity.setApprovedOnRiskOfficer(approvedOnLoanOfficer);
        loanRequestEntity.setApprovedByFrontOfficer(loanRequest.getApprovedByFrontOfficer());
        loanRequestEntity.setApprovedOnFrontOfficer(loanRequest.getApprovedOnFrontOfficer());
        Mockito.when(loanRequestTransformer.transformCustomerDetails(loanRequest)).thenReturn(loanRequestEntity);
        Mockito.when(customerRepository.save(loanRequestEntity)).thenReturn(loanRequestEntity);
        riskOfficerLoanService.saveCustomerData(loanRequest);
    }
}
