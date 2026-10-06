package com.example.fourbasicopt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

public class Calculator implements Serializable {

    public static final String ADD = "+";
    public static final String SUBTRACT = "−";
    public static final String MULTIPLY = "×";
    public static final String DIVIDE = "÷";

    private static final int MAX_INPUT_LENGTH = 15;
    private static final int MAX_PLAIN_LENGTH = 20;
    private static final MathContext PRECISION = new MathContext(15);

    private String current = "0";
    private String expression = "";
    private BigDecimal accumulator;
    private String pendingOperator;
    private boolean startNewInput;
    private boolean error;

    public String getDisplay() {
        return current;
    }

    public String getExpression() {
        return expression;
    }

    public boolean isError() {
        return error;
    }

    public void inputDigit(int digit) {
        beginInput();
        if (current.equals("0")) {
            current = String.valueOf(digit);
        } else if (current.length() < MAX_INPUT_LENGTH) {
            current += digit;
        }
    }

    public void inputDot() {
        beginInput();
        if (!current.contains(".") && current.length() < MAX_INPUT_LENGTH) {
            current += ".";
        }
    }

    public void inputOperator(String operator) {
        if (error) {
            return;
        }
        BigDecimal value = new BigDecimal(current);
        if (pendingOperator == null) {
            accumulator = value;
        } else if (!startNewInput && !apply(value)) {
            return;
        }
        pendingOperator = operator;
        current = format(accumulator);
        expression = current + " " + operator;
        startNewInput = true;
    }

    public void evaluate() {
        if (error || pendingOperator == null) {
            return;
        }
        BigDecimal operand = new BigDecimal(current);
        String left = format(accumulator);
        String operator = pendingOperator;
        if (!apply(operand)) {
            return;
        }
        expression = left + " " + operator + " " + format(operand) + " =";
        current = format(accumulator);
        accumulator = null;
        pendingOperator = null;
        startNewInput = true;
    }

    public void backspace() {
        if (error) {
            clear();
            return;
        }
        if (startNewInput) {
            return;
        }
        current = current.substring(0, current.length() - 1);
        if (current.isEmpty()) {
            current = "0";
        }
    }

    public void clear() {
        current = "0";
        expression = "";
        accumulator = null;
        pendingOperator = null;
        startNewInput = false;
        error = false;
    }

    private void beginInput() {
        if (error) {
            clear();
        }
        if (startNewInput) {
            current = "0";
            startNewInput = false;
            if (pendingOperator == null) {
                expression = "";
            }
        }
    }

    private boolean apply(BigDecimal operand) {
        switch (pendingOperator) {
            case ADD:
                accumulator = accumulator.add(operand, PRECISION);
                break;
            case SUBTRACT:
                accumulator = accumulator.subtract(operand, PRECISION);
                break;
            case MULTIPLY:
                accumulator = accumulator.multiply(operand, PRECISION);
                break;
            case DIVIDE:
                if (operand.signum() == 0) {
                    clear();
                    error = true;
                    return false;
                }
                accumulator = accumulator.divide(operand, PRECISION);
                break;
            default:
                throw new IllegalStateException("Unknown operator: " + pendingOperator);
        }
        return true;
    }

    private static String format(BigDecimal value) {
        if (value.signum() == 0) {
            return "0";
        }
        BigDecimal stripped = value.stripTrailingZeros();
        String plain = stripped.toPlainString();
        return plain.length() > MAX_PLAIN_LENGTH ? stripped.toString() : plain;
    }
}
