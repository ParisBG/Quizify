package com.bnr.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.util.Log;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private ImageButton mPrevButton;
    private boolean mIsCheater;
    private int mCheatCount;
    private Button cheatButton;
    private TextView cheatCounterView;

    private static final String EXTRA_ANSWER_IS_TRUE = "anser_is_true";
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String SAVE_CHEAT_BOOLEAN = "save_cheat_boolean";

    private static Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_text,
                    false),
            new Question(R.string.question_nba,
                    true),
            new Question(R.string.question_nfl,
                    true),
            new Question(R.string.question_rap,
                    false),
            new Question(R.string.question_feb,
                    false),
            new Question(R.string.question_bible,
                    false)
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
//            Log.d(TAG,"Updating question Text",new Exception());

        cheatButton = findViewById(R.id.cheat_button);
        cheatCounterView = findViewById(R.id.cheat_counter);

        mCheatCount = 3;

        if (savedInstanceState != null) {
            mIsCheater =
                    savedInstanceState.getBoolean(SAVE_CHEAT_BOOLEAN, false);
            mCurrentIndex =
                    savedInstanceState.getInt(KEY_INDEX, 0);
            mCheatCount =
                    savedInstanceState.getInt("cheatCount", 3);

        }

        mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                } else {
                    mCurrentIndex = (mCurrentIndex -1) % mQuestionBank.length;
                }

                updateQuestion();

            }

        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex =
                        (mCurrentIndex +1) % mQuestionBank.length;

                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex++;

                if (mCurrentIndex == mQuestionBank.length) {
                    mCurrentIndex = 0;
                }

                mIsCheater = false;
                updateQuestion();



            }
        });

        updateQuestion();
    }


    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();

        mQuestionTextView.setText(question);

    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        boolean hasCheated = mQuestionBank[mCurrentIndex].hasHeCheated();
        int messageResId = 0;

        if (hasCheated){
            messageResId = R.string.judgement_toast;
        } else {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume called");

        cheatCounterView.setText(String.valueOf(mCheatCount));

        if (mCheatCount == 0) {
            cheatButton.setVisibility(View.INVISIBLE);
        } else {
            cheatButton.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");

        }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(SAVE_CHEAT_BOOLEAN, mIsCheater);
        savedInstanceState.putInt("cheatCount", mCheatCount);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    public static Intent newIntent(Context context,
                                   boolean answerIsTrue, int position){
        Intent cheatIntent = new Intent(context,CheatActivity.class);
        cheatIntent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        cheatIntent.putExtra("position",position);
        return cheatIntent;
    }

    public void onClickCheat(View view){
            if (mCheatCount == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = cheatButton.getWidth() / 2;
                    int cy = cheatButton.getHeight() / 2;
                    float radius = cheatButton.getWidth();
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(
                                    cheatButton, cx, cy, radius, 0);

                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            cheatButton.setVisibility(View.INVISIBLE);

                        }
                    });
                    anim.start();
                } else {
                    cheatButton.setVisibility(View.INVISIBLE);
                }
            }

        mCheatCount--;
        cheatCounterView.setText(String.valueOf(mCheatCount));

                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
            startActivityForResult(
                    newIntent(this, answerIsTrue, mCurrentIndex), REQUEST_CODE_CHEAT);
        }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode != RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);

            }
        }

        public static void setCheated(int position){
            mQuestionBank[position].setCheatedTrue();
        }
}