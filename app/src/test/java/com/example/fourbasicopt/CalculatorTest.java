package com.example.fourbasicopt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
                case 'C':
                    calculator.clear();
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
        type("*3=");
        assertEquals("0.999999999999999", calculator.getDisplay());
    }

    @Test
    public void initialState() {
        assertEquals("0", calculator.getDisplay());
        assertEquals("", calculator.getExpression());
        assertFalse(calculator.isError());
    }

    @Test
    public void equalsWithoutOperatorDoesNothing() {
        type("=");
        assertEquals("0", calculator.getDisplay());
        type("12=");
        assertEquals("12", calculator.getDisplay());
        assertEquals("", calculator.getExpression());
    }

    @Test
    public void equalsRightAfterOperatorReusesLeftOperand() {
        type("5+=");
        assertEquals("10", calculator.getDisplay());
        assertEquals("5 + 5 =", calculator.getExpression());
    }

    @Test
    public void expressionFollowsInput() {
        type("12+");
        assertEquals("12 +", calculator.getExpression());
        assertEquals("12", calculator.getDisplay());
        type("*");
        assertEquals("12 ×", calculator.getExpression());
        type("3");
        assertEquals("12 ×", calculator.getExpression());
        assertEquals("3", calculator.getDisplay());
        type("=");
        assertEquals("12 × 3 =", calculator.getExpression());
        type("5");
        assertEquals("", calculator.getExpression());
        assertEquals("5", calculator.getDisplay());
    }

    @Test
    public void dotStartsNewOperandWithZero() {
        type("3+.5=");
        assertEquals("3.5", calculator.getDisplay());
        assertEquals("3 + 0.5 =", calculator.getExpression());
    }

    @Test
    public void trailingDotAndZerosAreDropped() {
        type("5.+2=");
        assertEquals("7", calculator.getDisplay());
        assertEquals("5 + 2 =", calculator.getExpression());
        type("2.50+0=");
        assertEquals("2.5", calculator.getDisplay());
        type("0.5-0.5=");
        assertEquals("0", calculator.getDisplay());
    }

    @Test
    public void negativeResultCanBeUsedAsNextOperand() {
        type("3-5=");
        assertEquals("-2", calculator.getDisplay());
        type("*2=");
        assertEquals("-4", calculator.getDisplay());
        assertEquals("-2 × 2 =", calculator.getExpression());
    }

    @Test
    public void inputLengthIsLimited() {
        type("12345678901234567890");
        assertEquals("123456789012345", calculator.getDisplay());
    }

    @Test
    public void largeResultUsesScientificNotation() {
        type("999999999999999*999999999999999=");
        assertEquals("9.99999999999998E+29", calculator.getDisplay());
    }

    @Test
    public void backspaceDoesNotEditResult() {
        type("1+2=<");
        assertEquals("3", calculator.getDisplay());
        type("12+<");
        assertEquals("12", calculator.getDisplay());
        assertEquals("12 +", calculator.getExpression());
    }

    @Test
    public void clearResetsEverything() {
        type("12+3C");
        assertEquals("0", calculator.getDisplay());
        assertEquals("", calculator.getExpression());
        type("4=");
        assertEquals("4", calculator.getDisplay());
    }

    @Test
    public void divideByZeroInChainIsError() {
        type("5/0+");
        assertTrue(calculator.isError());
        assertEquals("", calculator.getExpression());
    }

    @Test
    public void errorIgnoresOperatorsUntilCleared() {
        type("5/0=+=");
        assertTrue(calculator.isError());
        type("<");
        assertFalse(calculator.isError());
        assertEquals("0", calculator.getDisplay());

        type("5/0=C");
        assertFalse(calculator.isError());

        type("5/0=.5");
        assertFalse(calculator.isError());
        assertEquals("0.5", calculator.getDisplay());
    }

    @Test
    public void stateSurvivesSerialization() throws Exception {
        type("12+3");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(calculator);
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bytes.toByteArray()))) {
            calculator = (Calculator) in.readObject();
        }

        assertEquals("3", calculator.getDisplay());
        assertEquals("12 +", calculator.getExpression());
        type("=");
        assertEquals("15", calculator.getDisplay());
    }
}
