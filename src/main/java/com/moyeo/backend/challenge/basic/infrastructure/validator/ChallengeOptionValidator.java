package com.moyeo.backend.challenge.basic.infrastructure.validator;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.TimeOption;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ChallengeOptionValidator")
public class ChallengeOptionValidator implements ConstraintValidator<ValidChallengeOption, ChallengeCreateRequestDto> {

    @Override
    public boolean isValid(ChallengeCreateRequestDto challengeCreateRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = switch (challengeCreateRequestDto.getType()) {
            case TIME -> challengeCreateRequestDto.getOption() instanceof TimeOption;
            case ATTENDANCE, CONTENT -> challengeCreateRequestDto.getOption() instanceof StartEndOption;
            default -> false;
        };

        if (!valid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("챌린지 옵션 값이 유효하지 않습니다.")
                    .addPropertyNode("option")
                    .addConstraintViolation();
        }

        return valid;
    }
}
