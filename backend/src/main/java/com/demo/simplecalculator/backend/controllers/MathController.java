package com.demo.simplecalculator.backend.controllers;

import com.demo.simplecalculator.backend.service.CalculatorHistoryService;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${API_PREFIX_V}")
public class MathController {
    @Autowired
    CalculatorHistoryService calculatorHistoryService;
    @Autowired
    DoMathRequest doMathRequest;
    @Autowired
    MathOperator mathOperator;

    @RequestMapping(value="/solve/{number1}/{sign}/{number2}", method = RequestMethod.GET)
    public ResponseEntity<DoMathRequest> doMaths(
        @PathVariable("number1") String number1,
        @PathVariable("sign") String sign,
        @PathVariable("number2") String number2,
        @RequestHeader(value="client-token", required=false) String clientToken
    ) {
        boolean isInputAcceptable = isInputAcceptable(number1, String.valueOf(number2), sign);

        if ( !isInputAcceptable ) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }

        DoMathRequest solution = doMath(number1, number2, sign);
        DoMathRequest storedSolution = storeSolution(solution, clientToken);
        return new ResponseEntity<>(storedSolution, HttpStatus.OK);
    }

    @RequestMapping(value={ "/getHistory/{page}", "/getHistory" }, method = RequestMethod.GET)
    public ResponseEntity<List<DoMathRequest>> getHistory(
        @PathVariable("page") Optional<Integer> pageParameter,
        @RequestHeader(value="client-token", required=false) String clientToken
    ) {
        int page = 0;
        if (pageParameter.isPresent()) {
            page = pageParameter.get();
        }

        List<DoMathRequest> history = this.calculatorHistoryService.getHistoryByPage(page, clientToken);

        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    private boolean isInputAcceptable(String number1, String number2, String sign) {
        return isIntNumber(number1) && isIntNumber(number2) && isValidSign(sign);
    }

    private boolean isIntNumber(String value) {
        try{
            Integer.valueOf(value);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    private boolean isValidSign(String value) {
        return DoMathRequest.SignSymbols.values().contains(value);
    }

    private DoMathRequest doMath(String operand1, String operand2, String operation) {
        int int1 = Integer.valueOf( operand1);
        int int2 = Integer.valueOf(operand2);

        DoMathRequest.Sign sign = DoMathRequest.getSignBySymbol(operation);
        double result = 0;
        switch( sign ) {
            case PLUS:
                    result = mathOperator.add(int1, int2);
                    break;
            case MINUS:
                    result = mathOperator.subtract(int1, int2);
                    break;
            case MULTIPLICATION:
                    result = mathOperator.multiply(int1, int2);
                    break;
            case DIVISION:
                    result = mathOperator.divide(int1, int2);
                    break;
        }

        DoMathRequest solution = new DoMathRequest(int1, int2, sign, result);
        
        return solution;
    }

    private DoMathRequest storeSolution(DoMathRequest solution, String clientToken) {
        solution.setClientToken(clientToken);
        return this.calculatorHistoryService.save(solution);
    }

}
