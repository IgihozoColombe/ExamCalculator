package com.demo.simplecalculator.backend.repository;

import com.demo.simplecalculator.backend.controllers.DoMathRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("calculatorHistoryRepository")
public interface CalculatorHistoryRepository extends JpaRepository<DoMathRequest, Long> {
    Page<DoMathRequest> findByClientToken(String clientToken, Pageable pageable);
}