package com.moyeo.backend.challenge.basic.domain.repository;

import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadRequestDto;
import com.moyeo.backend.challenge.basic.application.dto.ChallengeReadResponseDto;
import com.moyeo.backend.challenge.basic.domain.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface ChallengeInfoRepository {

    Optional<Challenge> findByIdAndIsDeletedFalse(String id);

    Page<ChallengeReadResponseDto> searchChallenges(ChallengeReadRequestDto requestDto, Pageable pageable);

    Challenge save(Challenge challenge);

    void updateStatus(LocalDate date);
}
