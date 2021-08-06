package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.icu.text.MessageFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.example.myapplication.data.Repository;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.model.Question;
import com.example.myapplication.model.Score;
import com.example.myapplication.util.Prefs;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int scoreCounter = 0;
    private Score score;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        score = new Score();
        prefs = new Prefs(MainActivity.this);

        //Retrieve the last state
        currentQuestionIndex = prefs.getState();

        //setting the High_score text
        binding.highScoreValue.setText(String.valueOf(prefs.getHighestScore()));
        //setting the Currents_core text
        binding.scoreText.setText(MessageFormat.format("{0}pts.",
                String.valueOf(score.getScore())));

        //all the question should come in as an ArrayList type int
        questionList = new Repository().getQuestions(questionArrayList -> {


                    // putting the question into the question_textview
                    binding.questionTextview.setText(questionArrayList
                            .get(currentQuestionIndex).getAnswer());
                    //Log.d("Volley", "onCreate: "+ questionArrayList)

                    updateCounter(questionArrayList);


                }
        );
        //fastfoward button
        binding.fastFowardButton.setOnClickListener(v -> {
            currentQuestionIndex = (currentQuestionIndex + 10) % questionList.size();
            updateQuestion();
        });

        //rewind button
        binding.rewindButton.setOnClickListener(v -> {
            if (currentQuestionIndex >= 10) {
                currentQuestionIndex = (currentQuestionIndex - 10) % questionList.size();
            } else {
                //reset the index to 0 if < 10
                currentQuestionIndex = 0;
            }
            updateQuestion();
        });

        //next button
        binding.nextButton.setOnClickListener(v -> getNextQuestion());

        //reset button
        binding.resetButton.setOnClickListener(v -> {
            //reset the index to 0
            currentQuestionIndex = 0;
            updateQuestion();
        });

        //true button
        binding.trueButton.setOnClickListener(v -> checkAnswer(true));

        //false button
        binding.falseButton.setOnClickListener(v -> checkAnswer(false));

    }

    //saving high scores and states
    @Override
    protected void onPause() {
        prefs.saveHighestScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        //Log.d("score", "onPause: saving score " + prefs.getHighestScore());
        //Log.d("original", "onPause: Saving state" + prefs.getState());
        super.onPause();

    }

    private void IncreaseCurrentQuestionIndex() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
    }

    private void getNextQuestion() {
        IncreaseCurrentQuestionIndex();
        updateQuestion();
    }

    //checking Answer is right
    private void checkAnswer(boolean userChoseCorrect) {

        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMessageId;

        //after checking the answer is true or false display a message, play an animation
        // when the animation ends will move onto a new question
        // and addPoints or deductPoints()
        if (userChoseCorrect == answer) {

            snackMessageId = R.string.correct_answer;
            fadeAnimation();
            updateQuestion();
            addPoints();

        } else {

            snackMessageId = R.string.incorrect_answer;
            shakeAnimation();
            updateQuestion();
            deductPoints();

        }
        Snackbar.make(binding.questionCardview, snackMessageId, Snackbar.LENGTH_SHORT).show();
    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.textViewOutOf.setText(String.format(getString(R.string.text_formatted),
                currentQuestionIndex, questionArrayList.size()));
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.questionTextview.setText(question);
        updateCounter((ArrayList<Question>) questionList);
    }

    //when answer False Animation
    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);

        binding.questionCardview.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //when answer True Animation
    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        //after finish u can attach it to a view or a widget
        binding.questionCardview.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //current_score[+]
    private void addPoints() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        binding.scoreText.setText(MessageFormat.format("{0}pts.",
                String.valueOf(score.getScore())));
    }
    //current_score[-]
    private void deductPoints() {

        if (scoreCounter > 0) {
            scoreCounter -= 100;
            score.setScore(scoreCounter);
            binding.scoreText.setText(MessageFormat.format("{0}pts.",
                    String.valueOf(score.getScore())));
        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
        }


    }


}