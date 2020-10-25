package com.sg.loan.controller;

import com.sg.loan.commons.LoanException;
import com.sg.loan.commons.LoanError;
import com.sg.loan.model.LoanRequest;
import com.sg.loan.services.RiskOfficerLoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1")
public class RiskOfficerController {

    private final RiskOfficerLoanService customerService;

    public RiskOfficerController(RiskOfficerLoanService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/risk/verify")
    public ResponseEntity<?> createCustomerKYC(@Valid @RequestBody LoanRequest loanRequest) {
        try {

            customerService.saveCustomerData(loanRequest);
        } catch (Exception e) {
            if(e instanceof LoanException) {
                LoanError loanError = new LoanError(((LoanException) e).getErroCode(), ((LoanException) e).getMessage());
                return new ResponseEntity<LoanError>(loanError, HttpStatus.BAD_REQUEST);
            } else {
                LoanError loanError = new LoanError(((LoanException) e).getErroCode(),  e.getMessage());
                return new ResponseEntity<LoanError>(loanError, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<LoanRequest>(loanRequest, HttpStatus.ACCEPTED);
    }

    @PutMapping("/carloan/fron-officer/approval/{loan_ref_number}")
    public ResponseEntity<?> approveLoanAsFrontOfficer(@PathVariable(value = "loan_ref_number") String kycNumber, @Valid @RequestBody LoanRequest loanRequest) {
        try {
            customerService.saveCustomerData(loanRequest);
        } catch (Exception e) {
            if(e instanceof LoanException) {
                LoanError loanError = new LoanError(((LoanException) e).getErroCode(), ((LoanException) e).getMessage());
                return new ResponseEntity<LoanError>(loanError, HttpStatus.BAD_REQUEST);
            } else {
                LoanError loanError = new LoanError(((LoanException) e).getErroCode(),  e.getMessage());
                return new ResponseEntity<LoanError>(loanError, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<LoanRequest>(loanRequest, HttpStatus.OK);
    }

}
