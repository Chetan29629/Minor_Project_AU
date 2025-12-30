package com.example.orientataionquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class scoreactivity extends AppCompatActivity {

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score_activity);

        TextView marksObtained = findViewById(R.id.marksObtained);
        Button leaderboardBtn = findViewById(R.id.leaderboardBtn);

        Intent iresult = getIntent();
        int score = iresult.getIntExtra("Score", 0);
        marksObtained.setText("Marks obtained: " + score + " / 15");


        leaderboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent ileader=new Intent(scoreactivity.this,leaderboard.class);
                    startActivity(ileader);
                }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
