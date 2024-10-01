package az.edu.turing.mspayment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageablePaymentResponse {
    boolean nextPage;
    int totalPage;
    Long totalElements;
    List<PaymentResponse> payments;

}
