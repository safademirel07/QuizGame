package com.safademirel.quizgame.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.safademirel.quizgame.R;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by SAFA on 2.7.2017.
 */

public class Application extends android.app.Application {

    private static MediaPlayer mediaPlayer;
    public GoogleApiClient mGoogleApiClient;
    public final Handler leftTimer = new Handler();
    public boolean leftTimerIsRunnig = false;
    FirebaseAnalytics mFirebaseAnalytics;
    boolean bitti = false;
    boolean joker1 = false;
    boolean joker2 = false;
    boolean joker3 = false;
    int joker4= -1;

    boolean testMode = false;

    String base64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAltZveMP7V3Xb8vjs1LlU/jH6xQ1/6WvNJq1cU/fSN66uigaInVm8MNlyscFdI3WiiIYDmw/IxTX2wyWBXsEbGHepK4cqQ0kDq/LIoVnOdilnRaCNh1Nw48pKbHEnTxbmNAXita4SVI568lchu2HIOngEtA5aRhDDSEU09nmMG5SxyKi2l35tEsWw1MEKw76v5P4EiBeTziIx+Gl3X0U4auglTgPewK3jYSTu1fHrW1bHFfdLWwTC8a4vXNVlIZu+i6wgCAUJ+862MOWbhIvWm3p7LrKZ43izfq0HGxVm0GzRFm9NUXL9QCKGWIKdzN2gN9sG8BQ9BUyee2tXFebqKwIDAQAB";

    String dogru10 = "com.safademirel.quizgame.10dogru";
    String dogru25 = "com.safademirel.quizgame.25dogru";
    String dogru50 = "com.safademirel.quizgame.50dogru";


    private long score;

    //IabHelper mHelper;

