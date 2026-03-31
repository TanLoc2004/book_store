package Nhom.VoNguyenTanLoc_2280601829.validators;

import Nhom.VoNguyenTanLoc_2280601829.services.UserService;
import Nhom.VoNguyenTanLoc_2280601829.validators.annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUsernameValidator
        implements ConstraintValidator<ValidUsername, String> {

    private UserService userService;         // không final

    public ValidUsernameValidator() { }      // constructor no‑arg bắt buộc

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return userService == null || userService.findByUsername(username).isEmpty();
    }
}
