package com.example.paymentservice.payment_gateway;

import com.stripe.Stripe;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentGateway implements PaymentGateway {
    @Value("${stripe.key}")
    private String stripeKey;

    @Override
    public String getPaymentLink(Long amount, String orderId, String phoneNo, String name) {
        try {
            Stripe.apiKey = stripeKey;

            PaymentLinkCreateParams params =
                    PaymentLinkCreateParams.builder()
                            .addLineItem(
                                    PaymentLinkCreateParams.LineItem.builder()
                                            .setPrice(getPrice(amount).getId())
                                            .setQuantity(1L)
                                            .build()
                            ).setAfterCompletion(PaymentLinkCreateParams.AfterCompletion.builder().setRedirect(
                                    PaymentLinkCreateParams.AfterCompletion.Redirect.builder().setUrl(
                                            "https.//scaler.com/"
                                    ).build()
                            ).build())
                            .build();

            PaymentLink paymentLink = PaymentLink.create(params);
            return paymentLink.getUrl();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Price getPrice(Long amount){
        try {
            Stripe.apiKey = stripeKey;

            PriceCreateParams params =
                    PriceCreateParams.builder()
                            .setCurrency("usd" )
                            .setUnitAmount(amount)
                            .setRecurring(
                                    PriceCreateParams.Recurring.builder()
                                            .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                            .build()
                            )
                            .setProductData(
                                    PriceCreateParams.ProductData.builder().setName("Gold Plan" ).build()
                            )
                            .build();

            return Price.create(params);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