    void setConsumables(int count){
        if (getApiClient().isConnected()) {
            String playerId = Games.Players.getCurrentPlayerId(getApiClient());

            SharedPreferences sharedPref = Application.this.getSharedPreferences("consumablesKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("durum_"+playerId, true);
            editor.putInt("count_"+playerId, count);
            editor.apply();
        }
    }

    int getConsumablesCount(){
        if (getApiClient().isConnected()) {
            String playerId = Games.Players.getCurrentPlayerId(getApiClient());

            SharedPreferences sharedPref = Application.this.getSharedPreferences("consumablesKey", Context.MODE_PRIVATE);
            int durum = sharedPref.getInt("count_"+playerId, 0);
            return durum;
        }
        return -1;
    }
    int getConsumablesStatus(){
        if (getApiClient().isConnected()) {
            String playerId = Games.Players.getCurrentPlayerId(getApiClient());


            SharedPreferences sharedPref = Application.this.getSharedPreferences("consumablesKey", Context.MODE_PRIVATE);
            boolean durum = sharedPref.getBoolean("durum_"+playerId, false);
            if (durum == true)
                return 1;
            else
                return 0;
        }
        return -1;
    }


    void setKategori(int kategori, int type, int value) {
        if (getApiClient().isConnected()) {

            String playerId = Games.Players.getCurrentPlayerId(getApiClient());

            String key = "";
            if (kategori == 1)
                key = getString(R.string.kategori1);
            else if (kategori == 2)
                key = getString(R.string.kategori2);
            else if (kategori == 3)
                key = getString(R.string.kategori3);
            else if (kategori == 4)
                key = getString(R.string.kategori4);
            else if (kategori == 5)
                key = getString(R.string.kategori5);
            else if (kategori == 6)
                key = getString(R.string.kategori6);
            else if (kategori == 7)
                key = getString(R.string.kategori7);
            else if (kategori == 8)
                key = getString(R.string.kategori8);
            else if (kategori == 9)
                key = getString(R.string.kategori9);
            else if (kategori == 10)
                key = getString(R.string.kategori10);
            else if (kategori == 11)
                key = getString(R.string.kategori11);
            else if (kategori == 12)
                key = getString(R.string.kategori12);
            else if (kategori == 13)
                key = getString(R.string.kategori13);
            else if (kategori == 14)
                key = getString(R.string.kategori14);
            else if (kategori == 15)
                key = getString(R.string.kategori15);
            else if (kategori == 16)
                key = getString(R.string.kategori16);
            else if (kategori == 18)
                key = getString(R.string.kategori18);
            else if (kategori == 20)
                key = getString(R.string.kategori20);
            else if (kategori == 30)
                key = getString(R.string.kategori30);

            SharedPreferences sharedPref = Application.this.getSharedPreferences(key, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            if (type == 0)
                editor.putInt("DosyaOlustur_"+playerId, 1);
            else if (type == 1)
                editor.putInt("KategoriMaksSkor_"+playerId, value);
            else if (type == 2)
                editor.putInt("KategoriDogru_"+playerId, sharedPref.getInt("KategoriDogru_"+playerId, 0) + value);
            else if (type == 3)
                editor.putInt("KategoriYanlis_"+playerId, sharedPref.getInt("KategoriYanlis_"+playerId, 0) + value);

            editor.apply();
        }

    }

    public int getKategori(int kategori, int type) {
        if (getApiClient().isConnected()) {

            String playerId = Games.Players.getCurrentPlayerId(getApiClient());

            String key = "";
            if (kategori == 1)
                key = getString(R.string.kategori1);
            else if (kategori == 2)
                key = getString(R.string.kategori2);
            else if (kategori == 3)
                key = getString(R.string.kategori3);
            else if (kategori == 4)
                key = getString(R.string.kategori4);
            else if (kategori == 5)
                key = getString(R.string.kategori5);
            else if (kategori == 6)
                key = getString(R.string.kategori6);
            else if (kategori == 7)
                key = getString(R.string.kategori7);
            else if (kategori == 8)
                key = getString(R.string.kategori8);
            else if (kategori == 9)
                key = getString(R.string.kategori9);
            else if (kategori == 10)
                key = getString(R.string.kategori10);
            else if (kategori == 11)
                key = getString(R.string.kategori11);
            else if (kategori == 12)
                key = getString(R.string.kategori12);
            else if (kategori == 13)
                key = getString(R.string.kategori13);
            else if (kategori == 14)
                key = getString(R.string.kategori14);
            else if (kategori == 15)
                key = getString(R.string.kategori15);
            else if (kategori == 16)
                key = getString(R.string.kategori16);
            else if (kategori == 18)
                key = getString(R.string.kategori18);
            else if (kategori == 20)
                key = getString(R.string.kategori20);
            else if (kategori == 30)
                key = getString(R.string.kategori30);

            SharedPreferences sharedPref = Application.this.getSharedPreferences(key, Context.MODE_PRIVATE);
            if (type == 0)
                return sharedPref.getInt("DosyaOlustur_"+playerId, -1);
            else if (type == 1)
                return sharedPref.getInt("KategoriMaksSkor_"+playerId, 0);
            else if (type == 2)
                return sharedPref.getInt("KategoriDogru_"+playerId, 0);
            else if (type == 3)
                return sharedPref.getInt("KategoriYanlis_"+playerId, 0);

        }
        return -1;
    }


    public void startSong() {
        /*
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer = MediaPlayer.create(Application.this, R.raw.music);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        */
        return;
    }


    public void startEffect(int type) {

        SharedPreferences sharedPref = Application.this.getSharedPreferences(getString(R.string.preference_file_key5), Context.MODE_PRIVATE);
        if (!(sharedPref.getBoolean("MuzikSes", true)))
            return;


        MediaPlayer mediaPlayer2 = new MediaPlayer();
        mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);

        if (mediaPlayer2.isPlaying()) {
            mediaPlayer2.stop();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }

        if (type == 1)
            mediaPlayer2 = MediaPlayer.create(Application.this, R.raw.correct);
        else
            mediaPlayer2 = MediaPlayer.create(Application.this, R.raw.incorrect);

        mediaPlayer2.start();
    }


    public void stopSong() {
        return;

    }

    public long getScore(int kategori) {
        if (getApiClient().isConnected()) {

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

            if (key.length() <= 1)
                return 0;

            Games.Leaderboards.loadCurrentPlayerLeaderboardScore(getApiClient(), key, LeaderboardVariant.TIME_SPAN_ALL_TIME,
                    LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(
                    new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {

                        @Override
                        public void onResult(Leaderboards.LoadPlayerScoreResult arg0) {

                            LeaderboardScore c = arg0.getScore();
                            if (c == null)
                                score = 0;
                            else
                                score = c.getRawScore();
                        }

                    });
            return score;
        }
        return score;
    }



    public GoogleApiClient getApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }
}
