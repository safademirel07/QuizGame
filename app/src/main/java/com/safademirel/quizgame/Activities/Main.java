package com.safademirel.quizgame.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kobakei.ratethisapp.RateThisApp;
import com.safademirel.quizgame.Models.Question;
import com.safademirel.quizgame.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_UNUSED = 9002;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private static InterstitialAd gecisReklam;
    private static Context context;
    private Question[] mQuestions;
    private boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;


    private Button startGame;
    private Button login;
    private Button logout;

    private Button ranking;
    private Button achievements;
    private Button records;

    private Menu menuNew;
    private final String TAG = "ananin ami";
    private final int REQUEST_ACHIEVEMENTS = 9002;
    long score = 0;
    private boolean mSignInClicked = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mResolvingConnectionFailure = false;
    private Application application;
    private long mBackPressed;
    private AdRequest adRequestt;//adRequest referansı

    private static void showGecisReklam() {//Geçiş reklamı Göstermek için

        if (gecisReklam.isLoaded())
            gecisReklam.show();
    }




    private static Context getAppContext() {
        return Main.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //getSupportActionBar().setIcon(R.mipmap.iconsuper);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        application = (Application) getApplication();

        Main.context = getApplicationContext();


        application.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        startGame = (Button) findViewById(R.id.btnClassic);
        login = (Button) findViewById(R.id.btnLogin);
        logout = (Button) findViewById(R.id.btnLogout);

        ranking = (Button) findViewById(R.id.btnRanking);
        achievements = (Button) findViewById(R.id.btnAchi);
        records = (Button) findViewById(R.id.btnScore);

        AdView adViewSafa = (AdView) findViewById(R.id.adViewMain);

        if (application.testMode)
        {
            adViewSafa.setVisibility(View.INVISIBLE);
        }

        //startGame.setText("");
        //login.setText("");


        if (!isNetwork()) {

            noConnect();
        }


        if (isNetwork()) {

            application.mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                    .build();
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.kimlik));

        requestAd();
        loadGecisReklam();//Geçiş reklamı yüklüyoruz


        AdRequest adRequest2 = new AdRequest.Builder().addTestDevice("BF5CC14377CAA7F045223C35C78279DC").addTestDevice("B7DAAABFD3AEBAF0F50C41D760BAFBA5").addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        //AdViewSafa.setAdUnitId(getString(R.string.bannerad1));
        adViewSafa.loadAd(adRequest2);


        Bundle bundle = new Bundle();
        if (application.mFirebaseAnalytics != null) {
        application.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);}

        RateThisApp.Config config = new RateThisApp.Config(0, 5);
        config.setTitle(R.string.rate_title);
        config.setMessage(R.string.rate_message);
        config.setYesButtonText(R.string.rate_yes);
        config.setNoButtonText(R.string.rate_no);
        config.setCancelButtonText(R.string.rate_later);
        RateThisApp.init(config);

        RateThisApp.onStart(this);
        RateThisApp.showRateDialogIfNeeded(this);

        //Preferences
        if (isNetwork()) {
            if (application.getKategori(1, 0) != 1)
                application.setKategori(1, 0, 1);
            if (application.getKategori(2, 0) != 1)
                application.setKategori(2, 0, 1);
            if (application.getKategori(3, 0) != 1)
                application.setKategori(1, 0, 1);
            if (application.getKategori(4, 0) != 1)
                application.setKategori(1, 0, 1);
            if (application.getKategori(5, 0) != 1)
                application.setKategori(1, 0, 1);
            if (application.getKategori(6, 0) != 1)
                application.setKategori(1, 0, 1);
        }

        Context context = Main.this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key5), Context.MODE_PRIVATE);
        SharedPreferences sharedPrefBildirim = context.getSharedPreferences(
                getString(R.string.preference_file_key6), Context.MODE_PRIVATE);

        if (!sharedPref.contains("MuzikBaslangic")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Ses")
                    .setContentText("Sesi açmak istiyor musunuz!")
                    .setConfirmText("EVET")
                    .setCancelText("HAYIR")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            muzikSes(true,true);
                            sDialog.dismissWithAnimation();

                            new SweetAlertDialog(Main.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Ses")
                                    .setContentText("Ses açıldı.")
                                    .show();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            muzikSes(false,true);
                            sDialog.dismissWithAnimation();

                            new SweetAlertDialog(Main.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Ses")
                                    .setContentText("Ses kapatıldı.")
                                    .show();
                        }
                    })
                    .show();

        }


        if (!sharedPrefBildirim.contains("BildirimBaslangic")) {
            bildirimAyarla(true);
        }

        FirebaseInstanceId.getInstance().getToken();

    }

    private void showGameButton() {
        //login.setText("");
        login.setVisibility(View.INVISIBLE);
        //startGame.setText("OYUN BAŞLAT");
        logout.setVisibility(View.VISIBLE);
        startGame.setVisibility(View.VISIBLE);
        ranking.setVisibility(View.VISIBLE);
        records.setVisibility(View.VISIBLE);
        achievements.setVisibility(View.VISIBLE);
    }

    private void showLoginButton() {
        //startGame.setText("");
        startGame.setVisibility(View.INVISIBLE);
        //login.setText("GİRİŞ YAP");
        ranking.setVisibility(View.INVISIBLE);
        achievements.setVisibility(View.INVISIBLE);
        records.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);

    }
    private void requestAd() {
        adRequestt = new AdRequest.Builder().addTestDevice("BF5CC14377CAA7F045223C35C78279DC").addTestDevice("B7DAAABFD3AEBAF0F50C41D760BAFBA5").addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
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
        gecisReklam.loadAd(adRequestt);
    }

    private void noConnect() {
        showLoginButton();

        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Hata")
                .setMessage("İnternet bağlantısı yok.")
                .setCancelable(false)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        finish();
                    }
                });

        */

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Hata.")
                .setContentText("İnternet bağlantısı yok.")
                .setConfirmText("Tamam")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        finish();
                    }
                })
                .show();

    }

    private boolean isNetwork() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    public void onBackPressed() {
        if (!isNetwork()) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
            return;
        }
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Çıkmak için tekrar basın.", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menuNew = (Menu) menu;
        Log.d("olustur","olustur");
        if (getMuzikSes()) {
            menu.findItem(R.id.action_sesacik).setTitle("Ses : Açık");
        } else {
            menu.findItem(R.id.action_sesacik).setTitle("Ses : Kapalı");
        }

        if (getBildirim()) {
            menu.findItem(R.id.action_bildirimacik).setTitle("Bildirim : Açık");
            //item.setIcon(R.mipmap.ic_seskapali);
        } else {
            menu.findItem(R.id.action_bildirimacik).setTitle("Bildirim : Kapalı");
            //item.setIcon(R.mipmap.ic_sesacik);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_sesacik:
                if (getMuzikSes()) {
                    muzikSes(false,false);
                    Toast.makeText(getBaseContext(), "Ses kapatıldı.", Toast.LENGTH_SHORT).show();
                    item.setTitle("Ses : Kapalı");
                } else {
                    muzikSes(true,false);
                    Toast.makeText(getBaseContext(), "Ses açıldı.", Toast.LENGTH_SHORT).show();
                    item.setTitle("Ses : Açık");

                }
                return true;
            case R.id.action_bildirimacik:
                if (getBildirim()) {
                    bildirimAyarla(false);
                    Toast.makeText(getBaseContext(), "Bildirimler kapatıldı.", Toast.LENGTH_SHORT).show();
                    item.setTitle("Bildirim : Kapalı");
                    //item.setIcon(R.mipmap.ic_seskapali);
                } else {
                    bildirimAyarla(true);
                    Toast.makeText(getBaseContext(), "Bildirimler açıldı.", Toast.LENGTH_SHORT).show();
                    item.setTitle("Bildirim : Açık");
                    //item.setIcon(R.mipmap.ic_sesacik);

                }
                return true;

            case R.id.action_news:
                Intent intent = new Intent(this, News.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Bil Kazan ile kendini geliştirmek istiyorsan, hemen uygulamayı marketten indir. https://play.google.com/store/apps/details?id=com.safademirel.quizgame");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Uygulamayı Paylaş"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void muzikSes(boolean value, boolean nerden) {
        SharedPreferences sharedPref = Main.this.getSharedPreferences(getString(R.string.preference_file_key5), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        MenuItem ses = menuNew.findItem(R.id.action_sesacik);

        if (value)
            ses.setTitle("Ses : Açık");
        else
            ses.setTitle("Ses : Kapalı");


        editor.putString("MuzikBaslangic", "evet");
        editor.putBoolean("MuzikSes", value);
        editor.apply();






    }

    private boolean getMuzikSes() {
        SharedPreferences sharedPref = Main.this.getSharedPreferences(getString(R.string.preference_file_key5), Context.MODE_PRIVATE);
        return sharedPref.getBoolean("MuzikSes", true);
    }

    public static boolean getBildirim() {
        SharedPreferences sharedPref = getAppContext().getSharedPreferences("123125423312", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("Bildirim", true);
    }

    private void bildirimAyarla(boolean value) {
        SharedPreferences sharedPref = Main.this.getSharedPreferences(getString(R.string.preference_file_key6), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("BildirimBaslangic", "evet");
        editor.putBoolean("Bildirim", value);
        editor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
            case RC_SIGN_IN:
                Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + responseCode + ", intent=" + intent);
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (responseCode == RESULT_OK) {
                    showGameButton();
                    application.mGoogleApiClient.connect();
                } else {
                    BaseGameUtils.showActivityResultError(this, requestCode, responseCode, R.string.signin_other_error);
                }
                break;
            case RC_UNUSED:
                Log.d("response","response " + responseCode);
                if (responseCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED) {
                    onSignOutButtonClickedFromGP();
                }
                break;
        }
        super.onActivityResult(requestCode, responseCode, intent);
    }


    @Override
    public void onStart() {


        application.startSong();

        if (application.mGoogleApiClient == null) {
            showLoginButton();
        } else if (!application.mGoogleApiClient.isConnected()) {
            Log.d(TAG, "Connecting client.");
            application.mGoogleApiClient.connect();

        } else {
            Log.w(TAG,
                    "GameHelper: client was already connected on onStart()");
        }
        super.onStart();
    }

    @Override
    protected void onPause() {
        application.stopSong();
        super.onPause();

    }

    @Override
    public void onStop() {

        if (application.mGoogleApiClient != null && application.mGoogleApiClient.isConnected()) {
            showGameButton();

        } else {
            showLoginButton();


        }
        super.onStop();
    }

    @OnClick(R.id.btnLogin)
    public void onSignInButtonClicked() {
        if (!isNetwork()) {
            noConnect();
            return;
        }
        mSignInClicked = true;
        //showGameButton();
        application.mGoogleApiClient.connect();


    }

    @OnClick(R.id.btnLogout)
    public void onSignOutButtonClicked() {
        if (!isNetwork()) {
            noConnect();
            return;
        }
        mSignInClicked = false;
        showLoginButton();
        mExplicitSignOut = true;
        if (application.mGoogleApiClient != null && application.mGoogleApiClient.isConnected()) {
            Games.signOut(application.mGoogleApiClient);
            application.mGoogleApiClient.disconnect();
        }


    }

    public void onSignOutButtonClickedFromGP() {
        if (!isNetwork()) {
            noConnect();
            return;
        }
        mSignInClicked = false;
        showLoginButton();
        mExplicitSignOut = true;
        if (application.mGoogleApiClient != null && application.mGoogleApiClient.isConnected()) {
            //Games.signOut(application.mGoogleApiClient);
            application.mGoogleApiClient.disconnect();
        }


    }


    @OnClick(R.id.btnClassic)
    public void startGame() {
        if (!isNetwork()) {
            noConnect();
            return;
        }

        if (application.mGoogleApiClient.isConnected()) {
            application.mGoogleApiClient.connect();
        }


        if (application.mGoogleApiClient == null || !application.mGoogleApiClient.isConnected())
        {
            Toast.makeText(getBaseContext(),"Bir hata meydana geldi. Çıkıp yapıp girin.",Toast.LENGTH_SHORT).show();
            return;
        }

        if (application.getConsumablesStatus() == 0) {
            application.setConsumables(5);
        }


        Bundle bundle = new Bundle();
        application.mFirebaseAnalytics.logEvent("startGame", bundle);

        Intent intent = new Intent(this, Category.class);
        startActivity(intent);
        //finish();
        showGecisReklam();


    }

    @OnClick(R.id.btnScore)
    public void startScore() {
        if (!isNetwork()) {
            noConnect();
            return;
        }
        Intent intent = new Intent(this, Scores.class);
        startActivity(intent);
        //finish();
        showGecisReklam();

    }

    @OnClick(R.id.btnRanking)
    public void startRanking() {
        if (!isNetwork()) {
            noConnect();
            return;
        }
        if (application.mGoogleApiClient.isConnected()) {
            application.mGoogleApiClient.connect();
        }

        if (application.mGoogleApiClient == null || !application.mGoogleApiClient.isConnected())
        {
            Toast.makeText(getBaseContext(),"Bir hata meydana geldi. Çıkıp yapıp girin.",Toast.LENGTH_SHORT).show();
            return;
        }

        startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(application.mGoogleApiClient), RC_UNUSED);

    }

    @OnClick(R.id.btnAchi)
    public void startAchievement() {
        if (!isNetwork()) {
            noConnect();
            return;
        }
        if (application.mGoogleApiClient.isConnected()) {
            application.mGoogleApiClient.connect();
        }

        if (application.mGoogleApiClient == null || !application.mGoogleApiClient.isConnected())
        {
            Toast.makeText(getBaseContext(),"Bir hata meydana geldi. Çıkıp yapıp girin.",Toast.LENGTH_SHORT).show();
            return;
        }

        startActivityForResult(Games.Achievements.getAchievementsIntent(application.mGoogleApiClient), RC_UNUSED);
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        showGameButton();
    }

    @Override
    public void onConnectionSuspended(int i) {
        application.mGoogleApiClient.connect();
        showGameButton();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, application.mGoogleApiClient, connectionResult, RC_SIGN_IN, "hata");
            showLoginButton();

        }

    }

    public GoogleApiClient getApiClient() {
        return application.mGoogleApiClient;
    }
}
