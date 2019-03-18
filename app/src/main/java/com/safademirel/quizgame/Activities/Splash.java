package com.safademirel.quizgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.safademirel.quizgame.Activities.Main;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }
}