package com.example.orientataionquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_activity);
        ImageView orientationLogo = findViewById(R.id.orientationLogo);

        Animation animate= AnimationUtils.loadAnimation(MainActivity.this,R.anim.universitylogo);
        orientationLogo.startAnimation(animate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent inext=new Intent(MainActivity.this,Login.class);
                startActivity(inext);
                finish();
            }
        },5000);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

}