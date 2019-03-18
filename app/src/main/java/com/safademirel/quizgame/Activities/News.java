package com.safademirel.quizgame.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.safademirel.quizgame.Adapters.NewsAdapter;
import com.safademirel.quizgame.Models.New;
import com.safademirel.quizgame.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class News extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ananaaa";
    private List<New> newsArray;
    private SweetAlertDialog pDialog;
    private NewsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            getSupportActionBar().setTitle("DUYURULAR");
            getSupportActionBar().hide();

        }
        Application application = (Application) getApplication();

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Haberler yükleniyor...");
        pDialog.setCancelable(false);
        pDialog.show();

        Bundle bundle = new Bundle();
        bundle.putBoolean("newsOpened", true);
        if (application.mFirebaseAnalytics != null) {
        application.mFirebaseAnalytics.logEvent("newsOpened", bundle);}


        ListView listView = (ListView) findViewById(R.id.lvNews);
        newsArray = new ArrayList<>();

        adapter = new NewsAdapter(News.this, newsArray);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(News.this);
        parseNews("http://www.ilahilerapp.com/api/v1/news/"+ FirebaseInstanceId.getInstance().getToken());

    }

    private void errorPopup() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Hata")
                .setContentText("Bir hata meydana geldi!")
                .setConfirmText("TAMAM")
                .show();
    }


    private void parseNews(final String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
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

                        JSONArray questions = new JSONArray(jsonData);


                        for (int i = 0; i < questions.length(); i++) {
                            JSONObject obj = questions.getJSONObject(i);
                            New item = new New(obj.getString("title"), obj.getString("description"), obj.getString("date"));
                            newsArray.add(item);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismiss();
                                adapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
