package com.demo.simplecalculator.backend.services;


import com.demo.simplecalculator.backend.controllers.DoMathRequest;
import com.demo.simplecalculator.backend.repository.CalculatorHistoryRepository;
import com.demo.simplecalculator.backend.service.CalculatorHistoryService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CalculatorHistoryServiceTest {
    
    @Autowired
    private CalculatorHistoryRepository calculatorHistoryRepository;
    
    @AfterEach
    void tearDown(){
        calculatorHistoryRepository.deleteAll();
    }
    
    @Test
    void getHistory(){
        
        // exclude pagination working on small set

        DoMathRequest exampleEquation = new DoMathRequest(10, 20, DoMathRequest.Sign.PLUS, 30);
        calculatorHistoryRepository.save(exampleEquation);
        
        CalculatorHistoryService calculatorHistoryService = new CalculatorHistoryService(calculatorHistoryRepository);

        List<DoMathRequest> equationsList = calculatorHistoryService.getHistory();
        DoMathRequest savedExampleEquation = equationsList.get(0);

        assertEquals(savedExampleEquation.getNumber1(), exampleEquation.getNumber1());
        assertEquals(savedExampleEquation.getNumber2(), exampleEquation.getNumber2());
        assertEquals(savedExampleEquation.getSign(), exampleEquation.getSign());
        assertEquals(savedExampleEquation.getResult(), exampleEquation.getResult());
    }
    
    @Test
    void getPaginatedHistory(){
        final int pageSize = CalculatorHistoryService.PAGE_SIZE;
        
        // insert 5 pages of records
        for ( int i = 0; i < pageSize * 5; i++ ) {
            DoMathRequest exampleEquation = new DoMathRequest(10, 10, DoMathRequest.Sign.MINUS, 0);
            calculatorHistoryRepository.save(exampleEquation);   
        }
        
        CalculatorHistoryService calculatorHistoryService = new CalculatorHistoryService(calculatorHistoryRepository);
        
        // ensure records number on first page
        List<DoMathRequest> equationsList = calculatorHistoryService.getHistory();
        assertEquals(pageSize, equationsList.size());
        
        // ensure records number on third page
        equationsList = calculatorHistoryService.getHistoryByPage(2);
        assertEquals(pageSize, equationsList.size());
        
        // ensure no records if there arent' enough records in the database
        equationsList = calculatorHistoryService.getHistoryByPage(10);
        assertEquals(0, equationsList.size());
    }
    
    @Test
    void saveEquation() {
        CalculatorHistoryService calculatorHistoryService = new CalculatorHistoryService(calculatorHistoryRepository);
        DoMathRequest equationExample = new DoMathRequest(10, 1, DoMathRequest.Sign.MULTIPLICATION, 10);

        calculatorHistoryService.save(equationExample);

        assertEquals(1, calculatorHistoryRepository.count());
    }
    
}
