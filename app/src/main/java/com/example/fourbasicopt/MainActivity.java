package com.example.fourbasicopt;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_CALCULATOR = "calculator";

    private static final int[] DIGIT_BUTTON_IDS = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
    };

    private Calculator calculator = new Calculator();
    private TextView expressionView;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState != null) {
            Calculator saved = (Calculator) savedInstanceState.getSerializable(STATE_CALCULATOR);
            if (saved != null) {
                calculator = saved;
            }
        }

        expressionView = findViewById(R.id.tvExpression);
        resultView = findViewById(R.id.tvResult);

        for (int digit = 0; digit < DIGIT_BUTTON_IDS.length; digit++) {
            final int value = digit;
            bind(DIGIT_BUTTON_IDS[digit], () -> calculator.inputDigit(value));
        }
        bind(R.id.btnDot, () -> calculator.inputDot());
        bind(R.id.btnAdd, () -> calculator.inputOperator(Calculator.ADD));
        bind(R.id.btnSubtract, () -> calculator.inputOperator(Calculator.SUBTRACT));
        bind(R.id.btnMultiply, () -> calculator.inputOperator(Calculator.MULTIPLY));
        bind(R.id.btnDivide, () -> calculator.inputOperator(Calculator.DIVIDE));
        bind(R.id.btnEquals, () -> calculator.evaluate());
        bind(R.id.btnClear, () -> calculator.clear());
        bind(R.id.btnBackspace, () -> calculator.backspace());

        render();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_CALCULATOR, calculator);
    }

    private void bind(int buttonId, Runnable action) {
        findViewById(buttonId).setOnClickListener(v -> {
            action.run();
            render();
        });
    }

    private void render() {
        expressionView.setText(calculator.getExpression());
        if (calculator.isError()) {
            resultView.setText(R.string.error_divide_by_zero);
        } else {
            resultView.setText(calculator.getDisplay());
        }
    }
}
