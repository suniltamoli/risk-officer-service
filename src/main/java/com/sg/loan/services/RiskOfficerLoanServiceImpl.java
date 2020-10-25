package com.sg.loan.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sg.loan.commons.LoanException;
import com.sg.loan.entity.LoanRequestEntity;
import com.sg.loan.commons.Validator;
import com.sg.loan.middleware.MessageProducer;
import com.sg.loan.model.LoanRequest;
import com.sg.loan.repository.CustomerLoanRepository;
import com.sg.loan.transformer.LoanRequestTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RiskOfficerLoanServiceImpl implements RiskOfficerLoanService {
    private CustomerLoanRepository customerRepository;
    private LoanRequestTransformer transformer;
    private Validator validator;
    private final MessageProducer messageProducer;
    private final ObjectMapper objectMapper;

    public RiskOfficerLoanServiceImpl(CustomerLoanRepository customerRepository, LoanRequestTransformer transformer, Validator validator, MessageProducer messageProducer, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.transformer = transformer;
        this.validator = validator;
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
    }

    @Override
    public LoanRequest saveCustomerData(LoanRequest loanRequest) throws LoanException, JsonProcessingException {
        try {
            String loanReqAsStr = objectMapper.writeValueAsString(loanRequest);
            validator.validateRequest(loanRequest);
            //may be call some third party api first
            String approvedBy = "risk-officer";
            loanRequest.setApprovedByRiskOfficer(approvedBy);
            loanRequest.setLoanStatus("Approved");
            LoanRequestEntity loanRequestEntity = transformer.transformCustomerDetails(loanRequest);
            loanRequestEntity = customerRepository.save(loanRequestEntity);
            BeanUtils.copyProperties(loanRequestEntity, loanRequest);
            messageProducer.publishMessage(loanRequest);
        } catch (Exception e) {
            if(e instanceof DataIntegrityViolationException) {
                log.error(e.getMessage());
                throw new LoanException(e.getMessage(), "200001");
            } else {
                log.error(e.getMessage());
                throw e;
            }
        }
        return loanRequest;

    }

    @Override
    public LoanRequest getLoanDetails(String loanRefNumber) throws LoanException {
        List<LoanRequestEntity> loanRequestEntities =  customerRepository.getCustomerByloanRefNumberr(loanRefNumber);
        LoanRequestEntity loanRequestEntity = loanRequestEntities.get(0);
        LoanRequest loanRequest = new LoanRequest();
        BeanUtils.copyProperties(loanRequestEntity, loanRequest);
        return loanRequest;
    }

}
