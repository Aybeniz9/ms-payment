package az.edu.turing.mspayment.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults( level = AccessLevel.PRIVATE)
public class PaymentRequest {
    @Min(value=1)
            @Max( value=1000)
    BigDecimal amount;

    @NotBlank(message = "Currency connot be blank")
    String currency;

    @NotBlank(message = "Description cannot be blank")
    String description;
}
