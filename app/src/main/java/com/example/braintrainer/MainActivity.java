package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView timerTextView, answeredTextView, questionTextView, finishedTextView;
    Button goButton, button1, button2, button3, button4, resetButton;
    androidx.gridlayout.widget.GridLayout gridLayout;
    final int timerSeconds = 30;
    int correctAnswered = 0;
    int questionAsked = 0;
    final int lower = 5;
    final int upper = 30;
    int[] answers={0,0,0,0};
    int num1, num2;
    boolean playable = true;

    public void initialize(){
        goButton = findViewById(R.id.goButton);
        resetButton = findViewById(R.id.resetButton);
        gridLayout = findViewById(R.id.gridLayout);
        timerTextView = findViewById(R.id.timerTextView);
        answeredTextView = findViewById(R.id.answeredTextView);
        questionTextView = findViewById(R.id.questionTextView);
        finishedTextView = findViewById(R.id.finishedTextView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
    }

    public void resetGame(View view){
        resetDisplay();
        correctAnswered = 0;
        questionAsked = 0;
    }

    public void resetDisplay(){
        goButton.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.INVISIBLE);
        answeredTextView.setVisibility(View.INVISIBLE);
        questionTextView.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        finishedTextView.setVisibility(View.INVISIBLE);
    }

    public void start(View view){
        playable = true;
        goButton.setVisibility(View.INVISIBLE);
        gridLayout.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.VISIBLE);
        answeredTextView.setVisibility(View.VISIBLE);
        answeredTextView.setText(correctAnswered + "/" + questionAsked);
        questionTextView.setVisibility(View.VISIBLE);

        initializeQuestionAndAnswers();

        new CountDownTimer(timerSeconds*1000, 1000){
            @Override
            public void onTick(long timeTillFinish) {
                timerTextView.setText("0:" + timeTillFinish/1000 + "s");
            }

            @Override
            public void onFinish() {
                resetButton.setVisibility(View.VISIBLE);
                finishedTextView.setVisibility(View.VISIBLE);
                playable = false;
            }
        }.start();
    }

    public int generateRandomInt(int lowerBound, int upperBound){
        Random random = new Random();
        return lowerBound + random.nextInt(upperBound+1); //5 to 9
    }

    public void randomizePositionOfAnswers(){
        Random random = new Random();
        for(int i=0; i<answers.length; ++i){
            int randomPosition = random.nextInt(answers.length);
            int temp = answers[i];
            answers[i] = answers[randomPosition];
            answers[randomPosition] = temp;
        }
    }

    public void initializeQuestionAndAnswers(){
        num1 = generateRandomInt(lower, upper);
        num2 = generateRandomInt(lower, upper);
        int correctAnswer = num1 + num2;

        ArrayList<Integer>falseAnswers = new ArrayList<Integer>();

        falseAnswers.add(correctAnswer);
        int falseAnswer1 = generateRandomInt(lower,num1+num2);
        int falseAnswer2 = generateRandomInt(lower,num1+num2);
        int falseAnswer3 = generateRandomInt(lower,num1+num2);

        while(falseAnswers.contains(falseAnswer1)){
            falseAnswer1 = generateRandomInt(lower,num1+num2);
        }
        falseAnswers.add(falseAnswer1);

        while(falseAnswers.contains(falseAnswer2)){
            falseAnswer2 = generateRandomInt(lower,num1+num2);
        }
        falseAnswers.add(falseAnswer2);

        while(falseAnswers.contains(falseAnswer3)){
            falseAnswer3 = generateRandomInt(lower,num1+num2);
        }
        falseAnswers.add(falseAnswer3);

        answers[0] = correctAnswer;
        answers[1] = falseAnswer1;
        answers[2] = falseAnswer2;
        answers[3] = falseAnswer3;
        randomizePositionOfAnswers();

        questionTextView.setText(num1 + " + " + num2 + " = ?");
        button1.setText(String.valueOf(answers[0]));
        button2.setText(String.valueOf(answers[1]));
        button3.setText(String.valueOf(answers[2]));
        button4.setText(String.valueOf(answers[3]));
    }

    public void updateAnswer(View view){
        if(playable){
            Button b = (Button)view;
            int answer = Integer.parseInt(b.getText().toString());

            if(answer == num1 + num2){
                //update score
                correctAnswered++;
            }
            questionAsked++;
            answeredTextView.setText(correctAnswered + "/" + questionAsked);
            initializeQuestionAndAnswers();
        }
        else{
            Toast.makeText(this, "Please start a new game!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        resetDisplay();
    }
}
