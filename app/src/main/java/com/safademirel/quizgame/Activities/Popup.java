package com.safademirel.quizgame.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TextView;

import com.safademirel.quizgame.R;
import com.safademirel.quizgame.Utilities.ColorArcProgressBar;

public class Popup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        int right = getIntent().getIntExtra("right", 0);
        int notright = getIntent().getIntExtra("notRight", 0);
        int mypuan = getIntent().getIntExtra("maxScore", 0);

        TextView tvScoreResult = (TextView) findViewById(R.id.pskorValue);
        TextView tvRight = (TextView) findViewById(R.id.prightValue);
        TextView tvNotRight = (TextView) findViewById(R.id.pfalseValue);



        int total = right+notright;
        if (total == 0)
            total = 1;

        ColorArcProgressBar progressbar = (ColorArcProgressBar) findViewById(R.id.barPopup);
        progressbar.setMaxValues(total);
        progressbar.setCurrentValues(right);

        tvScoreResult.setText(String.valueOf(mypuan));
        tvRight.setText(String.valueOf(right));
        tvNotRight.setText(String.valueOf(notright));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER);

    }
}
