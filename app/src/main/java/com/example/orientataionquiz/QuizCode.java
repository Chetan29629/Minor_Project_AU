package com.example.orientataionquiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuizCode extends AppCompatActivity {
    private EditText otp1, otp2, otp3, otp4;
    private Button verifyCode;
    private static final String CODE = "1407";
    private int selectedOTPposition = 0;
    private InputMethodManager inputMethodManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String language = intent.getStringExtra("language");
        String name = intent.getStringExtra("name");
        String course = intent.getStringExtra("course");

// Soft Input Method
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_code_activity);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        verifyCode = findViewById(R.id.verifyCode);

        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);

        // Show keyboard by default on start Activity
        showKeyboard(otp1);


        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getCode = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                if ((getCode.length() == 4) && (getCode.equals(CODE))) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Toast.makeText(QuizCode.this, "Code Successfully Verified", Toast.LENGTH_SHORT).show();

                    Intent inext = new Intent(QuizCode.this, Quiz.class);
                    inext.putExtra("language", language);
                    inext.putExtra("name", name);
                    inext.putExtra("course", course);
                    startActivity(inext);
                    finish();


                } else {
                    Toast.makeText(QuizCode.this, "Please Enter a valid Code", Toast.LENGTH_SHORT).show();
                    //User-defined method
                    resetOTPFields();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //TextWatcher
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // No action needed
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // No action needed
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {

                switch (selectedOTPposition) {
                    case 0:
                        selectedOTPposition = 1;
                        showKeyboard(otp2);
                        break;
                    case 1:
                        selectedOTPposition = 2;
                        showKeyboard(otp3);
                        break;
                    case 2:
                        selectedOTPposition = 3;
                        showKeyboard(otp4);
                        break;

                    default:
                        verifyCode.setBackgroundColor(Color.RED);
                        verifyCode.setTextColor(Color.WHITE);

                }

            } else {

                if (selectedOTPposition > 0) {
                    selectedOTPposition--;
                    verifyCode.setBackgroundColor(Color.LTGRAY);
                    showKeyboard(getOTPField(selectedOTPposition));
                }
            }
        }
    };

    private void showKeyboard(EditText otp) {
        otp.requestFocus();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otp, InputMethodManager.SHOW_IMPLICIT);
    }

    private EditText getOTPField(int position) {
        switch (position) {
            case 1:
                return otp2;
            case 2:
                return otp3;
            case 3:
                return otp4;
            default:
                return otp1;
        }
    }

    private void resetOTPFields() {
        otp1.setText("");
        otp2.setText("");
        otp3.setText("");
        otp4.setText("");
        selectedOTPposition = 0;
        showKeyboard(otp1);
    }
}