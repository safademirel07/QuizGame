package com.safademirel.quizgame.Models;

/**
 * Created by SAFA on 2.7.2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SAFA on 17.1.2017.
 */

public class Question implements Parcelable {

    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int optionRight;
    private String url;

    public Question() { }


    public Question(String mQuestion, String mOptionA, String mOptionB, String mOptionC, String mOptionD, int mOptionRight, String mUrl )
    {
        this.question = mQuestion;
        this.optionA = mOptionA;
        this.optionB = mOptionB;
        this.optionC = mOptionC;
        this.optionD = mOptionD;
        this.optionRight = mOptionRight;
        this.url = mUrl;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getOptionRight() {
        return optionRight;
    }

    public void setOptionRight(int optionRight) {
        this.optionRight = optionRight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(optionA);
        parcel.writeString(optionB);
        parcel.writeString(optionC);
        parcel.writeString(optionD);
        parcel.writeInt(optionRight);
        parcel.writeString(url);

    }

    private Question(Parcel in) {
        question = in.readString();
        optionA = in.readString();
        optionB = in.readString();
        optionC = in.readString();
        optionD = in.readString();
        optionRight = in.readInt();
        url = in.readString();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
