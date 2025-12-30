package com.example.orientataionquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Quiz extends AppCompatActivity {
    private RecyclerView recyclerViewId;
    private TextView remainingQues, remainingTime;
    private TextView questions;
    private RadioGroup radiogrp;
    private int currentPosition = 0;
    private CountDownTimer questionTimer;

    private RadioButton opt1, opt2, opt3, opt4;
    private int score = 0;
    private int quesIndex = 0;
    private int quesNum = 14;
    private FirebaseFirestore db;
    String name, language, course;
    private long quizStartTime, quizEndTime, timeTaken, timeTakenInSeconds;

    // English Questions and Options
    private final String[] ENGLISH_QUESTIONS = {
            "Which indian leader famously declared the \"swaraj is my birth right \"?",
            "Which indian mathematician was known for his extraordinary contributions to mathematical analysis,number theory,and continued fractions?",
            "Who was the first woman to become the chief minister of an indian state?",
            "In which year was Indian Space Research Organisation (ISRO) established ?",
            "Who composed the national song of India ?",
            "Which ancient civilization is known for its advancement urban planning including the cities of Harappa and Mohanjodaro ?",
            "Which ancient Indian university considered one of the oldest in world that is located in Bihar ?",
            "In which year 'Bharat Jano' movement officially launched ?",
            "How many districts in Rajasthan are currently present ?",
            "Who designed the national flag of india ?",
            "Pakistan get  separated  from India  on which date?",
            "Which mosque, located in Mecca, is considered the holiest site in Islam and is the focal point of the Hajj pilgrimage ?",
            "What are the names of the two small moons that orbits  Mars ?",
            "What is the full form of ALU ?",
            "Between given below memories which  one of the memory is fastest memory ?"


    };

    private final String[][] ENGLISH_OPTIONS = {
            {"Jawaharlal Nehru", "Subhas Chandra Bose", "Bal Gangadhar Tilak", "Mahatma Gandhi"},
            {"Satyendra Nath Bose", "Srinivasa Ramanujan", "Aryabhata", "C.V. Raman"},
            {"Indira Gandhi", "Sarojini Naidu", "Suchita Kripalani", "Pratibha Patil"},
            {"1960", "1965", "1969", "1972"},
            {"Rabindranath Tagore", "Bankim Chandra Chattopadhyay", "Kavi Pradeep", "Pankaj Mullick"},
            {"Vedic Civilization", "Indus Valley Civilization", "Harappa Civilization", "Both b and c"},
            {"Nalanda University", "Takshashila University", "Apex University", "Kashi University"},
            {"1925", "1980", "1955", "1942"},
            {"20", "25", "50", "40"},
            {"Pingali Venkayya", "Sardar Patel", "Jawaharlal Nehru", "B.R. Ambedkar"},
            {"14 August 1947","15 August 1947","26 January 1950","16 August 1947"},
            {"Al-Masjid an-Nabawi","Masjid al-Haram","The jama masjid","Sultan Ahmed Mosque"},
            {"Phobos and Deimos", "Titan and Rhea", "Europa and Ganymede", "Ariel and Umbriel"},
            {"Arithmetic Logic Unit", "Array Logic Unit", "Advanced Logic Unit", "Application Logic Unit"},
            {"Cache Memory", "RAM", "Hard Disk Drive (HDD)", "Solid State Drive (SSD)"}
    };

    private final String[] ENGLISH_CORRECT_ANSWERS = {
            "Bal Gangadhar Tilak", "Srinivasa Ramanujan", "Suchita Kripalani", "1969", "Bankim Chandra Chattopadhyay", "Both b and c", "Nalanda University", "1942", "50", "Pingali Venkayya","14 August 1947","Masjid al-Haram","Phobos and Deimos","Arithmetic Logic Unit","Cache Memory"
    };

    // Hindi Questions and Options
    private final String[] HINDI_QUESTIONS = {
            "किस भारतीय नेता ने प्रसिद्ध रूप से 'स्वराज मेरा जन्मसिद्ध अधिकार है' घोषित किया?",
            "कौन सा भारतीय गणितज्ञ गणितीय विश्लेषण, संख्या सिद्धांत, और निरंतर अंशों में अपने असाधारण योगदान के लिए जाना जाता है?",
            "कौन भारतीय राज्य की पहली महिला मुख्यमंत्री बनीं?",
            "भारतीय अंतरिक्ष अनुसंधान संगठन (ISRO) की स्थापना किस वर्ष हुई थी?",
            "भारत के राष्ट्रीय गीत की रचना किसने की?",
            "कौन सी प्राचीन सभ्यता अपनी उन्नत शहरी योजना के लिए जानी जाती है, जिसमें हड़प्पा और मोहनजोदड़ो शामिल हैं?",
            "कौन सा प्राचीन भारतीय विश्वविद्यालय, जो विश्व में सबसे पुराना माना जाता है, बिहार में स्थित है?",
            "'भारत छोडो' आंदोलन किस वर्ष शुरू हुआ था?",
            "राजस्थान में वर्तमान में कितनी जिलों की संख्या है?",
            "भारत के राष्ट्रीय ध्वज को किसने डिजाइन किया?",
            "पाकिस्तान भारत से किस तारीख को अलग हुआ?",
            "मक्का में स्थित कौन सी मस्जिद इस्लाम का सबसे पवित्र स्थल मानी जाती है और हज की यात्रा का केंद्र है?",
            "मंगल ग्रह के छोटे चंद्रमाओं के क्या नाम हैं?",
            "ALU का पूरा नाम क्या है?",
            "नीचे दिए गए मेमोरी में से सबसे तेज मेमोरी कौन सी है?"
    };

    private final String[][] HINDI_OPTIONS = {
            {"बाल गंगाधर तिलक", "सुभाष चंद्र बोस", "जवाहरलाल नेहरू", "महात्मा गांधी"},
            {"सत्येंद्र नाथ बोस", "स्रीनिवास रामानुजन", "आर्यभट्ट", "सी.वी. रमन"},
            {"इंदिरा गांधी", "सारोजिनी नायडू", "सुचेता कृपलानी", "प्रणब मुखर्जी"},
            {"1960", "1965", "1969", "1972"},
            {"रवींद्रनाथ ठाकुर", "बंकिम चंद्र चट्टोपाध्याय", "कवि प्रदीप", "पंकज मलिक"},
            {"वैदिक सभ्यता", "सिंधु घाटी सभ्यता", "हड़प्पा सभ्यता", "उपरोक्त दोनों"},
            {"नालंदा विश्वविद्यालय", "तक्षशिला विश्वविद्यालय", "एपेक्स विश्वविद्यालय", "काशी विश्वविद्यालय"},
            {"1925", "1980", "1955", "1942"},
            {"20", "25", "50", "40"},
            {"पिंगली वेंकैया", "सर्दार पटेल", "जवाहरलाल नेहरू", "भीमराव अंबेडकर"},
            {"14 अगस्त 1947", "15 अगस्त 1947", "26 जनवरी 1950", "16 अगस्त 1947"},
            {"अल-मस्जिद अन-नबवी", "मस्जिद अल-हराम", "जामा मस्जिद", "सुलतान अहमद मस्जिद"},
            {"फोबोस और डाइमोस", "टाइटन और रिया", "यूरोपा और गैनीमेड", "एरियल और उमब्रील"},
            {"अर्थमेटिक लॉजिक यूनिट", "एरे लॉजिक यूनिट", "एडवांस्ड लॉजिक यूनिट", "एप्लिकेशन लॉजिक यूनिट"},
            {"कैश मेमोरी (Cache Memory)", "रैम (RAM)", "हार्ड डिस्क ड्राइव (HDD)", "सॉलिड स्टेट ड्राइव (SSD)"}
    };

    private final String[] HINDI_CORRECT_ANSWERS = {
            "बाल गंगाधर तिलक", "स्रीनिवास रामानुजन", "सुचेता कृपलानी", "1969", "बंकिम चंद्र चट्टोपाध्याय", "उपरोक्त दोनों", "नालंदा विश्वविद्यालय", "1942", "50", "पिंगली वेंकैया", "14 अगस्त 1947", "मस्जिद अल-हराम", "फोबोस और डाइमोस", "अर्थमेटिक लॉजिक यूनिट", "कैश मेमोरी (Cache Memory)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_activity);
        quizStartTime = System.currentTimeMillis();
        recyclerViewId = findViewById(R.id.recyclerViewId);
        remainingQues = findViewById(R.id.remainingQues);
        questions = findViewById(R.id.questions);
        radiogrp = findViewById(R.id.radiogrp);
        opt1 = findViewById(R.id.opt1);
        opt2 = findViewById(R.id.opt2);
        opt3 = findViewById(R.id.opt3);
        opt4 = findViewById(R.id.opt4);
        FloatingActionButton nextBtn = findViewById(R.id.nextBtn);
        remainingTime = findViewById(R.id.remainingTime);
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewId.setLayoutManager(layoutManager);


        // ... (populate quotesList as in your code)
        ArrayList<imgsModel> quotesList = new ArrayList<>();
        quotesList.add(new imgsModel(R.drawable.img));
        quotesList.add(new imgsModel(R.drawable.img_1));
        quotesList.add(new imgsModel(R.drawable.img_2));
        quotesList.add(new imgsModel(R.drawable.img_3));
        quotesList.add(new imgsModel(R.drawable.img_4));
        quotesList.add(new imgsModel(R.drawable.img_5));
        quotesList.add(new imgsModel(R.drawable.img_6));
        quotesList.add(new imgsModel(R.drawable.img_7));
        quotesList.add(new imgsModel(R.drawable.img_8));
        quotesList.add(new imgsModel(R.drawable.img_9));
        quotesList.add(new imgsModel(R.drawable.img_10));
        quotesList.add(new imgsModel(R.drawable.img_11));
        quotesList.add(new imgsModel(R.drawable.img_12));
        quotesList.add(new imgsModel(R.drawable.img_13));
        quotesList.add(new imgsModel(R.drawable.img_14));



        ItemsAdapter adapter = new ItemsAdapter(this, quotesList);
        recyclerViewId.setAdapter(adapter);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewId);

        Intent inext = getIntent();
        name = inext.getStringExtra("name");
        course = inext.getStringExtra("course");
        language = inext.getStringExtra("language");

        loadQuestion();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int buttonId = radiogrp.getCheckedRadioButtonId();
                if (buttonId == -1) {
                    Toast.makeText(Quiz.this, "Please select an option", Toast.LENGTH_SHORT).show();
                } else {
                    checkAnswer();
                    quesIndex++;
                    quesNum--;
                    if (quesIndex < ENGLISH_QUESTIONS.length) {
                        loadQuestion();
                        scrollToNextItem();
                    } else {
                        endQuiz();
                    }
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handle back press and home button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!isFinishing()) {
            showExitConfirmationDialog();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!isFinishing()) {
            showExitConfirmationDialog();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (questionTimer != null) {
            questionTimer.cancel();
        }
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Quiz")
                .setMessage("Are you sure you want to exit? Your quiz data will be lost.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addScoreToFirebase();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }





    private void loadQuestion() {
        radiogrp.clearCheck();
        remainingQues.setText("Remaining Question : " + quesNum);

        if ("HINDI".equals(language)) {
            questions.setText(HINDI_QUESTIONS[quesIndex]);
            opt1.setText(HINDI_OPTIONS[quesIndex][0]);
            opt2.setText(HINDI_OPTIONS[quesIndex][1]);
            opt3.setText(HINDI_OPTIONS[quesIndex][2]);
            opt4.setText(HINDI_OPTIONS[quesIndex][3]);
        } else {
            questions.setText(ENGLISH_QUESTIONS[quesIndex]);
            opt1.setText(ENGLISH_OPTIONS[quesIndex][0]);
            opt2.setText(ENGLISH_OPTIONS[quesIndex][1]);
            opt3.setText(ENGLISH_OPTIONS[quesIndex][2]);
            opt4.setText(ENGLISH_OPTIONS[quesIndex][3]);
        }
        startQuestionTimer();
    }

    private void startQuestionTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
        }

        questionTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isFinishing() && remainingTime != null) {
                    remainingTime.setText(millisUntilFinished / 1000 + " seconds");
                }
            }

            @Override
            public void onFinish() {
                checkAnswer();
                quesIndex++;
                quesNum--;
                if (quesIndex < ENGLISH_QUESTIONS.length) {
                    loadQuestion();
                    scrollToNextItem();
                } else {
                    quizEndTime = System.currentTimeMillis();
                    endQuiz();
                }
            }
        }.start();
    }

    private void navigateToScoreActivity() {
        Intent iresult = new Intent(Quiz.this, scoreactivity.class);
        iresult.putExtra("Score", score);
        iresult.putExtra("timeTaken", timeTaken);
        startActivity(iresult);
        finish();
    }

    private void endQuiz() {
        if (quizEndTime == 0) {
            quizEndTime = System.currentTimeMillis();
        }
        addScoreToFirebase();
        navigateToScoreActivity();
    }

    private void checkAnswer() {
        int selectedOpt = radiogrp.getCheckedRadioButtonId();
        if (selectedOpt != -1) {
            RadioButton selectedButton = findViewById(selectedOpt);
            String selectedAnswer = selectedButton.getText().toString();
            String correctAnswer = "HINDI".equals(language) ? HINDI_CORRECT_ANSWERS[quesIndex] : ENGLISH_CORRECT_ANSWERS[quesIndex];
            if (selectedAnswer.equals(correctAnswer)) {
                score++;
            }
        }
    }

    private void scrollToNextItem() {
        int totalItems = Objects.requireNonNull(recyclerViewId.getAdapter()).getItemCount();

        if (currentPosition < totalItems - 1) {
            currentPosition++;
        } else {
            currentPosition = 0;
        }

        recyclerViewId.smoothScrollToPosition(currentPosition);
    }

    private void addScoreToFirebase() {
        String userId = db.collection("UserScore").document().getId();
        timeTaken = (quizEndTime - quizStartTime);
        timeTakenInSeconds = (timeTaken / 1000);

        Map<String, Object> userScore = new HashMap<>();
        userScore.put("userName", name);
        userScore.put("course", course);
        userScore.put("score", score);
        userScore.put("timeTaken", timeTakenInSeconds);

        db.collection("UserScore").document(userId).set(userScore, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firebase", "Score successfully added.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firebase", "Error adding score: ", e);
                        // You might want to handle the failure case here, such as retrying or notifying the user.
                    }
                });
    }
}