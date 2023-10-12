/**
 * The SurveyActivity class is meant to navigate the user through
 * each question of the survey, and then calculate the score
 * dynamically and display in an Alert. A user will be given a
 * total score from 0 - 27, based on their responses to each question.
 *_______________________________________
 * Total Score : Severity               |
 * _____________________________________|
 * 1-4 : Minimal Depression             |
 * 5-9 : Mild Depression                |
 * 10-14 : Moderate Depression          |
 * 15-19 : Moderately Severe Depression |
 * 20-27 : Severe Depression            |
 *______________________________________|
 * @author Drey Smith
 * @date 10.07.2023
 */
package com.example.phq9;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {

    // Class level variables
    private SurveyController surveyController;
    private List<Question> surveyQuestions;
    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private int shadedScore = 0;
    private TextView txtSurveyHello;
    private TextView txtSurveyPrompt;
    private RadioGroup answerRadioGroup;
    private RadioButton[] radioChoices = new RadioButton[4];
    private RadioButton[] q10Choices = new RadioButton[4];
    private Button btnNext;
    private Button btnPrevious;
    private ImageButton btnBackToHome;
    private ProgressBar progressBar;
    private TextView txtProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // Initialize UI components
        initializeViews();

        // Initialize Survey Controller and Questions
        initializeSurvey();

        // Load user's name from SharedPreferences and set in greeting message
        loadUserNameFromSharedPreferences();

        // Set click listeners for buttons
        setOnClickListeners();

        // Load the first question in survey
        loadQuestion();
    }

    /**
     * Initializes the UI components by finding and assigning their views.
     */
    private void initializeViews() {
        // Initialize UI components
        txtSurveyHello = findViewById(R.id.txtSurveyHello);
        txtSurveyPrompt = findViewById(R.id.txtSurveyPrompt);
        answerRadioGroup = findViewById(R.id.radioGroupAnswers);

        // loop to assign first 4 radio buttons to an array
        for (int i = 0; i < radioChoices.length; i++) {
            radioChoices[i] = findViewById(getResources().getIdentifier("radioChoice" + i, "id", getPackageName()));
        }

        // loop to assign the last 4 radio buttons to an array
        for (int i = 0; i < q10Choices.length; i++) {
            q10Choices[i] = findViewById(getResources().getIdentifier("q10Choice" + i, "id", getPackageName()));
        }

        btnNext = findViewById(R.id.btnNextQuestion);
        btnPrevious = findViewById(R.id.btnPreviousQuestion);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        progressBar = findViewById(R.id.progressBarSurvey);
        txtProgress = findViewById(R.id.txtProgress);
    }


    /**
     * Initializes the survey controller and loads survey questions.
     */
    private void initializeSurvey() {
        // Initialize the survey controller and questions
        surveyController = new SurveyController(this);
        surveyQuestions = surveyController.loadQuestions();
    }

    /**
     * Loads the user's name from SharedPreferences and displays it in the greeting message.
     */
    private void loadUserNameFromSharedPreferences() {
        // Load user's name from SharedPreferences and set it in the greeting message
        SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userName = preferences.getString("NAME", "");
        txtSurveyHello.setText(getString(R.string.hello) + " " + userName + ".");
    }

    /**
     * Sets click listeners for the Next, Previous, and Back to Home buttons.
     */
    private void setOnClickListeners() {
        // Set click listeners for buttons
        // Next Button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleNextButtonClick();
            }
        });
        // Previous button
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePreviousButtonClick();
            }
        });
        // Back to Home
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBackToHomeClick();
            }
        });
    }

    /**
     * Method to handle the Next button. Validates if answer was checked,
     * if true, maps to next question, or displays score if at end. Increments
     * total score and updates UI elements onClick.
     */
    private void handleNextButtonClick() {
        // Handle the Next button click
        int selectedAnswerIndex = answerRadioGroup.getCheckedRadioButtonId();
        if (selectedAnswerIndex == -1) {
            Toast.makeText(SurveyActivity.this, getString(R.string.validate_answer), Toast.LENGTH_SHORT).show();
            return;
        }

        // Increment score
        totalScore += getScoreForSelectedAnswer(selectedAnswerIndex);


        currentQuestionIndex++;
        loadQuestion();

        // Clear the radio button selection
        answerRadioGroup.clearCheck();

        // Update UI elements and progress bar
        updateUIElements();
    }

    /**
     * Returns the score associated with the selected answer.
     *
     * @param selectedAnswerIndex The index of the selected answer in the RadioGroup.
     * @return The score for the selected answer.
     */
    private int getScoreForSelectedAnswer(int selectedAnswerIndex) {
        // Assign scores based on the selected answer
        if (selectedAnswerIndex == R.id.radioChoice0) {
            return 0;
        } else if (selectedAnswerIndex == R.id.radioChoice1) {
            if(currentQuestionIndex == 8) { // check for shadedScore criteria
                shadedScore += 1;
            }
            return 1;
        } else if (selectedAnswerIndex == R.id.radioChoice2) {
            shadedScore += 1;
            return 2;
        } else if (selectedAnswerIndex == R.id.radioChoice3) {
            shadedScore += 1;
            return 3;
        } else {
            // return default value
            return 0;
        }
    }

    /**
     * Updates UI elements, including the progress bar and button visibility.
     */
    private void updateUIElements() {
        // Update the visibility of Previous button
        btnPrevious.setVisibility(View.VISIBLE);

        // Calculate and update progress bar
        int totalQuestions = surveyQuestions.size();
        int completedQuestions = currentQuestionIndex + 1;
        int progressPercentage = (completedQuestions * 100) / totalQuestions;

        progressBar.setProgress(progressPercentage);
        txtProgress.setText(progressPercentage + "% complete");
    }

    /**
     * Handles the Previous button click. Loads the previous question,
     * updates UI elements.
     */
    private void handlePreviousButtonClick() {
        // Handle the Previous button click
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion();
            updateUIElements();
        }
    }

    /**
     * Handles the Back to Home button click by transitioning to the MainActivity.
     */
    private void handleBackToHomeClick() {
        // Handle the Back to Home button click
        Intent intent = new Intent(SurveyActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Calculates the total score and displays the PHQ-9 result in an alert dialog.
     */
    private void calculateAndShowResult() {
        // combine number of shaded checks with total score
        totalScore += shadedScore;

        // Determine the severity based on the class-level totalScore variable
        String severity = determineSeverity(totalScore);

        // Show the PHQ-9 result in an alert dialog
        showPHQ9Result(totalScore, severity);
    }

    /**
     * Determines the severity based on the total score.
     *
     * @param totalScore The total score obtained in the survey.
     * @return The severity level as a string.
     */
    private String determineSeverity(int totalScore) {
        // Determine severity based on the total score
        if (totalScore >= 1 && totalScore <= 4) {
            return getString(R.string.moderate_depression);
        } else if (totalScore >= 5 && totalScore <= 9) {
            return getString(R.string.mild_depression);
        } else if (totalScore >= 10 && totalScore <= 14) {
            return getString(R.string.moderate_depression);
        } else if (totalScore >= 15 && totalScore <= 19) {
            return getString(R.string.moderately_severe_depression);
        } else if(totalScore != 0) {
            return getString(R.string.severe_depression);
        } else {
            return getString(R.string.no_severity);
        }
    }

    /**
     * Shows the PHQ-9 result in an alert dialog.
     *
     * @param totalScore The total score obtained in the survey.
     * @param severity   The severity level as a string.
     */
    private void showPHQ9Result(int totalScore, String severity) {
        // Show the PHQ-9 result in an alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.phq9_result);
        // decide if "Consider Depressive Disorder" should be included in message
        if (shadedScore >= 4) {
            // if shadedScore qualifies, append "Consider Depressive Disorder" to Alert
            builder.setMessage(getString(R.string.total_score) + " " + totalScore + "\n" + getString(R.string.severity) + " " + severity + "\n" + getString(R.string.consider_disorder));
        } else {
            builder.setMessage(getString(R.string.total_score) + " " + totalScore + "\n" + getString(R.string.severity) + " " + severity);
        }
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
        handleBackToHomeClick(); // Nav back to home after Alert ack
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Loads the next question in the survey or calculates the result if all questions are answered.
     */
    private void loadQuestion() {
        // Check if we are on the last question and totalScore is not 0
        if (currentQuestionIndex == 9 && totalScore != 0) {
            showTenthQuestion();
        } else if (currentQuestionIndex < surveyQuestions.size()) {
            Question currentQuestion = surveyQuestions.get(currentQuestionIndex);
            txtSurveyPrompt.setText(currentQuestion.getQuestionText());
        } else {
            calculateAndShowResult();
        }
    }

    /**
     * Method to update the question and answer choices for the 10th question
     * in the survey.
     */
    private void showTenthQuestion() {
        // Set textview and radio buttons for the 10th question
        txtSurveyPrompt.setText(R.string.question_ten);

        // Adjust answer choices for the 10th question
        hideRadioChoices(radioChoices);
        showRadioChoices(q10Choices);
    }

    /**
     * Method to hide the radio button choices that were used for the
     * first 9 questions.
     *
     * @params RadioButton choices - The array of radio buttons to hide
     */
    private void hideRadioChoices(RadioButton[] choices) {
        for (RadioButton choice : choices) {
            choice.setVisibility(View.GONE);
        }
    }

    /**
     * Method to display the radio button choices that will be
     * used for the 10th question.
     *
     * @params RadioButton choices - The array of radio buttons to show
     */
    private void showRadioChoices(RadioButton[] choices) {
        for (RadioButton choice : choices) {
            choice.setVisibility(View.VISIBLE);
        }
    }
}