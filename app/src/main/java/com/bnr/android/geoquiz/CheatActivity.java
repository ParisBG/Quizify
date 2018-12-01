package com.bnr.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "anser_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "anser_is_shown";
    private static final String SAVE_CHEAT_BOOLEAN = "save_cheat_boolean";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private boolean hasStarted;
    private boolean displayCheater;
    private int position;
    private Button mShowAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        position = getIntent().getIntExtra("position",position);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        if (savedInstanceState != null){
            displayCheater =
                    savedInstanceState.getBoolean(SAVE_CHEAT_BOOLEAN, true);
            position =
                    savedInstanceState.getInt("position", position);

        }

        TextView api = findViewById(R.id.api_level_textView);
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.api_Level));
        sb.append(" ");
        sb.append(Build.VERSION.SDK_INT);
        api.setText(sb);
        mAnswerTextView = findViewById(R.id.answer_textView);
        mShowAnswerButton = findViewById(R.id.show_answer_button);
        setAnswerShownResult(true);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        //savedInstanceState.putBoolean(SAVE_CHEAT_BOOLEAN,displayCheater);
        savedInstanceState.putBoolean("displayCheater",displayCheater);
        savedInstanceState.putInt("position",position);

    }
    public void onClickShowAnswer(View view){
        QuizActivity.setCheated(position);
        theResults();
        displayCheater = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int cx = mShowAnswerButton.getWidth() / 2;
            int cy = mShowAnswerButton.getHeight() / 2;
            float radius = mShowAnswerButton.getWidth();
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(
                            mShowAnswerButton, cx, cy, radius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    mShowAnswerButton.setVisibility(View.INVISIBLE);

                }
            });
            anim.start();
        } else {
            mShowAnswerButton.setVisibility(View.INVISIBLE);
        }
    }



    public void theResults(){
        int trueOrFalse = mAnswerIsTrue
                ? R.string.true_button
                : R.string.false_button;

        mAnswerTextView.setText(trueOrFalse);

    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);

    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (displayCheater) {
            theResults();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
