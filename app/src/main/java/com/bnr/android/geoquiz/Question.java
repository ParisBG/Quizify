package com.bnr.android.geoquiz;


public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean cheated;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        textResId = mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setCheatedTrue(){
        cheated = true;
    }

    public boolean hasHeCheated(){
        return cheated;
    }
}
