package com.sg.loan.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sg.loan.commons.LoanException;
import com.sg.loan.model.LoanRequest;

public interface RiskOfficerLoanService {
    LoanRequest saveCustomerData(LoanRequest loanRequest ) throws LoanException, JsonProcessingException;
    LoanRequest getLoanDetails(String loanRefNumber) throws LoanException;
}
