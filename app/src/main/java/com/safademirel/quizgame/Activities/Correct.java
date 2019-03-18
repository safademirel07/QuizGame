package com.safademirel.quizgame.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.safademirel.quizgame.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Correct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        String soru = getIntent().getStringExtra("soru");
        String cevap = getIntent().getStringExtra("cevap");
        String url = getIntent().getStringExtra("url");
        final Bitmap image = (Bitmap) getIntent().getExtras().getParcelable("imagebitmap");


        TextView tvCorrectQuestion = (TextView) findViewById(R.id.correctQuestion);
        TextView tvCorrectQuestion2 = (TextView) findViewById(R.id.correctQuestion2);
        ImageView correctIv2 = (ImageView) findViewById(R.id.correctIv2);
        Button btnCorrectAnswer = (Button) findViewById(R.id.correctAnswer);

        RelativeLayout layoutCorrect = (RelativeLayout) findViewById(R.id.layoutCorrect);
        RelativeLayout layoutCorrect2 = (RelativeLayout) findViewById(R.id.layoutCorrect2);

        if (url.length() <= 0) {
            layoutCorrect.setVisibility(View.VISIBLE);
            layoutCorrect2.setVisibility(View.GONE);
            tvCorrectQuestion.setText(soru);
            btnCorrectAnswer.setText(cevap);

        }
        else {
            layoutCorrect.setVisibility(View.INVISIBLE);
            layoutCorrect2.setVisibility(View.VISIBLE);
            tvCorrectQuestion2.setText(soru);
            btnCorrectAnswer.setText(cevap);
            correctIv2.setImageBitmap(image);
        }

        final Intent intent = new Intent(this,ImagePopup.class);

        correctIv2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });


        ButterKnife.bind(this);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER);

    }

    @OnClick(R.id.closeCorrect)
    void closeWindow()
    {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
