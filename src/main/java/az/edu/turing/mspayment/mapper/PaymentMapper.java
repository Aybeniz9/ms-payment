package az.edu.turing.mspayment.mapper;

import az.edu.turing.mspayment.entity.Payment;
import az.edu.turing.mspayment.model.PaymentRequest;
import az.edu.turing.mspayment.model.PaymentResponse;

import java.time.LocalDateTime;


public class PaymentMapper {

    public static Payment mapRequestToEntity(PaymentRequest request) {
        return Payment.builder()
                        .amount(request.getAmount())
                                .description(request.getDescription())
                .build();
    }
    public static PaymentResponse mapEntityToPaymentResponse(Payment payment){
return PaymentResponse.builder().
        id(payment.getId()).
        amount(payment.getAmount()).
        description(payment.getDescription()).
        responseTime(LocalDateTime.now()).build();


    }
}
