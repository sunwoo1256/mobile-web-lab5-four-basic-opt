package com.example.fourbasicopt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {

    private Calculator calculator;

    @Before
    public void setUp() {
        calculator = new Calculator();
    }

    private void type(String keys) {
        for (char key : keys.toCharArray()) {
            switch (key) {
                case '+':
                    calculator.inputOperator(Calculator.ADD);
                    break;
                case '-':
                    calculator.inputOperator(Calculator.SUBTRACT);
                    break;
                case '*':
                    calculator.inputOperator(Calculator.MULTIPLY);
                    break;
                case '/':
                    calculator.inputOperator(Calculator.DIVIDE);
                    break;
                case '=':
                    calculator.evaluate();
                    break;
                case '.':
                    calculator.inputDot();
                    break;
                case '<':
                    calculator.backspace();
                    break;
                default:
                    calculator.inputDigit(key - '0');
            }
        }
    }

    @Test
    public void fourBasicOperations() {
        type("12+3=");
        assertEquals("15", calculator.getDisplay());
        assertEquals("12 + 3 =", calculator.getExpression());
        type("7-10=");
        assertEquals("-3", calculator.getDisplay());
        type("6*7=");
        assertEquals("42", calculator.getDisplay());
        type("9/4=");
        assertEquals("2.25", calculator.getDisplay());
    }

    @Test
    public void decimalsAreExact() {
        type("0.1+0.2=");
        assertEquals("0.3", calculator.getDisplay());
    }

    @Test
    public void chainedOperatorsEvaluateLeftToRight() {
        type("2+3*");
        assertEquals("5", calculator.getDisplay());
        type("4=");
        assertEquals("20", calculator.getDisplay());
    }

    @Test
    public void resultCanBeUsedAsNextOperand() {
        type("2+3=*4=");
        assertEquals("20", calculator.getDisplay());
    }

    @Test
    public void lastOperatorWins() {
        type("8+*2=");
        assertEquals("16", calculator.getDisplay());
    }

    @Test
    public void divideByZeroIsError() {
        type("5/0=");
        assertTrue(calculator.isError());
        type("3");
        assertFalse(calculator.isError());
        assertEquals("3", calculator.getDisplay());
    }

    @Test
    public void backspaceAndLeadingZeros() {
        type("007");
        assertEquals("7", calculator.getDisplay());
        type("5.5<<<<");
        assertEquals("0", calculator.getDisplay());
        type("1..2");
        assertEquals("1.2", calculator.getDisplay());
    }

    @Test
    public void repeatingDecimalIsRounded() {
        type("1/3=");
        assertEquals("0.333333333333333", calculator.getDisplay());
    }
}
