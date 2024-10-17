package az.edu.turing.mspayment.controller;

import az.edu.turing.mspayment.model.PageablePaymentResponse;
import az.edu.turing.mspayment.model.PaymentRequest;
import az.edu.turing.mspayment.model.PaymentResponse;
import az.edu.turing.mspayment.model.criteria.PaymentCriteria;
import az.edu.turing.mspayment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void savePayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        paymentService.save(paymentRequest);
    }

    @GetMapping
    public PageablePaymentResponse getAllPayments(@RequestParam int page, @RequestParam int count, PaymentCriteria criteria) {

        System.out.println(paymentService.getAllPayments(page, count, criteria));
        return paymentService.getAllPayments(page, count, criteria);
    }

    @GetMapping("/{id}")
    public PaymentResponse getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PutMapping("/{id}")
    public void updatePayment(@PathVariable Long id, @RequestBody PaymentRequest paymentRequest) {
        paymentService.updatePayment(id, paymentRequest);
    }



    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.delete(id);
    }
}
