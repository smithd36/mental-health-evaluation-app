/**
 * Represents a question in the PHQ-9 questionnaire.
 *
 * This class encapsulates the text of a question.
 *
 * @author Drey Smith
 * @date 10.07.2023
 */
package com.example.phq9;

public class Question {
    private String questionText;

    /**
     * Constructs a new Question object with the provided question text.
     *
     * @param questionText The text of the question.
     */
    public Question(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Gets the text of the question.
     *
     * @return The text of the question.
     */
    public String getQuestionText() {
        return questionText;
    }
}