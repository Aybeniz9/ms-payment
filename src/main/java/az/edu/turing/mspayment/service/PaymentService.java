package az.edu.turing.mspayment.service;

import az.edu.turing.mspayment.entity.Payment;
import az.edu.turing.mspayment.entity.repository.PaymentRepository;
import az.edu.turing.mspayment.exception.NotFoundException;
import az.edu.turing.mspayment.model.PageablePaymentResponse;
import az.edu.turing.mspayment.model.PaymentRequest;
import az.edu.turing.mspayment.model.PaymentResponse;
import az.edu.turing.mspayment.model.client.CountryClient;
import az.edu.turing.mspayment.model.criteria.PaymentCriteria;
import az.edu.turing.mspayment.service.specification.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static az.edu.turing.mspayment.mapper.PaymentMapper.mapEntityToPaymentResponse;
import static az.edu.turing.mspayment.mapper.PaymentMapper.mapRequestToEntity;
import static az.edu.turing.mspayment.model.constants.ExceptionConstants.COUNTRY_NOT_FOUND_CODE;
import static az.edu.turing.mspayment.model.constants.ExceptionConstants.COUNTRY_NOT_FOUND_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CountryClient countryClient;

    public void save(PaymentRequest request) {
        log.info("Saving payment started");
        countryClient.getAvailableCountries(request.getCurrency())
                .stream().filter(countryDto -> countryDto.getRemainingLimit().compareTo(request.getAmount()) > 0)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(COUNTRY_NOT_FOUND_MESSAGE, request.getAmount(), request.getCurrency()), COUNTRY_NOT_FOUND_CODE));
        paymentRepository.save(mapRequestToEntity(request));
        log.info("Saving payment finished");
    }

    public PaymentResponse getPaymentById(Long id) {
        log.info("Getting payment started by id : {}", id);
        Payment payment = fetchPaymentIfExists(id);
        log.info("Getting payment finished by id : {}", id);
        return mapEntityToPaymentResponse(payment);
    }

    public PageablePaymentResponse getAllPayments(int page, int count, PaymentCriteria criteria) {
        log.info("getAllPayments started");
        var pageable= PageRequest.of(page, count, Sort.by(Sort.Direction.DESC,"id"));
        var pageablePayments = paymentRepository.findAll(new PaymentSpecification(new PaymentCriteria()),pageable);
        var payments=pageablePayments.getContent().stream()
                .map(payment -> mapEntityToPaymentResponse(payment)).collect(Collectors.toList());
        log.info("getAllPayments finished");
        return PageablePaymentResponse.builder()
                        .payments(payments)
                                .nextPage(pageablePayments.hasNext())
                                        .totalElements(pageablePayments.getTotalElements())
                .totalPage(pageablePayments.getTotalPages())

                .build();
    }
    public void updatePayment(Long id, PaymentRequest request){
        log.info("Updating payment started by id : {}", id);
        Payment payment = fetchPaymentIfExists(id);
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        paymentRepository.save(payment);
        log.info("Updating payment finished by id : {}", id);

    }

    private Payment fetchPaymentIfExists(Long id) {
        return paymentRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void delete(Long id) {
        log.info("Deleting payment started by id : {}", id);
        paymentRepository.deleteById(id);
        log.info("Deleting payment finished by id : {}", id);
    }
}
