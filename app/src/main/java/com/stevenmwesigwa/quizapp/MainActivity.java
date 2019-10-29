package com.stevenmwesigwa.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {
    private enum OptionsType {
        RADIO, CHECKBOX, OPEN_ANSWER
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static int finalScore = 6;

    //References to questions in the xml file
    private TextView question1 = findViewById(R.id.question_1);
    private TextView question2 = findViewById(R.id.question_2);
    private TextView question3 = findViewById(R.id.question_3);
    private TextView question4 = findViewById(R.id.question_4);
    private TextView question5 = findViewById(R.id.question_5);
    private TextView question6 = findViewById(R.id.question_6);
    //References to question options in the xml file
    private RadioButton qn1Option1 = findViewById(R.id.qn_1_option_1_txt_1);
    private RadioButton qn1Option2 = findViewById(R.id.qn_1_option_2_txt_2);
    private CheckBox qn2Option1 = findViewById(R.id.qn_2_option_1_txt_1);
    private CheckBox qn2Option2 = findViewById(R.id.qn_2_option_2_txt_2);
    private CheckBox qn2Option3 = findViewById(R.id.qn_2_option_3_txt_3);
    private CheckBox qn2Option4 = findViewById(R.id.qn_2_option_4_txt_4);
    private RadioButton qn3Option1 = findViewById(R.id.qn_3_option_1_txt_1);
    private RadioButton qn3Option2 = findViewById(R.id.qn_3_option_2_txt_2);
    private RadioButton qn3Option3 = findViewById(R.id.qn_3_option_3_txt_3);
    private RadioButton qn3Option4 = findViewById(R.id.qn_3_option_4_txt_4);
    private CheckBox qn4Option1 = findViewById(R.id.qn_4_option_1_txt_1);
    private CheckBox qn4Option2 = findViewById(R.id.qn_4_option_2_txt_2);
    private CheckBox qn4Option3 = findViewById(R.id.qn_4_option_3_txt_3);
    private CheckBox qn4Option4 = findViewById(R.id.qn_4_option_4_txt_4);
    private CheckBox qn4Option5 = findViewById(R.id.qn_4_option_5_txt_5);
    private EditText qn5Option1 = findViewById(R.id.qn_5_option_1_txt_1);
    private EditText qn6Option1 = findViewById(R.id.qn_6_option_1_txt_1);


    public static int getFinalScore() {
        return finalScore;
    }

    public static void setFinalScore(int finalScore) {
        MainActivity.finalScore = finalScore;
    }

    private void markRadioButtonWrong(RadioButton radioButton) {
        radioButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void markRadioButtonRight(RadioButton radioButton) {
        radioButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }


    private void markCheckBoxButtonWrong(CheckBox checkBoxButton) {
        checkBoxButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void markCheckBoxButtonRight(CheckBox checkBoxButton) {
        checkBoxButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }


    private void evaluateRadioBtnQuestion(Set<RadioButton> questionOptions, Set<RadioButton> questionCorrectAnswer) {

        RadioButton correctAnswer = questionCorrectAnswer.iterator().next();
        ;
        Optional<RadioButton> studentAnswer = questionOptions.stream().filter(option -> option.isChecked()).findFirst();

        if (!studentAnswer.isPresent() || !studentAnswer.get().equals(correctAnswer)) {
            markRadioButtonRight(correctAnswer);
            MainActivity.finalScore--;
        } else {
            markRadioButtonWrong(studentAnswer.get());
            markRadioButtonRight(correctAnswer);
        }
    }

    private void evaluateCheckBoxBtnQuestion(Set<CheckBox> questionOptions, Set<CheckBox> questionCorrectAnswers) {

        Set<CheckBox> studentAnswers = questionOptions.stream().filter(option -> option.isChecked()).collect(Collectors.toSet());

        if (studentAnswers.isEmpty()) {
            questionCorrectAnswers.forEach(option -> markCheckBoxButtonRight(option));
            MainActivity.finalScore--;
        } else {
            Set<CheckBox> wrongAnswers = studentAnswers.stream().filter(option -> !questionCorrectAnswers.contains(option)).collect(Collectors.toSet());
            wrongAnswers.forEach(option -> markCheckBoxButtonWrong(option));
            questionCorrectAnswers.forEach(option -> markCheckBoxButtonRight(option));
            if (!wrongAnswers.isEmpty()) {
                MainActivity.finalScore--;
            }
        }
    }


    public void evaluateQuiz(View view) {
        // List of question options
        Set<RadioButton> qn1Options = new HashSet<>(Arrays.asList(qn1Option1, qn1Option2));
        Set<CheckBox> qn2Options = new HashSet<>(Arrays.asList(qn2Option1, qn2Option2, qn2Option3, qn2Option4));
        Set<RadioButton> qn3Options = new HashSet<>(Arrays.asList(qn3Option1, qn3Option2, qn3Option3, qn3Option4));
        Set<CheckBox> qn4Options = new HashSet<>(Arrays.asList(qn4Option1, qn4Option2, qn4Option3, qn4Option4, qn4Option5));

        //List of question answers
        Set<RadioButton> qn1Answers = new HashSet<>(Arrays.asList(qn1Option2));
        Set<CheckBox> qn2Answers = new HashSet<>(Arrays.asList(qn2Option1, qn2Option4));
        Set<RadioButton> qn3Answers = new HashSet<>(Arrays.asList(qn3Option1, qn3Option2, qn3Option3, qn3Option4));
        Set<CheckBox> qn4Answers = new HashSet<>(Arrays.asList(qn4Option1, qn4Option2, qn4Option3, qn4Option4, qn4Option5));

        evaluateRadioBtnQuestion(qn1Options, qn1Answers);
        evaluateCheckBoxBtnQuestion(qn2Options, qn2Answers);
        evaluateRadioBtnQuestion(qn3Options, qn3Answers);
        evaluateCheckBoxBtnQuestion(qn4Options, qn4Answers);
    }

}
