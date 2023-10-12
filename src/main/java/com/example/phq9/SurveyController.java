/**
 * Manages the PHQ-9 survey, providing methods for loading survey questions.
 *
 * This controller class is responsible for loading the survey questions from the
 * application's resources and providing them as a list of Question objects.
 *
 * @author Drey Smith
 * @date 10.07.2023
 */
package com.example.phq9;

import android.content.Context;
import android.content.res.Resources;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class responsible for managing the PHQ-9 survey.
 *
 * This class provides methods for loading survey questions.
 *
 * @author Your Name
 * @date Date of creation
 */
public class SurveyController {
    private Context context;

    /**
     * Constructs a new SurveyController object with the provided context.
     *
     * @param context The application context.
     */
    public SurveyController(Context context) {
        this.context = context;
    }

    /**
     * Loads the list of survey questions from the application's resources.
     *
     * @return A list of Question objects representing the survey questions.
     */
    public List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        Resources resources = context.getResources();

        String[] questionTexts = resources.getStringArray(R.array.survey_questions);

        for (String questionText : questionTexts) {
            questions.add(new Question(questionText));
        }

        return questions;
    }
}