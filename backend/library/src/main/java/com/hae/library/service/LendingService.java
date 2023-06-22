package com.hae.library.service;

import com.hae.library.domain.Book;
import com.hae.library.domain.Lending;
import com.hae.library.domain.Member;
import com.hae.library.dto.Lending.ResponseLendingDto;
import com.hae.library.repository.LendingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LendingService {
    private final LendingRepository lendingRepo;

    @Transactional
    public void lendingBook() {
        // 도서 대출 로직 구현
        Lending lending = new Lending().builder()
                .book(Book.builder().callSign("100.32.v1.c2").build())
                .lendingLibrarian(Member.builder().build())
                .lendingCondition("lendingCondition")
                .build();

        lendingRepo.save(lending);
    }

    @Transactional
    public void returningBook(Long lendingId) {
        // 도서 반납 로직 구현
        Lending lending = lendingRepo.findById(lendingId).get();

        lending.setReturning(Member.builder().build(), "RETURNED");
        lendingRepo.save(lending);
    }

    @Transactional
    public ResponseLendingDto getOneLendingHistory(Long lendingId) {
        // 개별 대출 기록 조회 로직 구현
        ResponseLendingDto responseLendingDto = ResponseLendingDto.from(lendingRepo.findById(lendingId).get());
        return responseLendingDto;
    }

    @Transactional
    public ArrayList<ResponseLendingDto> getAllLendingHistory() {
        // 전체 대출 기록 조회 로직 구현
        ArrayList<ResponseLendingDto> responseLendingDtoList = new ArrayList<>();
        for (Lending lending : lendingRepo.findAll()) {
            responseLendingDtoList.add(ResponseLendingDto.from(lending));
        }

        return responseLendingDtoList;
    }

    // 유저가 대출 연장 버튼 클릭(예약이 있으면 불가)
    @Transactional
    public boolean updateRenew(Long lendingId) {
        // 대출 연장 로직 구현
        Optional<Lending> optionalLending = lendingRepo.findById(lendingId);
        if (optionalLending.isPresent()) {
            Lending lending = optionalLending.get();
            lending.renewLending();
            return true;
        }

        return false;
    }

    @Transactional
    public boolean deleteLending(Long lendingId) {
        // 대출 삭제 로직 구현
        Optional<Lending> optionalLending = lendingRepo.findById(lendingId);
        if (optionalLending.isPresent()) {
            lendingRepo.deleteById(lendingId);
            return true;
        }

        return false;
    }
}
