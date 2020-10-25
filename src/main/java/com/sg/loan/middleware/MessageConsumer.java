package com.sg.loan.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.loan.commons.LoanException;
import com.sg.loan.model.LoanRequest;
import com.sg.loan.services.RiskOfficerLoanService;
import javafx.fxml.LoadException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumer {
    private final RiskOfficerLoanService customerService;
    private final ObjectMapper objectMapper;

    public MessageConsumer(RiskOfficerLoanService customerService, ObjectMapper objectMapper) {
        this.customerService = customerService;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "${CONSUMER_TOPIC_NAME}")
    public void consumeMessage(String message) throws JsonProcessingException, LoadException, LoanException {
        MDC.put("messageReceivedOnConsumerTopic", message);
        LoanRequest loanRequest = objectMapper.readValue(message, LoanRequest.class);
        customerService.saveCustomerData(loanRequest);
    }
}
