package com.sg.loan.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.loan.model.LoanRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageProducer {

    private final KafkaTemplate kafkaTemplate;
    private final String topicName;
    private final String disbursementTopicName;
    private final ObjectMapper objectMapper;

    public MessageProducer(KafkaTemplate kafkaTemplate, @Value("${PRODUCER_TOPIC_NAME}") String topicName,  @Value("${DISBURSEMENT_PRODUCER_TOPIC_NAME}")  String disbursementTopicName, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
        this.disbursementTopicName = disbursementTopicName;
        this.objectMapper = objectMapper;
    }

    public void publishMessage(LoanRequest loanRequest)  {
        try {
            String requestAsString = objectMapper.writeValueAsString(loanRequest);

            kafkaTemplate.send(topicName, requestAsString);
            MDC.put("messagePublishToResponseTopic", requestAsString);
            kafkaTemplate.send(disbursementTopicName, requestAsString);
            MDC.put("messagePublishToDisbursementTopic", requestAsString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}
