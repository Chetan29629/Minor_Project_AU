package com.example.orientataionquiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class leaderboard extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private List<String> participantsList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard_activity);


        ListView leaderboardListView = findViewById(R.id.leaderboardListView);
        participantsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, participantsList);
        leaderboardListView.setAdapter(adapter);
        FloatingActionButton exitBtn = findViewById(R.id.exitBtn);

        fetchLeaderboardData();

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void fetchLeaderboardData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query to sort by score in descending order and time taken in ascending order
        db.collection("UserScore")
                .orderBy("score", Query.Direction.DESCENDING)
                .orderBy("timeTaken", Query.Direction.ASCENDING)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // Log the error message to get more details
                        Log.e("FirestoreError", "Error while loading data: ", e);
                        Toast.makeText(leaderboard.this, "Error while loading data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("dataerror",e.getMessage());
                        return;
                    }
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        Log.d("FirestoreQuery", "Documents found: " + queryDocumentSnapshots.size());
                    } else {
                        Log.d("FirestoreQuery", "No documents found.");
                    }

                    if (queryDocumentSnapshots != null) {
                        participantsList.clear(); // Clear the list before adding updated data

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("userName");
                            Long score = document.getLong("score");
                            Long timeTaken = document.getLong("timeTaken");

                            // Check for null values and set default values if necessary
                            if (name == null) {
                                name = "Unknown";
                            }
                            if (score == null) {
                                score = 0L; // Default value for score
                            }
                            if (timeTaken == null) {
                                timeTaken = 0L; // Default value for time taken
                            }

                            // Format the data for display
                            String displayText = "Name: " + name + " \nScore: " + score + " \nTime Taken: " + timeTaken + " seconds";
                            participantsList.add(displayText);
                        }

                        adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                    }
                });
    }


}