package bidding.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FormHandle {

    public static ResponseEntity<Object> handleFormValidationError(BindingResult bindingResult, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status);

        //Get all fields errors
        List<String> errors = bindingResult
                .getFieldErrors()
                .stream()
                .map(x -> x.getField() + ':' + x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatusCode.valueOf(status.value()));
    }
}
