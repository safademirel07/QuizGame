package com.safademirel.quizgame.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.safademirel.quizgame.Models.Question;
import com.safademirel.quizgame.R;
import com.safademirel.quizgame.Utilities.FirebaseInstanceIDService;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Quiz extends AppCompatActivity implements View.OnClickListener {

    private final static int[] CLICKABLES = {
            R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnNextDuring, R.id.btnCevapOgren

    };
    private static final int TIME_INTERVAL = 2000;
    private static TextView scoreTV;
    private static TextView timeTV;
    private static TextView scoreDuringTV;

    //TextView leftTime;
    private static TextView questionTV;
    private static TextView tvJoker1;
    private static TextView tvJoker2;
    private static TextView tvJoker3;
    private static ImageView logoDuringSafa;
    private static Button btnNextDuring;
    private static InterstitialAd gecisReklam;
    private final int time = 20;
    private int pastTime = 0;
    private int questionN = 0;
    Question[] sorular;
    private TextView question;
    private TextView question2;
    private Button buttonA;
    private Button buttonB;
    private Button buttonC;
    private Button buttonD;
    private ImageView imageView;
    private Button buttonReport;
    private RelativeLayout gameWindow;
    private RelativeLayout nextWindow;
    private RelativeLayout digerLayoutSafa;
    private RelativeLayout digerLayoutSafa2;

    private TextView tvPass;
    private int myScore = 0;
    private int cevap = 0;

    private int right = 0;
    private int notright = 0;

    private int category = 0;
    private SweetAlertDialog pDialog;

    Bitmap image = null;
    float density;

    private Button btnCevapOgren;


    private Application appSafa;
    private final Runnable kalanSureRunnable = new Runnable() {
        public void run() {

            if (appSafa.bitti) {
                appSafa.leftTimer.removeCallbacks(kalanSureRunnable);
                appSafa.leftTimerIsRunnig = false;
                return;
            }

            appSafa.leftTimerIsRunnig = true;
            pastTime++;

            if (time - pastTime >= 0) {
                timeTV.setText(String.valueOf((time - pastTime)));
                appSafa.leftTimer.postDelayed(kalanSureRunnable, 1000);
            } else {
                ++questionN;
                updateMyScore(false, 1, 1);
            }

        }
    };
    private Question[] mQuestion;
    private long mBackPressed;
    private AdRequest adRequestt;//adRequest referansı

    private static void showGecisReklam() {//Geçiş reklamı Göstermek için

        if (gecisReklam.isLoaded())
            gecisReklam.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            getSupportActionBar().setTitle("YARIŞMA");
            getSupportActionBar().hide();

        }



        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Hatalı soru gönderiliyor...");
        pDialog.setCancelable(false);

        ButterKnife.bind(this);

        gameWindow = (RelativeLayout) findViewById(R.id.quizGame);
        nextWindow = (RelativeLayout) findViewById(R.id.duringGame);
        digerLayoutSafa = (RelativeLayout) findViewById(R.id.digerLayoutSafa);
        digerLayoutSafa2 = (RelativeLayout) findViewById(R.id.digerLayoutSafa2);

        scoreDuringTV = (TextView) findViewById(R.id.tvScoreDuring);
        question = (TextView) findViewById(R.id.txtQuestion);
        question2 = (TextView) findViewById(R.id.txtQuestion2);
        scoreTV = (TextView) findViewById(R.id.scoreValue);
        timeTV = (TextView) findViewById(R.id.timesValue);
        questionTV = (TextView) findViewById(R.id.leftQuestionValue);
        imageView =  (ImageView) findViewById(R.id.ivSafa2);
        tvPass = (TextView) findViewById(R.id.tvPass);
        btnCevapOgren = (Button) findViewById(R.id.btnCevapOgren);
        question.setMovementMethod(new ScrollingMovementMethod());






        density = getResources().getDisplayMetrics().density;

        btnNextDuring = (Button) findViewById(R.id.btnNextDuring);

        logoDuringSafa = (ImageView) findViewById(R.id.logoDuringSafa);

        tvJoker1 = (TextView) findViewById(R.id.tvFifty);
        tvJoker2 = (TextView) findViewById(R.id.tvAsk);
        tvJoker3 = (TextView) findViewById(R.id.tvPass);

        buttonA = (Button) findViewById(R.id.btnOne);
        buttonB = (Button) findViewById(R.id.btnTwo);
        buttonC = (Button) findViewById(R.id.btnThree);
        buttonD = (Button) findViewById(R.id.btnFour);

        AdView adViewSafa = (AdView) findViewById(R.id.adViewQuiz);

        appSafa = (Application) getApplicationContext();

        if (appSafa.testMode)
        {
            adViewSafa.setVisibility(View.INVISIBLE);
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.kimlik));

        AdRequest adRequest2 = new AdRequest.Builder().addTestDevice("BF5CC14377CAA7F045223C35C78279DC").addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();


        adViewSafa.loadAd(adRequest2);

        requestAd();
        loadGecisReklam();//Geçiş reklamı yüklüyoruz


        appSafa.stopSong();
        appSafa.startSong();


        if (appSafa.getApiClient() == null || !appSafa.getApiClient().isConnected()) {
            Toast.makeText(getBaseContext(),"Bir hata meydana geldi. Çıkıp yapıp girin.",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Games.setViewForPopups(appSafa.getApiClient(), findViewById(R.id.gps_popup));
        }

        Parcelable[] parcelables = getIntent().getBundleExtra("QUESTION").getParcelableArray("QUESTION");

        category = getIntent().getBundleExtra("QUESTION").getInt("CATEGORY");


        mQuestion = Arrays.copyOf(parcelables, parcelables.length, Question[].class);

        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }

        cancelRunnable();

        askQuestion(questionN);


    }

    private void errorPopup() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Hata")
                .setContentText("Bir hata meydana geldi, soru gönderilemedi.")
                .setConfirmText("TAMAM")
                .show();
    }

    private void okayPopup() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Gönderildi")
                .setContentText("Hatalı soru gönderildi. Uyarınız için teşekkürler.")
                .setConfirmText("TAMAM")
                .show();
    }


    @OnClick(R.id.btnReport)
    public void Report() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Hatalı Soru")
                .setContentText("Bu sorunu hatalı olduğunu düşünüyorsanız bize gönderin.")
                .setConfirmText("GÖNDER")
                .setCancelText("İPTAL")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        sendReport();
                    }
                })
                .show();
    }

    public void sendReport() {
        pDialog.show();
        OkHttpClient client = new OkHttpClient();

        if (FirebaseInstanceId.getInstance().getToken() == null)
        {
            pDialog.dismiss();
            errorPopup();
            return;
        }

        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("question",mQuestion[questionN-1].getQuestion() )
                .add("token", FirebaseInstanceId.getInstance().getToken());

        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url("http://ilahilerapp.com/api/v1/report")
                .post(formBody)
                .build();


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismiss();
                        errorPopup();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.dismiss();
                            okayPopup();
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.dismiss();
                            errorPopup();
                        }
                    });
                }
            }
        });
    }

    private void requestAd() {
        adRequestt = new AdRequest.Builder().addTestDevice("BF5CC14377CAA7F045223C35C78279DC").addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        gecisReklam = new InterstitialAd(this);
        gecisReklam.setAdUnitId(getString(R.string.gecisad2));
        gecisReklam.setAdListener(new AdListener() { //Geçiş reklama listener ekliyoruz
            public void onAdClosed() { //Geçiş Reklam Kapatıldığında çalışır
                loadGecisReklam();
            }
        });
    }

    private void loadGecisReklam() {//Geçiş reklamı Yüklemek için
        gecisReklam.loadAd(adRequestt);
    }

    private void askQuestion(int position) {
        //showMyScore();
        //if (position > 11) {
        //     passMyScoreDone();
        // }
        visibleAll();
        questionTV.setText(String.valueOf(position + 1) + "/12");

        timeTV.setText("20");
        scoreTV.setText(String.valueOf(myScore));
        pastTime = 0;

        question.scrollTo(0,0);

        if (mQuestion[position].getUrl().length() <= 0)  { //text
            //imageView.setVisibility(View.GONE);
            //question.setVisibility(View.VISIBLE);
            digerLayoutSafa.setVisibility(View.VISIBLE);
            digerLayoutSafa2.setVisibility(View.GONE);
            question.setText(mQuestion[position].getQuestion());
            buttonA.setText(mQuestion[position].getOptionA());
            buttonB.setText(mQuestion[position].getOptionB());
            buttonC.setText(mQuestion[position].getOptionC());
            buttonD.setText(mQuestion[position].getOptionD());
        }
        else{
            digerLayoutSafa.setVisibility(View.INVISIBLE);
            digerLayoutSafa2.setVisibility(View.VISIBLE);
            //question.setText(mQuestion[position].getQuestion());
            question2.setText(mQuestion[position].getQuestion());
            final Intent intent = new Intent(this,ImagePopup.class);
            UrlImageViewHelper.setUrlDrawable(imageView, "http://www.ilahilerapp.com/api/images/" + mQuestion[position].getUrl(), null, UrlImageViewHelper.CACHE_DURATION_INFINITE, new UrlImageViewCallback() {
                @Override
                public void onLoaded(ImageView ımageView, Bitmap bitmap, String s, boolean b) {
                    if (bitmap != null) {
                        image = bitmap;

                        imageView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Bundle extras = new Bundle();
                                extras.putParcelable("imagebitmap", image);
                                intent.putExtras(extras);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });
            buttonA.setText(mQuestion[position].getOptionA());
            buttonB.setText(mQuestion[position].getOptionB());
            buttonC.setText(mQuestion[position].getOptionC());
            buttonD.setText(mQuestion[position].getOptionD());
        }
        if (density <= 1.5) {

            if (mQuestion[position].getOptionA().length() > 30 && mQuestion[position].getOptionA().length() < 41) {
                buttonA.setTextSize(9);
            } else if (mQuestion[position].getOptionA().length() >= 41) {
                buttonA.setTextSize(8);
            } else {
                buttonA.setTextSize(12);
            }

            if (mQuestion[position].getOptionB().length() > 30 && mQuestion[position].getOptionB().length() < 41) {
                buttonB.setTextSize(9);
            } else if (mQuestion[position].getOptionB().length() >= 41) {
                buttonB.setTextSize(8);
            } else {
                buttonB.setTextSize(12);
            }

            if (mQuestion[position].getOptionC().length() > 30 && mQuestion[position].getOptionC().length() < 41) {
                buttonC.setTextSize(9);
            } else if (mQuestion[position].getOptionC().length() >= 41) {
                buttonC.setTextSize(8);
            } else {
                buttonC.setTextSize(12);
            }

            if (mQuestion[position].getOptionD().length() > 30 && mQuestion[position].getOptionD().length() < 41) {
                buttonD.setTextSize(9);
            } else if (mQuestion[position].getOptionD().length() >= 41) {
                buttonD.setTextSize(8);
            } else {
                buttonD.setTextSize(12);
            }
        }
        else if (density > 1.5 && density < 2.0) {

            if (mQuestion[position].getOptionA().length() > 30 && mQuestion[position].getOptionA().length() < 41) {
                buttonA.setTextSize(12);
            } else if (mQuestion[position].getOptionA().length() >= 41) {
                buttonA.setTextSize(11);
            } else {
                buttonA.setTextSize(15);
            }

            if (mQuestion[position].getOptionB().length() > 30 && mQuestion[position].getOptionB().length() < 41) {
                buttonB.setTextSize(12);
            } else if (mQuestion[position].getOptionB().length() >= 41) {
                buttonB.setTextSize(11);
            } else {
                buttonB.setTextSize(15);
            }

            if (mQuestion[position].getOptionC().length() > 30 && mQuestion[position].getOptionC().length() < 41) {
                buttonC.setTextSize(12);
            } else if (mQuestion[position].getOptionC().length() >= 41) {
                buttonC.setTextSize(11);
            } else {
                buttonC.setTextSize(15);
            }

            if (mQuestion[position].getOptionD().length() > 30 && mQuestion[position].getOptionD().length() < 41) {
                buttonD.setTextSize(12);
            } else if (mQuestion[position].getOptionD().length() >= 41) {
                buttonD.setTextSize(11);
            } else {
                buttonD.setTextSize(15);
            }
        }
        else if (density >= 2.0 && density <= 3.0) {

            if (mQuestion[position].getOptionA().length() > 25 && mQuestion[position].getOptionA().length() < 31) {
                buttonA.setTextSize(13);
            } else if (mQuestion[position].getOptionA().length() >= 31) {
                buttonA.setTextSize(12);
            } else {
                buttonA.setTextSize(15);
            }

            if (mQuestion[position].getOptionB().length() > 25 && mQuestion[position].getOptionB().length() < 31) {
                buttonB.setTextSize(13);
            } else if (mQuestion[position].getOptionB().length() >= 31) {
                buttonB.setTextSize(12);
            } else {
                buttonB.setTextSize(15);
            }

            if (mQuestion[position].getOptionC().length() > 25 && mQuestion[position].getOptionC().length() < 31) {
                buttonC.setTextSize(13);
            } else if (mQuestion[position].getOptionC().length() >= 31) {
                buttonC.setTextSize(12);
            } else {
                buttonC.setTextSize(15);
            }

            if (mQuestion[position].getOptionD().length() > 25 && mQuestion[position].getOptionD().length() < 31) {
                buttonD.setTextSize(13);
            } else if (mQuestion[position].getOptionC().length() >= 31) {
                buttonD.setTextSize(12);

            } else {
                buttonD.setTextSize(15);
            }
        }
        else if (density > 3.0 && density < 100.0) {

            if (mQuestion[position].getOptionA().length() > 25 && mQuestion[position].getOptionA().length() < 31) {
                buttonA.setTextSize(14);
            } else if (mQuestion[position].getOptionA().length() >= 31) {
                buttonA.setTextSize(12);
            } else {
                buttonA.setTextSize(15);
            }

            if (mQuestion[position].getOptionB().length() > 25 && mQuestion[position].getOptionB().length() < 31) {
                buttonB.setTextSize(14);
            } else if (mQuestion[position].getOptionB().length() >= 31) {
                buttonB.setTextSize(12);
            } else {
                buttonB.setTextSize(15);
            }

            if (mQuestion[position].getOptionC().length() > 25 && mQuestion[position].getOptionC().length() < 31) {
                buttonC.setTextSize(14);
            } else if (mQuestion[position].getOptionC().length() >= 31) {
                buttonC.setTextSize(12);
            } else {
                buttonC.setTextSize(15);
            }

            if (mQuestion[position].getOptionD().length() > 25 && mQuestion[position].getOptionD().length() < 31) {
                buttonD.setTextSize(14);
            } else if (mQuestion[position].getOptionD().length() >= 31) {
                buttonD.setTextSize(12);

            } else {
                buttonD.setTextSize(15);
            }
        }
        //leftTime.setText(String.valueOf((time)));
        cevap = mQuestion[position].getOptionRight();
        //cancelRunnable();
        startRunnable();
    }

    private void cancelRunnable() {
        if (appSafa.leftTimerIsRunnig) {
            appSafa.leftTimer.removeCallbacks(kalanSureRunnable);
        }
        appSafa.leftTimerIsRunnig = false;

    }

    private void startRunnable() {
        pastTime = 0;
        if (appSafa.leftTimerIsRunnig) {
            appSafa.leftTimer.removeCallbacks(kalanSureRunnable);
            appSafa.leftTimerIsRunnig = false;
        }

        appSafa.leftTimer.postDelayed(kalanSureRunnable, 1000);
    }

    private void askJoker(int mRight, int position) {

        Intent intent = new Intent(this, Ask.class);
        Bundle bundle = new Bundle();


        Random generator = new Random();
        int i = generator.nextInt(20) + 61; //maks 79
        int i2 = generator.nextInt(10) + 1; // maks 10
        int i3 = generator.nextInt(10) + 1; // maks 10
        int i4 = generator.nextInt(10) + 1; // maks 10

        int total = i + i2 + i3 + i4;

        if (total < 100) {
            int yenisans = generator.nextInt(2) + 1;
            if (yenisans == 1)
                i2 += 100 - total;
            else if (yenisans == 2)
                i3 += 100 - total;
            else if (yenisans == 3)
                i4 += 100 - total;
        }


        total = i + i2 + i3 + i4;

        appSafa.joker2 = true;
        tvJoker2.setVisibility(View.INVISIBLE);

        if (mRight == 1) {
            bundle.putInt("mA",i);
            bundle.putInt("mB",i2);
            bundle.putInt("mC",i3);
            bundle.putInt("mD",i4);

        } else if (mRight == 2) {
            bundle.putInt("mA",i2);
            bundle.putInt("mB",i);
            bundle.putInt("mC",i3);
            bundle.putInt("mD",i4);

        } else if (mRight == 3) {
            bundle.putInt("mA",i3);
            bundle.putInt("mB",i2);
            bundle.putInt("mC",i);
            bundle.putInt("mD",i4);

        } else if (mRight == 4) {
            bundle.putInt("mA", i4);
            bundle.putInt("mB", i2);
            bundle.putInt("mC", i3);
            bundle.putInt("mD", i);
        }
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void eliminateTwoButton(int mright) {

        int[] colm = {1, 2, 3, 4};
        List l = new ArrayList();
        for (int i : colm) {
            if (i != mright) {
                l.add(i);
            }
        }

        appSafa.joker1 = true;
        tvJoker1.setVisibility(View.INVISIBLE);

        Collections.shuffle(l);
        for (int i = 0; i < 2; i++) {
            int rakam = (int) l.get(i);
            if (rakam == 1) {
                buttonA.setVisibility(View.INVISIBLE);
            } else if (rakam == 2) {
                buttonB.setVisibility(View.INVISIBLE);
            } else if (rakam == 3) {
                buttonC.setVisibility(View.INVISIBLE);
            } else if (rakam == 4) {
                buttonD.setVisibility(View.INVISIBLE);
            }

        }

    }

    private void pass() {
        Intent intent = new Intent(this, Buy.class);
        Bundle bundle = new Bundle();
        bundle.putString("soru",mQuestion[questionN-1].getQuestion());
        if (mQuestion[questionN-1].getOptionRight() == 1)
            bundle.putString("cevap",mQuestion[questionN-1].getOptionA());
        else if (mQuestion[questionN-1].getOptionRight() == 2)
            bundle.putString("cevap",mQuestion[questionN-1].getOptionB());
        else if (mQuestion[questionN-1].getOptionRight() == 3)
            bundle.putString("cevap",mQuestion[questionN-1].getOptionC());
        else if (mQuestion[questionN-1].getOptionRight() == 4)
            bundle.putString("cevap",mQuestion[questionN-1].getOptionD());
        bundle.putString("url",mQuestion[questionN-1].getUrl());
        bundle.putParcelable("imagebitmap", image);

        intent.putExtras(bundle);
        startActivity(intent);
        /*
        appSafa.joker3 = true;
        tvJoker3.setVisibility(View.INVISIBLE);
        updateMyScore(true, 0, 1);
        */

    }

    private void visibleAll() {
        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);

    }

    void passMyScoreDone() {
        // Intent intent = new Intent(getActivity(), QuizDoneActivity.class);
        //intent.putExtra("myscore",getMyScore());
        //startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {

            cancelRunnable();
            appSafa.leftTimer.removeCallbacks(kalanSureRunnable);
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(Quiz.this, "Menüye dönmek için tekrar basın.", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();


    }


    private void updateMyScore(boolean plus, int timesUp, int pass) {
        if (pass == 0 || timesUp == 0) {
            if (pass == 1) {
                appSafa.setKategori(category, 2, 1);
                appSafa.startEffect(1);
                myScore += 20;
                right += 1;

            } else {
                if (plus) {
                    appSafa.setKategori(category, 2, 1);
                    appSafa.startEffect(1);
                    myScore += (time - pastTime);
                    right += 1;
                } else {
                    appSafa.startEffect(2);
                    appSafa.setKategori(category, 3, 1);

                    myScore -= (time - pastTime);
                    if (myScore < 0)
                        myScore = 0;
                    notright += 1;
                }
                checkAchievement(category);
            }
        }


        scoreDuringTV.setVisibility(View.VISIBLE);
        //scoreDuringTV.setTextSize(20);
        btnCevapOgren.setVisibility(View.INVISIBLE);
        if (pass == 1) {
            if (timesUp == 1) {
                btnCevapOgren.setVisibility(View.VISIBLE);
                logoDuringSafa.setImageDrawable(getResources().getDrawable(R.drawable.pass));
            }
            else
                logoDuringSafa.setImageDrawable(getResources().getDrawable(R.drawable.truesing));
        }
        else {
            if (plus) {
                logoDuringSafa.setImageDrawable(getResources().getDrawable(R.drawable.truesing));

            } else {
                btnCevapOgren.setVisibility(View.VISIBLE);
                logoDuringSafa.setImageDrawable(getResources().getDrawable(R.drawable.falsesing));
            }
        }
        scoreTV.setText(String.valueOf(myScore));
        if (pass == 0) {
            if (plus)
                scoreDuringTV.setText("KAZANILAN SKOR: " + String.valueOf(time - pastTime));
            else
                scoreDuringTV.setText("KAYBEDİLEN SKOR: " + String.valueOf(time - pastTime));
        } else {
            if (timesUp == 1)
                scoreDuringTV.setText("SÜRE DOLDU");
            else {
                scoreDuringTV.setTextSize(16);
                scoreDuringTV.setText("GEÇİLDİ, KAZANILAN SKOR: " + String.valueOf(time - pastTime));
            }
            //scoreDuringTV.setVisibility(View.INVISIBLE);
        }

        if (questionN >= 12) {
            btnNextDuring.setText("BİTİR");
        }
        cancelRunnable();
        pastTime = 0;
        gameWindow.setVisibility(View.GONE);
        nextWindow.setVisibility(View.VISIBLE);


    }

    private void checkAchievement(int kategori) {

        if (appSafa.mGoogleApiClient.isConnected()) {

            String achievement10 = "";
            String achievement25 = "";
            String achievement50 = "";
            String achievement100 = "";
            String achievement250 = "";
            String achievement500 = "";
            String achievement1000 = "";
            String achievement5000 = "";
            String achievement10000 = "";

            if (kategori == 1) {
                achievement10 = getString(R.string.achievement_genel_kltr__10_doru);
                achievement25 = getString(R.string.achievement_genel_kltr__25_doru);
                achievement50 = getString(R.string.achievement_genel_kltr__50_doru);
                achievement100 = getString(R.string.achievement_genel_kltr__100_doru);
                achievement250 = getString(R.string.achievement_genel_kltr__250_doru);
                achievement500 = getString(R.string.achievement_genel_kltr__500_doru);
                achievement1000 = getString(R.string.achievement_genel_kltr__1000_doru);
                achievement5000 = getString(R.string.achievement_genel_kltr__5000_doru);
                achievement10000 = getString(R.string.achievement_genel_kltr__10000_doru);
            } else if (kategori == 2) {
                achievement10 = getString(R.string.achievement_tarih__10_doru);
                achievement25 = getString(R.string.achievement_tarih__25_doru);
                achievement50 = getString(R.string.achievement_tarih__50_doru);
                achievement100 = getString(R.string.achievement_tarih__100_doru);
                achievement250 = getString(R.string.achievement_tarih__250_doru);
                achievement500 = getString(R.string.achievement_tarih__500_doru);
                achievement1000 = getString(R.string.achievement_tarih__1000_doru);
                achievement5000 = getString(R.string.achievement_tarih__5000_doru);
                achievement10000 = getString(R.string.achievement_tarih__10000_doru);
            } else if (kategori == 3) {
                achievement10 = getString(R.string.achievement_corafya__10_doru);
                achievement25 = getString(R.string.achievement_corafya__25_doru);
                achievement50 = getString(R.string.achievement_corafya__50_doru);
                achievement100 = getString(R.string.achievement_corafya__100_doru);
                achievement250 = getString(R.string.achievement_corafya__250_doru);
                achievement500 = getString(R.string.achievement_corafya__500_doru);
                achievement1000 = getString(R.string.achievement_corafya__1000_doru);
                achievement5000 = getString(R.string.achievement_corafya__5000_doru);
                achievement10000 = getString(R.string.achievement_corafya__10000_doru);
            } else if (kategori == 4) {
                achievement10 = getString(R.string.achievement_kltrsanat__10_doru);
                achievement25 = getString(R.string.achievement_kltrsanat__25_doru);
                achievement50 = getString(R.string.achievement_kltrsanat__50_doru);
                achievement100 = getString(R.string.achievement_kltrsanat__100_doru);
                achievement250 = getString(R.string.achievement_kltrsanat__250_doru);
                achievement500 = getString(R.string.achievement_kltrsanat__500_doru);
                achievement1000 = getString(R.string.achievement_kltrsanat__1000_doru);
                achievement5000 = getString(R.string.achievement_kltrsanat__5000_doru);
                achievement10000 = getString(R.string.achievement_kltrsanat__10000_doru);
            } else if (kategori == 5) {
                achievement10 = getString(R.string.achievement_spor__10_doru);
                achievement25 = getString(R.string.achievement_spor__25_doru);
                achievement50 = getString(R.string.achievement_spor__50_doru);
                achievement100 = getString(R.string.achievement_spor__100_doru);
                achievement250 = getString(R.string.achievement_spor__250_doru);
                achievement500 = getString(R.string.achievement_spor__500_doru);
                achievement1000 = getString(R.string.achievement_spor__1000_doru);
                achievement5000 = getString(R.string.achievement_spor__5000_doru);
                achievement10000 = getString(R.string.achievement_spor__10000_doru);
            } else if (kategori == 6) {
                achievement10 = getString(R.string.achievement_edebiyat__10_doru);
                achievement25 = getString(R.string.achievement_edebiyat__25_doru);
                achievement50 = getString(R.string.achievement_edebiyat__50_doru);
                achievement100 = getString(R.string.achievement_edebiyat__100_doru);
                achievement250 = getString(R.string.achievement_edebiyat__250_doru);
                achievement500 = getString(R.string.achievement_edebiyat__500_doru);
                achievement1000 = getString(R.string.achievement_edebiyat__1000_doru);
                achievement5000 = getString(R.string.achievement_edebiyat__5000_doru);
                achievement10000 = getString(R.string.achievement_edebiyat__10000_doru);
            } else if (kategori == 7) {
                achievement10 = getString(R.string.achievement_mzik__10_doru);
                achievement25 = getString(R.string.achievement_mzik__25_doru);
                achievement50 = getString(R.string.achievement_mzik__50_doru);
                achievement100 = getString(R.string.achievement_mzik__100_doru);
                achievement250 = getString(R.string.achievement_mzik__250_doru);
                achievement500 = getString(R.string.achievement_mzik__500_doru);
                achievement1000 = getString(R.string.achievement_mzik__1000_doru);
                achievement5000 = getString(R.string.achievement_mzik__5000_doru);
                achievement10000 = getString(R.string.achievement_mzik__10000_doru);
            } else if (kategori == 8) {
                achievement10 = getString(R.string.achievement_sinema__10_doru);
                achievement25 = getString(R.string.achievement_sinema__25_doru);
                achievement50 = getString(R.string.achievement_sinema__50_doru);
                achievement100 = getString(R.string.achievement_sinema__100_doru);
                achievement250 = getString(R.string.achievement_sinema__250_doru);
                achievement500 = getString(R.string.achievement_sinema__500_doru);
                achievement1000 = getString(R.string.achievement_sinema__1000_doru);
                achievement5000 = getString(R.string.achievement_sinema__5000_doru);
                achievement10000 = getString(R.string.achievement_sinema__10000_doru);
            } else if (kategori == 10) {
                achievement10 = getString(R.string.achievement_ocuk__10_doru);
                achievement25 = getString(R.string.achievement_ocuk__25_doru);
                achievement50 = getString(R.string.achievement_ocuk__50_doru);
                achievement100 = getString(R.string.achievement_ocuk__100_doru);
                achievement250 = getString(R.string.achievement_ocuk__250_doru);
                achievement500 = getString(R.string.achievement_ocuk__500_doru);
                achievement1000 = getString(R.string.achievement_ocuk__1000_doru);
                achievement5000 = getString(R.string.achievement_ocuk__5000_doru);
                achievement10000 = getString(R.string.achievement_ocuk__10000_doru);
            } else if (kategori == 9) {
                achievement10 = getString(R.string.achievement_siyaset__10_doru);
                achievement25 = getString(R.string.achievement_siyaset__25_doru);
                achievement50 = getString(R.string.achievement_siyaset__50_doru);
                achievement100 = getString(R.string.achievement_siyaset__100_doru);
                achievement250 = getString(R.string.achievement_siyaset__250_doru);
                achievement500 = getString(R.string.achievement_siyaset__500_doru);
                achievement1000 = getString(R.string.achievement_siyaset__1000_doru);
                achievement5000 = getString(R.string.achievement_siyaset__5000_doru);
                achievement10000 = getString(R.string.achievement_siyaset__10000_doru);
            } else if (kategori == 11) {
                achievement10 = getString(R.string.achievement_bayraklar__10_doru);
                achievement25 = getString(R.string.achievement_bayraklar__25_doru);
                achievement50 = getString(R.string.achievement_bayraklar__50_doru);
                achievement100 = getString(R.string.achievement_bayraklar__100_doru);
                achievement250 = getString(R.string.achievement_bayraklar__250_doru);
                achievement500 = getString(R.string.achievement_bayraklar__500_doru);
                achievement1000 = getString(R.string.achievement_bayraklar__1000_doru);
                achievement5000 = getString(R.string.achievement_bayraklar__5000_doru);
                achievement10000 = getString(R.string.achievement_bayraklar__10000_doru);
            } else if (kategori == 12) {
                achievement10 = getString(R.string.achievement_nller__10_doru);
                achievement25 = getString(R.string.achievement_nller__25_doru);
                achievement50 = getString(R.string.achievement_nller__50_doru);
                achievement100 = getString(R.string.achievement_nller__100_doru);
                achievement250 = getString(R.string.achievement_nller__250_doru);
                achievement500 = getString(R.string.achievement_nller__500_doru);
                achievement1000 = getString(R.string.achievement_nller__1000_doru);
                achievement5000 = getString(R.string.achievement_nller__5000_doru);
                achievement10000 = getString(R.string.achievement_nller__10000_doru);
            } else if (kategori == 13) {
                achievement10 = getString(R.string.achievement_futbolcular__10_doru);
                achievement25 = getString(R.string.achievement_futbolcular__25_doru);
                achievement50 = getString(R.string.achievement_futbolcular__50_doru);
                achievement100 = getString(R.string.achievement_futbolcular__100_doru);
                achievement250 = getString(R.string.achievement_futbolcular__250_doru);
                achievement500 = getString(R.string.achievement_futbolcular__500_doru);
                achievement1000 = getString(R.string.achievement_futbolcular__1000_doru);
                achievement5000 = getString(R.string.achievement_futbolcular__5000_doru);
                achievement10000 = getString(R.string.achievement_futbolcular__10000_doru);
            } else if (kategori == 14) {
                achievement10 = getString(R.string.achievement_arabalar__10_doru);
                achievement25 = getString(R.string.achievement_arabalar__25_doru);
                achievement50 = getString(R.string.achievement_arabalar__50_doru);
                achievement100 = getString(R.string.achievement_arabalar__100_doru);
                achievement250 = getString(R.string.achievement_arabalar__250_doru);
                achievement500 = getString(R.string.achievement_arabalar__500_doru);
                achievement1000 = getString(R.string.achievement_arabalar__1000_doru);
                achievement5000 = getString(R.string.achievement_arabalar__5000_doru);
                achievement10000 = getString(R.string.achievement_arabalar__10000_doru);
            } else if (kategori == 16) {
                achievement10 = getString(R.string.achievement_takm_armalar__10_doru);
                achievement25 = getString(R.string.achievement_takm_armalar__25_doru);
                achievement50 = getString(R.string.achievement_takm_armalar__50_doru);
                achievement100 = getString(R.string.achievement_takm_armalar__100_doru);
                achievement250 = getString(R.string.achievement_takm_armalar__250_doru);
                achievement500 = getString(R.string.achievement_takm_armalar__500_doru);
                achievement1000 = getString(R.string.achievement_takm_armalar__1000_doru);
                achievement5000 = getString(R.string.achievement_takm_armalar__5000_doru);
                achievement10000 = getString(R.string.achievement_takm_armalar__10000_doru);
            } else if (kategori == 18) {
                achievement10 = getString(R.string.achievement_hayvanlar__10_doru);
                achievement25 = getString(R.string.achievement_hayvanlar__25_doru);
                achievement50 = getString(R.string.achievement_hayvanlar__50_doru);
                achievement100 = getString(R.string.achievement_hayvanlar__100_doru);
                achievement250 = getString(R.string.achievement_hayvanlar__250_doru);
                achievement500 = getString(R.string.achievement_hayvanlar__500_doru);
                achievement1000 = getString(R.string.achievement_hayvanlar__1000_doru);
                achievement5000 = getString(R.string.achievement_hayvanlar__5000_doru);
                achievement10000 = getString(R.string.achievement_hayvanlar__10000_doru);
            } else if (kategori == 20) {
                achievement10 = getString(R.string.achievement_bitkiler__10_doru);
                achievement25 = getString(R.string.achievement_bitkiler__25_doru);
                achievement50 = getString(R.string.achievement_bitkiler__50_doru);
                achievement100 = getString(R.string.achievement_bitkiler__100_doru);
                achievement250 = getString(R.string.achievement_bitkiler__250_doru);
                achievement500 = getString(R.string.achievement_bitkiler__500_doru);
                achievement1000 = getString(R.string.achievement_bitkiler__1000_doru);
                achievement5000 = getString(R.string.achievement_bitkiler__5000_doru);
                achievement10000 = getString(R.string.achievement_bitkiler__10000_doru);
            } else if (kategori == 30) {
                achievement10 = getString(R.string.achievement_islamiyet__10_doru);
                achievement25 = getString(R.string.achievement_islamiyet__25_doru);
                achievement50 = getString(R.string.achievement_islamiyet__50_doru);
                achievement100 = getString(R.string.achievement_islamiyet__100_doru);
                achievement250 = getString(R.string.achievement_islamiyet__250_doru);
                achievement500 = getString(R.string.achievement_islamiyet__500_doru);
                achievement1000 = getString(R.string.achievement_islamiyet__1000_doru);
                achievement5000 = getString(R.string.achievement_islamiyet__5000_doru);
                achievement10000 = getString(R.string.achievement_islamiyet__10000_doru);
            }

            int getMyRightAnswers = appSafa.getKategori(kategori, 2);
            if (getMyRightAnswers == 10) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement10);
                sendAchievementsToFirebase(achievement10);
            } else if (getMyRightAnswers == 25) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement25);
                sendAchievementsToFirebase(achievement25);
            } else if (getMyRightAnswers == 50) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement50);
                sendAchievementsToFirebase(achievement50);
            } else if (getMyRightAnswers == 100) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement100);
                sendAchievementsToFirebase(achievement100);
            } else if (getMyRightAnswers == 250) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement250);
                sendAchievementsToFirebase(achievement250);
            } else if (getMyRightAnswers == 500) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement500);
                sendAchievementsToFirebase(achievement500);
            } else if (getMyRightAnswers == 1000) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement1000);
                sendAchievementsToFirebase(achievement1000);
            } else if (getMyRightAnswers == 5000) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement5000);
                sendAchievementsToFirebase(achievement5000);
            } else if (getMyRightAnswers == 10000) {
                Games.Achievements.unlock(appSafa.mGoogleApiClient, achievement10000);
                sendAchievementsToFirebase(achievement10000);
            }
        }
    }

    private void sendAchievementsToFirebase(String key) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, key);
        if (appSafa.mFirebaseAnalytics != null) {
        appSafa.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, bundle); }

    }

    private void popupDouble() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Joker")
                .setContentText("Çift cevap hakkınızı kullandınız. Bir seferliğine soruya yanlış cevap verirseniz tekrar cevap verme hakkınız olucak.")
                .setConfirmText("TAMAM")
                .show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOne:
                ++questionN;
                cancelRunnable();
                if (cevap == 1) {
                    if (appSafa.joker4 == 0)
                    {
                        appSafa.joker4 = 1;
                    }
                    updateMyScore(true, 0, 0);
                } else {
                    if (appSafa.joker4 == 0)
                    {
                        --questionN;
                        appSafa.joker4 = 1;
                        buttonA.setVisibility(View.INVISIBLE);
                        break;
                    }
                    updateMyScore(false, 0, 0);
                }
                break;
            case R.id.btnTwo:
                ++questionN;
                cancelRunnable();
                if (cevap == 2) {
                    if (appSafa.joker4 == 0)
                    {
                        appSafa.joker4 = 1;
                    }
                    updateMyScore(true, 0, 0);
                } else {
                    if (appSafa.joker4 == 0)
                    {
                        --questionN;
                        appSafa.joker4 = 1;
                        buttonB.setVisibility(View.INVISIBLE);
                        break;
                    }
                    updateMyScore(false, 0, 0);
                }
                break;
            case R.id.btnThree:
                ++questionN;
                cancelRunnable();

                if (cevap == 3) {
                    if (appSafa.joker4 == 0)
                    {
                        appSafa.joker4 = 1;
                    }
                    updateMyScore(true, 0, 0);

                } else {
                    if (appSafa.joker4 == 0)
                    {
                        --questionN;
                        appSafa.joker4 = 1;
                        buttonC.setVisibility(View.INVISIBLE);
                        break;
                    }
                    updateMyScore(false, 0, 0);
                }
                break;
            case R.id.btnFour:
                ++questionN;
                cancelRunnable();
                if (cevap == 4) {
                    if (appSafa.joker4 == 0)
                    {
                        appSafa.joker4 = 1;
                    }
                    updateMyScore(true, 0, 0);

                } else {
                    if (appSafa.joker4 == 0)
                    {
                        --questionN;
                        appSafa.joker4 = 1;
                        buttonD.setVisibility(View.INVISIBLE);
                        break;
                    }
                    updateMyScore(false, 0, 0);

                }
                break;
            case R.id.btnNextDuring:

                if (questionN >= 12) {
                    if (myScore > 240) {
                        Intent intent = new Intent(Quiz.this, Main.class);
                        startActivity(intent);
                        showGecisReklam();
                        finish();
                        break;
                    }

                    if (!appSafa.bitti) {
                        submitScore(category, myScore); //Submit to my category
                        submitScore(0, myScore); //Submit to general category
                        Bundle bundle = new Bundle();
                        bundle.putLong(FirebaseAnalytics.Param.SCORE, myScore);
                        bundle.putLong("trueCount", right);
                        bundle.putLong("falseCount", notright);
                        bundle.putString("leaderboard_id", String.valueOf(category));
                        bundle.putString("leaderboard_id_GENEL", "0");
                        Bundle bundleNew = new Bundle();
                        bundleNew.putBoolean("status",true);
                        if (appSafa.mFirebaseAnalytics != null) {
                            appSafa.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE, bundle);
                            appSafa.mFirebaseAnalytics.logEvent("finishedGame", bundleNew);
                        }

                    }
                    if (myScore > appSafa.getKategori(category, 1))
                        appSafa.setKategori(category, 1, myScore);

                    appSafa.bitti = true;
                    cancelRunnable();
                    pastTime = 0;
                    Intent intent = new Intent(Quiz.this, Result.class);
                    intent.putExtra("myScore", myScore);
                    intent.putExtra("right", right);
                    intent.putExtra("notRight", notright);
                    startActivity(intent);

                    finish();
                    showGecisReklam();
                    break;
                }

                cancelRunnable();

                nextWindow.setVisibility(View.GONE);
                gameWindow.setVisibility(View.VISIBLE);

                askQuestion(questionN);
                break;
            case R.id.tvFifty:
                if (!appSafa.joker1)
                    eliminateTwoButton(cevap);
                break;
            case R.id.tvAsk:
                if (!appSafa.joker2
                        )
                    askJoker(cevap, questionN);
                break;
            case R.id.tvPass: //cift joker
                if (appSafa.joker4 == -1)
                {
                    appSafa.joker4 = 0;
                    popupDouble();
                    tvPass.setVisibility(View.GONE);
                }
                break;
            case R.id.btnCevapOgren:
                pass();
                break;
        }
    }

    public void submitScore(int kategori, final long mScore) {
        final long[] mSkor = {0};
        Log.d("bi bak","bakalim 1 " + appSafa.mGoogleApiClient.isConnected());
        if (!appSafa.mGoogleApiClient.isConnected()) {
            appSafa.mGoogleApiClient.connect();
        }
        Log.d("bi bak","bakalim 2 " + appSafa.mGoogleApiClient.isConnected());

        if (appSafa.getApiClient().isConnected()) {

            String key = "";
            if (kategori == 0)
                key = getString(R.string.leaderboard_genel);
            else if (kategori == 1)
                key = getString(R.string.leaderboard_genel_kltr);
            else if (kategori == 2)
                key = getString(R.string.leaderboard_tarih);
            else if (kategori == 3)
                key = getString(R.string.leaderboard_corafya);
            else if (kategori == 4)
                key = getString(R.string.leaderboard_kltrsanat);
            else if (kategori == 5)
                key = getString(R.string.leaderboard_spor);
            else if (kategori == 6)
                key = getString(R.string.leaderboard_edebiyat);
            else if (kategori == 7)
                key = getString(R.string.leaderboard_mzik);
            else if (kategori == 8)
                key = getString(R.string.leaderboard_sinema);
            else if (kategori == 9)
                key = getString(R.string.leaderboard_siyaset);
            else if (kategori == 10)
                key = getString(R.string.leaderboard_ocuk);
            else if (kategori == 11)
                key = getString(R.string.leaderboard_bayraklar);
            else if (kategori == 12)
                key = getString(R.string.leaderboard_nller);
            else if (kategori == 13)
                key = getString(R.string.leaderboard_futbolcular);
            else if (kategori == 14)
                key = getString(R.string.leaderboard_arabalar);
            else if (kategori == 16)
                key = getString(R.string.leaderboard_takm_armalar);
            else if (kategori == 18)
                key = getString(R.string.leaderboard_hayvanlar);
            else if (kategori == 20)
                key = getString(R.string.leaderboard_bitkiler);
            else if (kategori == 30)
                key = getString(R.string.leaderboard_islamiyet);


            final String finalKey = key;
            Log.d("bi bak","bakalim 3 " + finalKey);

            if (key.length() <= 1) {
                //Toast.makeText(getBaseContext(),"key yok. 2",Toast.LENGTH_SHORT).show();
                return;
            }
            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(appSafa.getApiClient(), key, LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(
                    new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {



                        @Override
                        public void onResult(Leaderboards.LoadPlayerScoreResult arg0) {
                            Log.d("bi bak","bakalim 4 " + arg0.toString());

                            LeaderboardScore c = arg0.getScore();
                            if (c == null)
                                mSkor[0] = 0;
                            else
                                mSkor[0] = c.getRawScore();


                            if (appSafa.getApiClient().isConnected()) {
                                Games.Leaderboards.submitScoreImmediate(appSafa.getApiClient(), finalKey, mSkor[0] + mScore).setResultCallback(new ResultCallback<Leaderboards.SubmitScoreResult>() {

                                    @Override
                                    public void onResult(Leaderboards.SubmitScoreResult arg0) {
                                        Log.d("bi bak","bakalim 5 " + arg0.toString());



                                    }

                                });
                            }
                        }
                    }
            );
        }
    }
}
