package com.safademirel.quizgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.safademirel.quizgame.R;

public class Scores extends AppCompatActivity implements View.OnClickListener {


    private final static int[] CLICKABLES = {
            R.id.popbtnCategory1, R.id.popbtnCategory2, R.id.popbtnCategory3, R.id.popbtnCategory4, R.id.popbtnCategory5, R.id.popbtnCategory6, R.id.popbtnCategory7, R.id.popbtnCategory8
            , R.id.popbtnCategory9, R.id.popbtnCategory10, R.id.popbtnCategory11, R.id.popbtnCategory12, R.id.popbtnCategory13, R.id.popbtnCategory14, R.id.popbtnCategory15, R.id.popbtnCategory16, R.id.popbtnCategory17, R.id.popbtnCategory18

    };

    Application application;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        if(getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            getSupportActionBar().setTitle("Rekorlar");
            getSupportActionBar().hide();

        }


        application = (Application) getApplication();


        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }

    }

    public int kategoriGetRekorValue(int kategori)
    {
        return (application.getKategori(kategori, 1));
    }

    public int kategoriGetRightValue(int kategori)
    {
        return (application.getKategori(kategori, 2));
    }


    public int kategoriNotGetRightValue(int kategori)
    {
        return (application.getKategori(kategori, 3));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        Bundle bundleNew = new Bundle();
        Intent intent = new Intent(this, Popup.class);

        int kategori = 0;


        switch (view.getId()) {
            case R.id.popbtnCategory1:
                kategori = 1;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory2:
                kategori = 2;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory3:
                kategori = 3;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.popbtnCategory4:
                kategori = 30;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.popbtnCategory5:
                kategori = 4;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.popbtnCategory6:
                kategori = 5;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.popbtnCategory7:
                kategori = 6;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.popbtnCategory8:
                kategori = 7;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.popbtnCategory9:
                kategori = 8;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.popbtnCategory10:
                kategori = 10;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory11:
                kategori = 9;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory12:
                kategori = 11;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory13:
                kategori = 12;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory14:
                kategori = 13;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory15:
                kategori = 14;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory16:
                kategori = 16;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory17:
                kategori = 18;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.popbtnCategory18:
                kategori = 20;
                bundle.putInt("right", kategoriGetRightValue(kategori));
                bundle.putInt("notRight", kategoriNotGetRightValue(kategori));
                bundle.putInt("maxScore", kategoriGetRekorValue(kategori));
                bundleNew.putInt("scoresOpenedCategory",kategori);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("finishedGame", bundleNew); }
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
