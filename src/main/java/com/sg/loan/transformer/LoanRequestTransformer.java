package com.sg.loan.transformer;

import com.sg.loan.entity.LoanRequestEntity;
import com.sg.loan.model.LoanRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class LoanRequestTransformer {
    public LoanRequestEntity transformCustomerDetails(LoanRequest loanRequest) {
        LoanRequestEntity loanRequestEntity = new LoanRequestEntity();
        BeanUtils.copyProperties(loanRequest, loanRequestEntity);
        Timestamp approvedOnLoanOfficer = new Timestamp(System.currentTimeMillis());
        loanRequestEntity.setApprovedOnRiskOfficer(approvedOnLoanOfficer);
        loanRequestEntity.setApprovedByFrontOfficer(loanRequest.getApprovedByFrontOfficer());
        loanRequestEntity.setApprovedOnFrontOfficer(loanRequest.getApprovedOnFrontOfficer());
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            Date parsedDate = dateFormat.parse(loanRequest.getApprovedOnFrontOfficer());
//            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
//            loanRequestEntity.setApprovedOnFrontOfficer(timestamp);
//        } catch(Exception e) { //this generic but you can control another types of exception
//            // look the origin of exception
//        }


//        loanRequestEntity.setLoanRefNumber("LOAN"+UUID.randomUUID().toString().replace("-", ""));
        return  loanRequestEntity;
    }
}
