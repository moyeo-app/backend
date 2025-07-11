package com.moyeo.backend.payment.application.mapper;

import com.moyeo.backend.payment.application.dto.PaymentRequestDto;
import com.moyeo.backend.payment.domain.PaymentHistory;
import com.moyeo.backend.payment.domain.PaymentStatus;
import com.moyeo.backend.payment.domain.PaymentType;
import com.moyeo.backend.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(user)")
    @Mapping(target = "type", expression = "java(type)")
    @Mapping(target = "status", expression = "java(status)")
    PaymentHistory toPayment(PaymentRequestDto requestDto, User user, PaymentType type, PaymentStatus status);
}
