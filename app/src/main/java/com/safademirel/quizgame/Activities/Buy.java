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
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.safademirel.quizgame.R;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Buy extends AppCompatActivity implements BillingProcessor.IBillingHandler, View.OnClickListener, RewardedVideoAdListener {

    BillingProcessor bp;

    String base64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAltZveMP7V3Xb8vjs1LlU/jH6xQ1/6WvNJq1cU/fSN66uigaInVm8MNlyscFdI3WiiIYDmw/IxTX2wyWBXsEbGHepK4cqQ0kDq/LIoVnOdilnRaCNh1Nw48pKbHEnTxbmNAXita4SVI568lchu2HIOngEtA5aRhDDSEU09nmMG5SxyKi2l35tEsWw1MEKw76v5P4EiBeTziIx+Gl3X0U4auglTgPewK3jYSTu1fHrW1bHFfdLWwTC8a4vXNVlIZu+i6wgCAUJ+862MOWbhIvWm3p7LrKZ43izfq0HGxVm0GzRFm9NUXL9QCKGWIKdzN2gN9sG8BQ9BUyee2tXFebqKwIDAQAB";
    String dogru10 = "com.safademirel.quizgame.10dogru";
    String dogru25 = "com.safademirel.quizgame.25dogru";
    String dogru50 = "com.safademirel.quizgame.50dogru";
    String dogru100 = "com.safademirel.quizgame.100dogru";
    String dogru250 = "com.safademirel.quizgame.250dogru";
    String dogru500 = "com.safademirel.quizgame.500dogru";
    String dogru750 = "com.safademirel.quizgame.750dogru";
    String dogru1000 = "com.safademirel.quizgame.1000dogru";

    private RewardedVideoAd mAd;
    Button btUse;
    Button btnBuyAd;

    String soru;
    String cevap;
    String url;
    Bitmap image;
    private final static int[] CLICKABLES = {
            R.id.btnBuy1, R.id.btnBuy2, R.id.btnBuy3, R.id.btnBuy4, R.id.btnBuy5, R.id.btnBuy6, R.id.btnBuy7, R.id.btnBuy8, R.id.btnBuyAd, R.id.btUse

    };

    Application appSafa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        bp = new BillingProcessor(this,base64 ,this);

        appSafa = (Application) getApplicationContext();


        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }

        soru = getIntent().getStringExtra("soru");
        cevap = getIntent().getStringExtra("cevap");
        url = getIntent().getStringExtra("url");
        image = (Bitmap) getIntent().getExtras().getParcelable("imagebitmap");

        btUse = (Button) findViewById(R.id.btUse);
        btnBuyAd = (Button) findViewById(R.id.btnBuyAd);
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        loadAd();

        if (!mAd.isLoaded()) {
            btnBuyAd.setEnabled(false);
            btnBuyAd.setText("Reklam yükleniyor...");
        }
        btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    private void loadAd() {
        if (!mAd.isLoaded()){
            mAd.loadAd(getString(R.string.odul), new AdRequest.Builder().addTestDevice("BF5CC14377CAA7F045223C35C78279DC").addTestDevice("B7DAAABFD3AEBAF0F50C41D760BAFBA5").addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
        }
    }

    private void okayPopup(int count) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Satın Al")
                .setContentText(+count +" Doğru Hak satın aldınız. Desteğiniz için teşekkürler.")
                .setConfirmText("TAMAM")
                .show();
    }

    private void okayPopup2(int count) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Reklam")
                .setContentText("Reklam izleyerek " +count +" Doğru Hak kazandınız. Desteğiniz için teşekkürler.")
                .setConfirmText("TAMAM")
                .show();
    }

    private void errorPopup() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Kullan")
                .setContentText("Doğru hakkınız yok. Satın alarak veya reklam izleyerek işleme devam edebilirsiniz.")
                .setConfirmText("TAMAM")
                .show();
    }

    private void errorPopup2(int errorCode) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Satın Al")
                .setContentText("Satın alırken bir hata meydana geldi. Hata kodu " +errorCode)
                .setConfirmText("TAMAM")
                .show();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        Bundle bundle = new Bundle();

        if (Objects.equals(productId, dogru10)) {
            appSafa.setConsumables(appSafa.getConsumablesCount() + 10);
            btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
            okayPopup(10);
            bp.consumePurchase(dogru10);
            bundle.putInt("consumablesBuy", 10);
            if (appSafa.mFirebaseAnalytics != null) {
                appSafa.mFirebaseAnalytics.logEvent("buySafa", bundle);}

        }
        if (Objects.equals(productId, dogru25)) {
            bp.consumePurchase(dogru25);
            appSafa.setConsumables(appSafa.getConsumablesCount() + 25);
            btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
            okayPopup(25);
            bundle.putInt("consumablesBuy", 25);
            if (appSafa.mFirebaseAnalytics != null) {
                appSafa.mFirebaseAnalytics.logEvent("buySafa", bundle);}
        }
        if (Objects.equals(productId, dogru50)) {
            appSafa.setConsumables(appSafa.getConsumablesCount() + 50);
            btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
            okayPopup(50);
            bundle.putInt("consumablesBuy", 50);
            if (appSafa.mFirebaseAnalytics != null) {
                appSafa.mFirebaseAnalytics.logEvent("buySafa", bundle);}
        }
        if (Objects.equals(productId, dogru100)) {
            appSafa.setConsumables(appSafa.getConsumablesCount() + 100);
            btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
            okayPopup(100);
            bundle.putInt("consumablesBuy", 100);
            if (appSafa.mFirebaseAnalytics != null) {
                appSafa.mFirebaseAnalytics.logEvent("buySafa", bundle);}
        }
        if (Objects.equals(productId, dogru250)) {
            appSafa.setConsumables(appSafa.getConsumablesCount() + 250);
            btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
            okayPopup(250);
            bundle.putInt("consumablesBuy", 250);
            if (appSafa.mFirebaseAnalytics != null) {
                appSafa.mFirebaseAnalytics.logEvent("buySafa", bundle);}
        }
        if (Objects.equals(productId, dogru500)) {
            appSafa.setConsumables(appSafa.getConsumablesCount() + 500);
            btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
            okayPopup(500);
            bundle.putInt("consumablesBuy", 500);
            if (appSafa.mFirebaseAnalytics != null) {
                appSafa.mFirebaseAnalytics.logEvent("buySafa", bundle);}
        }
        if (Objects.equals(productId, dogru750)) {
            appSafa.setConsumables(appSafa.getConsumablesCount() + 750);
            btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
            okayPopup(750);
            bundle.putInt("consumablesBuy", 750);
            if (appSafa.mFirebaseAnalytics != null) {
                appSafa.mFirebaseAnalytics.logEvent("buySafa", bundle);}
        }
        if (Objects.equals(productId, dogru1000)) {
            appSafa.setConsumables(appSafa.getConsumablesCount() + 1000);
            btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
            okayPopup(1000);
            bundle.putInt("consumablesBuy", 1000);
            if (appSafa.mFirebaseAnalytics != null) {
                appSafa.mFirebaseAnalytics.logEvent("buySafa", bundle);}
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        errorPopup2(errorCode);
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBuy1:
                bp.purchase(this, dogru10);
                break;
            case R.id.btnBuy2:
                bp.purchase(this, dogru25);
                break;
            case R.id.btnBuy3:
                bp.purchase(this, dogru50);
                break;
            case R.id.btnBuy4:
                bp.purchase(this, dogru100);
                break;
            case R.id.btnBuy5:
                bp.purchase(this, dogru250);
                break;
            case R.id.btnBuy6:
                bp.purchase(this, dogru500);
                break;
            case R.id.btnBuy7:
                bp.purchase(this, dogru750);
                break;
            case R.id.btnBuy8:
                bp.purchase(this, dogru1000);
                break;
            case R.id.btUse:
                if (appSafa.getConsumablesCount() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("soru",soru);
                    bundle.putString("cevap",cevap);
                    bundle.putString("url",url);
                    bundle.putParcelable("imagebitmap", image);
                    if (appSafa.mFirebaseAnalytics != null ) {
                    appSafa.mFirebaseAnalytics.logEvent("showAnswer", bundle);}
                    appSafa.setConsumables(appSafa.getConsumablesCount() - 1);
                    btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
                    Intent intent = new Intent(this, Correct.class);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    errorPopup();
                }
                break;
            case R.id.btnBuyAd:
                if (mAd.isLoaded()){
                    Bundle bundle = new Bundle();
                    bundle.putInt("adClicked",1);
                    if (appSafa.mFirebaseAnalytics != null) {
                    appSafa.mFirebaseAnalytics.logEvent("clickedAd", bundle);}
                    mAd.show();
                }
                break;
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        btnBuyAd.setEnabled(true);
        btnBuyAd.setText("Reklam İzle (+2 Doğru)");
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        btnBuyAd.setEnabled(false);
        loadAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Bundle bundle = new Bundle();
        bundle.putInt("adFinished",1);
        if (appSafa.mFirebaseAnalytics != null) {
        appSafa.mFirebaseAnalytics.logEvent("finishedAd", bundle);}
        appSafa.setConsumables(appSafa.getConsumablesCount() +2);
        btUse.setText("Kullan ("+ String.valueOf(appSafa.getConsumablesCount())+")");
        okayPopup2(2);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "İptal edildi.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "Video yüklenemedi.", Toast.LENGTH_SHORT).show();

    }
}
