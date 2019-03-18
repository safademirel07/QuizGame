package com.safademirel.quizgame.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.iid.FirebaseInstanceId;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.safademirel.quizgame.Models.Question;
import com.safademirel.quizgame.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;


public class Category extends AppCompatActivity implements View.OnClickListener {

    private final static int[] CLICKABLES = {
            R.id.btnCategory1, R.id.btnCategory2, R.id.btnCategory3, R.id.btnCategory4, R.id.btnCategory5, R.id.btnCategory6, R.id.btnCategory7, R.id.btnCategory8
            , R.id.btnCategory9, R.id.btnCategory10, R.id.btnCategory11, R.id.btnCategory12, R.id.btnCategory13, R.id.btnCategory14, R.id.btnCategory15, R.id.btnCategory16, R.id.btnCategory17
            , R.id.btnCategory18

    };
    private static final String TAG = "safa";
    private static InterstitialAd gecisReklam;
    private Question[] mQuestion;
    private int category = 0;
    ProgressDialog mProgressDialog;
    private SweetAlertDialog pDialog;
    private Application application;
    private AdRequest adRequestBanner;//adRequest referansı

    public static void showGecisReklam() {//Geçiş reklamı Göstermek için

        if (gecisReklam.isLoaded())
            gecisReklam.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        this.setTitle("KATEGORİ SEÇ");

        MobileAds.initialize(getApplicationContext(), getString(R.string.kimlik));

        requestAd();
        loadGecisReklam();//Geçiş reklamı yüklüyoruz

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            getSupportActionBar().hide();
        }

        application = (Application) getApplication();


        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Yarışma yükleniyor...");
        pDialog.setCancelable(false);

        /*
        mProgressDialog = new ProgressDialog(Category.this);
        mProgressDialog.setMessage("Yarışma yükleniyor...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        */

        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }
    }

    private void requestAd() {
        adRequestBanner = new AdRequest.Builder().addTestDevice("BF5CC14377CAA7F045223C35C78279DC").addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        gecisReklam = new InterstitialAd(this);
        gecisReklam.setAdUnitId(getString(R.string.gecisad1));
        gecisReklam.setAdListener(new AdListener() { //Geçiş reklama listener ekliyoruz
            public void onAdClosed() { //Geçiş Reklam Kapatıldığında çalışır
                loadGecisReklam();
            }
        });
    }

    private void loadGecisReklam() {//Geçiş reklamı Yüklemek için
        gecisReklam.loadAd(adRequestBanner);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);


        finish();
        super.onBackPressed();
    }

    private Question[] parseQuestions(String jsonData, int type) throws JSONException {
        JSONArray questions = new JSONArray(jsonData);

        Question[] questionArray = new Question[questions.length()];


        for (int i = 0; i < questions.length(); i++) {
            JSONObject obj = questions.getJSONObject(i);
            if (type == 1) {
                Question item = new Question(obj.getString("soru"), obj.getString("cevapA"), obj.getString("cevapB"), obj.getString("cevapC"), obj.getString("cevapD"), obj.getInt("dogruCevap"), "");
                questionArray[i] = item;
            }
            else if (type == 2) {
                Question item = new Question(obj.getString("soru"), obj.getString("cevapA"), obj.getString("cevapB"), obj.getString("cevapC"), obj.getString("cevapD"), obj.getInt("dogruCevap"), obj.getString("imgUrl"));
                questionArray[i] = item;
            }
        }


        return questionArray;
    }


    private void errorPopup() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Hata")
                .setContentText("Bir hata meydana geldi!")
                .setConfirmText("TAMAM")
                .show();
    }

    private void parseInfo(final String url, final int type) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Builder()
                .url(url)
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

                try {
                    String jsonData = response.body().string();
                    System.out.println(jsonData);

                    if (response.isSuccessful()) {
                        mQuestion = parseQuestions(jsonData, type);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismiss();
                                startGame(mQuestion);
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
                } catch (JSONException e) {
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

    private void startGame(Question[] mQuestion) {
        application.bitti = false;
        application.joker1 = false;
        application.joker2 = false;
        application.joker3 = false;
        application.joker4 = -1;
        Bundle bundle = new Bundle();
        bundle.putParcelableArray("QUESTION", mQuestion);
        bundle.putInt("CATEGORY", category);
        Intent intent = new Intent(this, Quiz.class);
        intent.putExtra("QUESTION", bundle);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();

        switch (view.getId()) {
            case R.id.btnCategory1:
                pDialog.show();
                category = 1;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory2:
                pDialog.show();
                category = 2;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory3:
                pDialog.show();
                category = 3;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory4:
                pDialog.show();
                category = 30;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory5:
                pDialog.show();
                category = 4;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory6:
                pDialog.show();
                category = 5;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory7:
                pDialog.show();
                category = 6;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory8:
                pDialog.show();
                category = 7;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory9:
                pDialog.show();
                category = 8;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory10:
                pDialog.show();
                category = 10;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory11:
                pDialog.show();
                category = 9;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),1);
                break;
            case R.id.btnCategory12:
                pDialog.show();
                category = 11;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),2);
                break;
            case R.id.btnCategory13:
                pDialog.show();
                category = 12;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),2);
                break;
            case R.id.btnCategory14:
                pDialog.show();
                category = 13;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),2);
                break;
            case R.id.btnCategory15:
                pDialog.show();
                category = 14;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),2);
                break;
            case R.id.btnCategory16:
                pDialog.show();
                category = 16;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),2);
                break;
            case R.id.btnCategory17:
                pDialog.show();
                category = 18;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),2);
                break;
            case R.id.btnCategory18:
                pDialog.show();
                category = 20;
                bundle.putInt("gameCategory", category);
                if (application.mFirebaseAnalytics != null) {
                    application.mFirebaseAnalytics.logEvent("selectCategory", bundle);
                }
                parseInfo("http://www.ilahilerapp.com/api/v1/quiznew/" + category + "/" + FirebaseInstanceId.getInstance().getToken(),2);
                break;
        }
    }
}
