package com.demo.simplecalculator.backend.controllers;

import org.springframework.stereotype.Repository;

import java.util.AbstractMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashMap;
import javax.persistence.Column;


@Entity
@Repository
public class DoMathRequest {
    
    public enum Sign {PLUS, MINUS, MULTIPLICATION, DIVISION};

    
    final public static HashMap<Sign, String> SignSymbols = new HashMap() {{
        put(Sign.PLUS, "+");
        put(Sign.MINUS, "-");
        put(Sign.MULTIPLICATION, "x");
        put(Sign.DIVISION, ":");
    }};
    
    final public static DoMathRequest.Sign getSignBySymbol(String signSymbol) {
        return DoMathRequest.SignSymbols
            .entrySet()
            .stream()
            .filter(entry -> signSymbol.equals(entry.getValue()))
            .findFirst()
            .orElse(new AbstractMap.SimpleEntry<Sign, String>(null,null))
            .getKey();
    }
    
    // attributes
    
    @Id
    @GeneratedValue
    private long id;
    private int number1;
    private int number2;
    private Sign sign = null;
    private String signSymbol = null;
    private double result;
    @Column(name = "clientToken")
    private String clientToken;
    
    // constructors

    public DoMathRequest() {
    }
    
    public DoMathRequest ( long id, int number1, int number2, Sign sign, double result, String clientToken ) {
        this.id = id;
        this.number1 = number1;
        this.number2 = number2;
        this.sign = sign;
        this.signSymbol = this.SignSymbols.get(sign);
        this.result = result;
        this.clientToken = clientToken;
    }

    public DoMathRequest ( long id, int number1, int number2, Sign sign, double result ) {
        this.id = id;
        this.number1 = number1;
        this.number2 = number2;
        this.sign = sign;
        this.signSymbol = this.SignSymbols.get(sign);
        this.result = result;
    }
    
    public DoMathRequest ( int number1, int number2, Sign sign, double result ) {
        this.number1 = number1;
        this.number2 = number2;
        this.sign = sign;
        this.signSymbol = this.SignSymbols.get(sign);
        this.result = result;
    }
    
    public DoMathRequest ( int number1, int number2, Sign sign ) {
        this.number1 = number1;
        this.number2 = number2;
        this.sign = sign;
        this.signSymbol = this.SignSymbols.get(sign);
    }
    
    // getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber1() {
        return number1;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public int getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
        this.signSymbol = this.SignSymbols.get(sign);
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getSignSymbol() {
        return signSymbol;
    }

    public void setSignSymbol(String signSymbol) {
        this.signSymbol = signSymbol;
        this.sign = getSignBySymbol(signSymbol);
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }
    
}