package com.safademirel.quizgame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.safademirel.quizgame.R;
import com.safademirel.quizgame.Utilities.ColorArcProgressBar;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class Result extends AppCompatActivity {

    private boolean verdim = false;

    private Application appSafa;

    int mypuan;
    int right;
    int notright;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mypuan = getIntent().getIntExtra("myScore", 0);
        right = getIntent().getIntExtra("right", 0);
        notright = getIntent().getIntExtra("notRight", 0);

        AdView adViewSafa = (AdView) findViewById(R.id.adViewResult);

        appSafa = (Application) getApplicationContext();

        if (appSafa.testMode)
        {
            adViewSafa.setVisibility(View.INVISIBLE);
        }

        if(getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);

            getSupportActionBar().setTitle("SONUÇ");
            getSupportActionBar().hide();

        }
        ButterKnife.bind(this);

        if (verdim) {
            onMenuDon();
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.kimlik));

        AdRequest adRequest2 = new AdRequest.Builder().addTestDevice("BF5CC14377CAA7F045223C35C78279DC").addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();


        adViewSafa.loadAd(adRequest2);

        appSafa.leftTimer.removeCallbacks(null);

        ColorArcProgressBar progressbar = (ColorArcProgressBar) findViewById(R.id.bar1);
        TextView tvScoreResult = (TextView) findViewById(R.id.skorValue);
        TextView tvRight = (TextView) findViewById(R.id.rightValue);
        TextView tvNotRight = (TextView) findViewById(R.id.falseValue);



        progressbar.setCurrentValues(right);
        tvScoreResult.setText(String.valueOf(mypuan));
        tvRight.setText(String.valueOf(right));
        tvNotRight.setText(String.valueOf(notright));
        verdim = true;

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnMenuDon)
    public void onMenuDon() {

        Intent intent = new Intent(Result.this, Main.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnShare)
    protected void onShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, ben " + right + " Doğru, " + notright + " Yanlış ile " + mypuan + " puan yaptım. Sen rekorumu geçebilir misin? Hemen uygulamayı indir. https://play.google.com/store/apps/details?id=com.safademirel.quizgame");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Skorunu Paylaş"));

    }

}
