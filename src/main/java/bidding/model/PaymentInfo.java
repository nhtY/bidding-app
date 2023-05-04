package bidding.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

@Data
@RequiredArgsConstructor(staticName = "of")
public class PaymentInfo {

    @NonNull
    @CreditCardNumber(message = "Geçersiz Kart numarası")
    private String ccNumber;

    /** Credit Card expiration date MM/YY **/
    @NonNull
    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message="MM/YY formatında olmalıdır")
    private String ccExpiration;

    /** Credit Card CVV**/
    @NonNull
    @Digits(integer = 3, fraction = 0, message = "Geçersiz CVV")
    private String ccCVV;

}
