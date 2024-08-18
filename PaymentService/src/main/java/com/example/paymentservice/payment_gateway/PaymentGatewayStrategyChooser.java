package com.example.paymentservice.payment_gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayStrategyChooser {
    @Autowired
    private RazorpayPaymentGateway razorpayPaymentGateway;

    public PaymentGateway returnPaymentGateway() {
        return razorpayPaymentGateway;
    }
}
