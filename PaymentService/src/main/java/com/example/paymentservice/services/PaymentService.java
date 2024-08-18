package com.example.paymentservice.services;

import com.example.paymentservice.payment_gateway.PaymentGateway;
import com.example.paymentservice.payment_gateway.PaymentGatewayStrategyChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentGatewayStrategyChooser paymentGatewayStrategyChooser;

    public String getPaymentLink(Long amount, String orderId, String phoneNo, String name){
        PaymentGateway paymentGateway = paymentGatewayStrategyChooser.returnPaymentGateway();

        return paymentGateway.getPaymentLink(amount, orderId, phoneNo, name);
    }
}
