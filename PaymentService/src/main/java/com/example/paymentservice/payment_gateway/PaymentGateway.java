package com.example.paymentservice.payment_gateway;

import org.springframework.stereotype.Component;

@Component
public interface PaymentGateway {
    String getPaymentLink(Long amount, String orderId, String phoneNo, String name);
}
