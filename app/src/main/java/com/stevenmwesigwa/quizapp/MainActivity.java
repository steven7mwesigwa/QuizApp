package com.stevenmwesigwa.quizapp;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static int finalScore = 6;

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
        radioButton.setTextColor(ContextCompat.getColor(this, R.color.colorBlueSuccess));
    }


    private void markCheckBoxButtonWrong(CheckBox checkBoxButton) {
        checkBoxButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    private void markCheckBoxButtonRight(CheckBox checkBoxButton) {
        checkBoxButton.setTextColor(ContextCompat.getColor(this, R.color.colorBlueSuccess));
    }


    private void evaluateEditTextQuestion(LinearLayout question_wrapper, EditText questionOption, String correctAnswer, LinearLayout editTextOptionWrapper) {

        int initialQuizScoreValue = MainActivity.getFinalScore();
        boolean isUserAnswerEmpty = questionOption.getText().toString().trim().length() == 0;
        if (isUserAnswerEmpty || !questionOption.getText().toString().trim().equalsIgnoreCase(correctAnswer)) {
            MainActivity.finalScore--;

        }

        int finalQuizScoreValue = MainActivity.getFinalScore();

        markQuestion(initialQuizScoreValue, finalQuizScoreValue, question_wrapper);

        //Show / Display Correct Answer
        TextView textView = new TextView(this);
        textView.setText(getResources().getString(R.string.qz_correct_answr_edittext) + correctAnswer);
        textView.setTextAppearance(this, R.style.qz_edittext_correct_answer_display);
        removeLayoutSecondChild(editTextOptionWrapper);
        editTextOptionWrapper.addView(textView);

    }

    private void evaluateRadioBtnQuestion(LinearLayout question_wrapper, Set<RadioButton> questionOptions, Set<RadioButton> questionCorrectAnswer) {
        int initialQuizScoreValue = MainActivity.getFinalScore();
        RadioButton correctAnswer = questionCorrectAnswer.iterator().next();

        Optional<RadioButton> studentAnswer = questionOptions.stream().filter(option -> option.isChecked()).findFirst();

        if (!studentAnswer.isPresent()) {
            markRadioButtonRight(correctAnswer);
            MainActivity.finalScore--;
        } else {
            if (studentAnswer.get().equals(correctAnswer)) {
                markRadioButtonRight(correctAnswer);
            } else {
                markRadioButtonWrong(studentAnswer.get());
                markRadioButtonRight(correctAnswer);
                MainActivity.finalScore--;
            }

        }
        int finalQuizScoreValue = MainActivity.getFinalScore();

        markQuestion(initialQuizScoreValue, finalQuizScoreValue, question_wrapper);
    }


    private void removeLayoutSecondChild(LinearLayout question_wrapper) {
        int getQuestionWrapperChildCount = question_wrapper.getChildCount();
        if (getQuestionWrapperChildCount > 1) {
            question_wrapper.removeViewAt(1);
        }
    }

    private void markQuestionAsWrong(LinearLayout question_wrapper) {
        removeLayoutSecondChild(question_wrapper);
        ImageView markQuestionImage = generateImageView(R.drawable.wrong_answer_img);
        question_wrapper.addView(markQuestionImage);
    }

    private void markQuestionAsCorrect(LinearLayout question_wrapper) {
        removeLayoutSecondChild(question_wrapper);
        ImageView markQuestionImage = generateImageView(R.drawable.right_answer_img);
        question_wrapper.addView(markQuestionImage);
    }

    private ImageView generateImageView(int imageResource) {
        ImageView markQuestionImage = new ImageView(this);
        markQuestionImage.setImageResource(imageResource);
        markQuestionImage.setVisibility(View.VISIBLE);
        markQuestionImage.setId(View.generateViewId());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        markQuestionImage.setLayoutParams(params);
        return markQuestionImage;
    }

    private void markQuestion(int initialQuizScoreValue, int finalQuizScoreValue, LinearLayout question_wrapper) {
        if (initialQuizScoreValue > finalQuizScoreValue) {
            markQuestionAsWrong(question_wrapper);
        } else {
            markQuestionAsCorrect(question_wrapper);
        }
    }

    private void evaluateCheckBoxBtnQuestion(LinearLayout question_wrapper, Set<CheckBox> questionOptions, Set<CheckBox> questionCorrectAnswers) {
        int initialQuizScoreValue = MainActivity.getFinalScore();

        Set<CheckBox> studentAnswers = questionOptions.stream().filter(option -> option.isChecked()).collect(Collectors.toSet());

        if (studentAnswers.isEmpty()) {
            questionCorrectAnswers.forEach(option -> markCheckBoxButtonRight(option));
            MainActivity.finalScore--;
        } else {
            Set<CheckBox> wrongAnswers = studentAnswers.stream().filter(option -> !questionCorrectAnswers.contains(option)).collect(Collectors.toSet());
            wrongAnswers.forEach(option -> markCheckBoxButtonWrong(option));
            questionCorrectAnswers.forEach(option -> markCheckBoxButtonRight(option));
            if (!wrongAnswers.isEmpty() || !studentAnswers.equals(questionCorrectAnswers)) {
                MainActivity.finalScore--;
            }
        }

        Set<CheckBox> uncheckedWrongOptions = questionOptions.stream().filter(option -> !option.isChecked() && !questionCorrectAnswers.contains(option) ).collect(Collectors.toSet());
        uncheckedWrongOptions.forEach(option -> option.setTextColor(ContextCompat.getColor(this, R.color.colorBlack)));

        int finalQuizScoreValue = MainActivity.getFinalScore();

        markQuestion(initialQuizScoreValue, finalQuizScoreValue, question_wrapper);
    }


    public void evaluateQuiz(View view) {
        //References to question Wrappers in the xml file
        LinearLayout question1Wrapper = findViewById(R.id.question_1_wrapper);
        LinearLayout question2Wrapper = findViewById(R.id.question_2_wrapper);
        LinearLayout question3Wrapper = findViewById(R.id.question_3_wrapper);
        LinearLayout question4Wrapper = findViewById(R.id.question_4_wrapper);
        LinearLayout question5Wrapper = findViewById(R.id.question_5_wrapper);
        LinearLayout question6Wrapper = findViewById(R.id.question_6_wrapper);

        //References to EditText Option Wrappers in the xml file
        LinearLayout editTextQn5Option1Wrapper = findViewById(R.id.qz_edittext_qn_5_option_1_wrapper);
        LinearLayout editTextQn6Option1Wrapper = findViewById(R.id.qz_edittext_qn_6_option_1_wrapper);

        //References to question options in the xml file
        RadioButton qn1Option1 = findViewById(R.id.qn_1_option_1_txt_1);
        RadioButton qn1Option2 = findViewById(R.id.qn_1_option_2_txt_2);
        CheckBox qn2Option1 = findViewById(R.id.qn_2_option_1_txt_1);
        CheckBox qn2Option2 = findViewById(R.id.qn_2_option_2_txt_2);
        CheckBox qn2Option3 = findViewById(R.id.qn_2_option_3_txt_3);
        CheckBox qn2Option4 = findViewById(R.id.qn_2_option_4_txt_4);
        RadioButton qn3Option1 = findViewById(R.id.qn_3_option_1_txt_1);
        RadioButton qn3Option2 = findViewById(R.id.qn_3_option_2_txt_2);
        RadioButton qn3Option3 = findViewById(R.id.qn_3_option_3_txt_3);
        RadioButton qn3Option4 = findViewById(R.id.qn_3_option_4_txt_4);
        CheckBox qn4Option1 = findViewById(R.id.qn_4_option_1_txt_1);
        CheckBox qn4Option2 = findViewById(R.id.qn_4_option_2_txt_2);
        CheckBox qn4Option3 = findViewById(R.id.qn_4_option_3_txt_3);
        CheckBox qn4Option4 = findViewById(R.id.qn_4_option_4_txt_4);
        CheckBox qn4Option5 = findViewById(R.id.qn_4_option_5_txt_5);
        EditText qn5Option1 = findViewById(R.id.qn_5_option_1_txt_1);
        EditText qn6Option1 = findViewById(R.id.qn_6_option_1_txt_1);


        // List of question options
        Set<RadioButton> qn1Options = new HashSet<>(Arrays.asList(qn1Option1, qn1Option2));
        Set<CheckBox> qn2Options = new HashSet<>(Arrays.asList(qn2Option1, qn2Option2, qn2Option3, qn2Option4));
        Set<RadioButton> qn3Options = new HashSet<>(Arrays.asList(qn3Option1, qn3Option2, qn3Option3, qn3Option4));
        Set<CheckBox> qn4Options = new HashSet<>(Arrays.asList(qn4Option1, qn4Option2, qn4Option3, qn4Option4, qn4Option5));

        //List of question answers
        Set<RadioButton> qn1Answers = new HashSet<>(Arrays.asList(qn1Option2));
        Set<CheckBox> qn2Answers = new HashSet<>(Arrays.asList(qn2Option1, qn2Option4));
        Set<RadioButton> qn3Answers = new HashSet<>(Arrays.asList(qn3Option2));
        Set<CheckBox> qn4Answers = new HashSet<>(Arrays.asList(qn4Option1, qn4Option2, qn4Option4, qn4Option5));
        String qn5Answer = "void";
        String qn6Answer = "3.0";

        evaluateRadioBtnQuestion(question1Wrapper, qn1Options, qn1Answers);
        evaluateCheckBoxBtnQuestion(question2Wrapper, qn2Options, qn2Answers);
        evaluateRadioBtnQuestion(question3Wrapper, qn3Options, qn3Answers);
        evaluateCheckBoxBtnQuestion(question4Wrapper, qn4Options, qn4Answers);
        evaluateEditTextQuestion(question5Wrapper, qn5Option1, qn5Answer, editTextQn5Option1Wrapper);
        evaluateEditTextQuestion(question6Wrapper, qn6Option1, qn6Answer, editTextQn6Option1Wrapper);
// Display dialog box for final score
        popUpFinalScoreAlertBox();
    }

    public void loadWelcomeActivity(View view) {
        MainActivity.setFinalScore(6);
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    private void popUpFinalScoreAlertBox() {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.alertbox_layout, null);
        builder.setView(customLayout);
        TextView quizFinalScore = customLayout.findViewById(R.id.qz_final_score_msg);
        quizFinalScore.setText("" + MainActivity.getFinalScore());

        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
//Reset final score
        MainActivity.setFinalScore(6);
    }


}
