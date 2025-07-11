package com.moyeo.backend.challenge.basic.infrastructure.validator;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeCreateRequestDto;
import com.moyeo.backend.challenge.basic.domain.StartEndOption;
import com.moyeo.backend.challenge.basic.domain.TimeOption;
import com.moyeo.backend.common.enums.ErrorCode;
import com.moyeo.backend.common.exception.CustomException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChallengeOptionValidator implements ConstraintValidator<ValidChallengeOption, ChallengeCreateRequestDto> {

    @Override
    public boolean isValid(ChallengeCreateRequestDto challengeCreateRequestDto, ConstraintValidatorContext constraintValidatorContext) {
        switch (challengeCreateRequestDto.getType()) {
            case TIME -> {
                if (!(challengeCreateRequestDto.getOption() instanceof TimeOption)) {
                    throw new CustomException(ErrorCode.INVALID_OPTION_FORMAT);
                }
            }
            case ATTENDANCE, CONTENT -> {
                if (!(challengeCreateRequestDto.getOption() instanceof StartEndOption)) {
                    throw new CustomException(ErrorCode.INVALID_OPTION_FORMAT);
                }
            }
            default -> throw new CustomException(ErrorCode.INVALID_OPTION_FORMAT);
        }
        return true;
    }
}
