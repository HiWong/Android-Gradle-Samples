package com.yeungeek.rxjava.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yeungeek.rxjava.R;
import com.yeungeek.rxjava.retrofit.Contributor;
import com.yeungeek.rxjava.retrofit.GithubApi;
import com.yeungeek.rxjava.util.RxUtils;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static java.lang.String.format;

/**
 * Created by yeungeek on 2015/9/24.
 */
public class RetrofitFragment extends BaseFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupLogger();
        api = createGithubApi();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_retrofit;
    }

    @OnClick(R.id.btn_demo_retrofit_contributors)
    public void onListContributorsClicked() {
        adapter.clear();

        subscription.add(api.contributors(username.getText().toString(), repo.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contributor>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Retrofit call 1 completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e,
                                "woops we got an error while getting the list of contributors");
                    }

                    @Override
                    public void onNext(List<Contributor> contributors) {
                        for (Contributor c : contributors) {
                            adapter.add(format("%s has made %d contributions to %s",
                                    c.login,
                                    c.contributions,
                                    repo.getText().toString()));

                            Timber.d("%s has made %d contributions to %s",
                                    c.login,
                                    c.contributions,
                                    repo.getText().toString());
                        }
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        subscription = RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscription);
    }

    private GithubApi createGithubApi() {
        OkHttpClient client = new OkHttpClient();


        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://api.github.com/");

        final String githubToken = getResources().getString(R.string.github_oauth_token);

        /**
         * @see https://github.com/square/okhttp/wiki/Interceptors
         */

        if (!TextUtils.isEmpty(githubToken)) {
            client.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request newRequest = request.newBuilder().addHeader("Authorization", String.format("token %s", githubToken)).build();
                    return chain.proceed(newRequest);
                }
            });

            builder.client(client);
        }

        return builder.build().create(GithubApi.class);
    }

    @Bind(R.id.demo_retrofit_contributors_username)
    EditText username;
    @Bind(R.id.demo_retrofit_contributors_repository)
    EditText repo;
//    @Bind(R.id.log_list)
//    ListView resultList;

    private GithubApi api;
    private CompositeSubscription subscription = new CompositeSubscription();
}
