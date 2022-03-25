package com.apponative.committee_app.firebase;

import android.app.Activity;

import com.apponative.committee_app.R;
import com.apponative.committee_app.utils.CommitteeCallBack;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Muhammad Waqas on 5/25/2017.
 */

public class OkkHttpBuilder {
    private static final OkkHttpBuilder ourInstance = new OkkHttpBuilder();
    OkHttpClient client = new OkHttpClient();
    static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    static Activity activity;

    public static OkkHttpBuilder getInstance(Activity activity) {
        OkkHttpBuilder.activity = activity;
        return ourInstance;
    }

    public void shareTurnsWithMembers(String datalist, final CommitteeCallBack.CommitteeRandomDrawCallBacks onturnshared) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, datalist);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(activity.getResources().getString(R.string.shareTurnUrl)).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnTurnSelected(onturnshared, false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnTurnSelected(onturnshared, true);
            }
        });
    }

    void runOnTurnSelected(final CommitteeCallBack.CommitteeRandomDrawCallBacks onturnshared, final boolean result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onturnshared.OnTurnShared(result);
            }
        });
    }

    void runCommitteeCompleted(final CommitteeCallBack.CommitteeCompleteCallBack committeeCompleteCallBack, final boolean result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                committeeCompleteCallBack.OnCommitteeComplete(result);
            }
        });
    }
    public void completeCommittee(String datalist, final CommitteeCallBack.CommitteeCompleteCallBack committeeCompleteCallBack) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, datalist);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(activity.getResources().getString(R.string.completeCommitteeUrl)).newBuilder();
        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runCommitteeCompleted(committeeCompleteCallBack,false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runCommitteeCompleted(committeeCompleteCallBack,true);
            }
        });
    }


    public void filterUserContacts(String contactlist, final CommitteeCallBack.CommitteeCompleteCallBack committeeCompleteCallBack) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, contactlist);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(activity.getResources().getString(R.string.filterContactsUrl)).newBuilder();
        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }
}
