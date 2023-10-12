/**
 * The MainActivity class serves as the entry point of the PHQ-9 survey application.
 * It allows users to input their personal information and start the survey.
 *
 * @author Drey Smith
 * @date 10.07.2023
 */
package com.example.phq9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Class level variables
    private EditText edtName;
    private EditText edtID;
    private EditText edtDate;
    private Button btnBegin;
    private SharedPreferences preferences;

    /**
     * Called when the activity is first created. This method initializes the UI components,
     * sets up SharedPreferences for storing user data, and attaches a click listener to the
     * "Begin" button for handling user input validation and navigation to the survey activity.
     *
     * @param savedInstanceState A Bundle containing saved state information, which may be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeViews();

        // Initialize SharedPreferences for storing user data
        initializeSharedPreferences();

        // Set click listener for the "Begin" button
        setButtonClickListeners();
    }

    /**
     * Initializes the UI components by finding and assigning their views.
     */
    private void initializeViews() {
        edtName = findViewById(R.id.edtName);
        edtID = findViewById(R.id.edtID);
        edtDate = findViewById(R.id.edtDate);
        btnBegin = findViewById(R.id.btnBegin);
    }

    /**
     * Initializes SharedPreferences for storing user data
     * that will be relayed in the SurveyActivity.
     */
    private void initializeSharedPreferences() {
        preferences = getSharedPreferences("UserData", MODE_PRIVATE);
    }

    /**
     * Sets a click listener for the "Begin" button.
     * When clicked, it calls the handleBeginButtonClick() method.
     */
    private void setButtonClickListeners() {
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBeginButtonClick();
            }
        });
    }

    /**
     * Handles the "Begin" button click. Validates user input and navigates to the survey activity
     * if input is valid.
     */
    private void handleBeginButtonClick() {
        // get entered input for all fields
        String name = edtName.getText().toString().trim();
        String id = edtID.getText().toString().trim();
        String date = edtDate.getText().toString().trim();

        // save to preferences and nav to survey
        if (validateInput(name, id, date)) {
            saveUserDataToSharedPreferences(name, id, date);
            navigateToSurveyActivity();
        }
    }

    /**
     * Validates user input fields (name, ID, and date).
     *
     * @param name The user's name.
     * @param id   The user's ID.
     * @param date The date that the form is being accessed.
     * @return True if input is valid; otherwise, false.
     */
    private boolean validateInput(String name, String id, String date) {
        // if any empty values throw error
        if (name.isEmpty() || id.isEmpty() || date.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.validate_input, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true; // else validate
    }

    /**
     * Saves user data (name, ID, and date) to SharedPreferences.
     *
     * @param name The user's name.
     * @param id   The user's ID.
     * @param date The date that the form is being accessed.
     */
    private void saveUserDataToSharedPreferences(String name, String id, String date) {
        SharedPreferences.Editor editor = preferences.edit();

        // key,values
        editor.putString("NAME", name);
        editor.putString("ID", id);
        editor.putString("DATE", date);
        editor.apply(); // Commit changes
    }

    /**
     * Navigates to the SurveyActivity to start the PHQ-9 survey
     * then closes the MainActivity.
     */
    private void navigateToSurveyActivity() {
        // open SurveyActivity and close MainActivity
        Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
        startActivity(intent);
        finish();
    }
}