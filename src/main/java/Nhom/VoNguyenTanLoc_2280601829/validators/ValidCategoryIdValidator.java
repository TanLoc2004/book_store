package Nhom.VoNguyenTanLoc_2280601829.validators;

import Nhom.VoNguyenTanLoc_2280601829.entities.Category;
import Nhom.VoNguyenTanLoc_2280601829.validators.annotations.ValidCategoryId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class ValidCategoryIdValidator implements
ConstraintValidator<ValidCategoryId, Category> {
@Override
public boolean isValid(Category category,
ConstraintValidatorContext context) {
return category != null && category.getId() != null;
}
}