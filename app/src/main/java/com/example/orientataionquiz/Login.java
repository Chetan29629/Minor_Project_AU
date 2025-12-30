package com.example.orientataionquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {

    private EditText nameEdtView;
    private EditText courseEdtView;
    private EditText languageEdtText;

    String name;
    String course;
    String language;

    private FirebaseFirestore db;

    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        nameEdtView = findViewById(R.id.nameEdtView);
        courseEdtView = findViewById(R.id.courseEdtView);
        languageEdtText = findViewById(R.id.languageEdtText);
        Button submitForm = findViewById(R.id.submitForm);

        // Submit button click listener
        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEdtView.getText().toString();
                course = courseEdtView.getText().toString();
                language = languageEdtText.getText().toString().toUpperCase().replaceAll("\\s", "").trim();

                // Validate inputs
                if (TextUtils.isEmpty(name) || name.length() < 3) {
                    nameEdtView.requestFocus();
                    nameEdtView.setError("Please Enter Your Valid Name");
                } else if (TextUtils.isEmpty(course) || course.length() < 3) {
                    courseEdtView.requestFocus();
                    courseEdtView.setError("Please Enter Chosen Valid Course");
                } else if (TextUtils.isEmpty(language) || !(language.equalsIgnoreCase("ENGLISH") || language.equalsIgnoreCase("HINDI"))) {
                    languageEdtText.requestFocus();
                    languageEdtText.setError("Please select a valid Language: English or Hindi");
                } else {
                    submitForm.setEnabled(false);
                    // Store data in Firestore
                    addUserData(name, course, language);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Login.this, QuizCode.class);
                            intent.putExtra("language", language);
                            intent.putExtra("name", name);
                            intent.putExtra("course", course);
                            startActivity(intent);
                            finish();
                        }
                    }, 3000);
                }
            }
        });
    }

    private void addUserData(String name, String course, String language) {
        // Create a unique document ID
        userId = db.collection("Users").document().getId();

        // Create a map to store the data
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("course", course);
        userData.put("language", language);

        // Add data to Firestore
        db.collection("Users").document(userId)
                .set(userData, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "User data added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Error adding data: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
