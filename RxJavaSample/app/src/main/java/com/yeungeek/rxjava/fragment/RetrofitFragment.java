package com.yeungeek.rxjava.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.EditText;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yeungeek.rxjava.R;
import com.yeungeek.rxjava.retrofit.Contributor;
import com.yeungeek.rxjava.retrofit.GithubApi;
import com.yeungeek.rxjava.retrofit.User;
import com.yeungeek.rxjava.util.RxUtils;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
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

    @OnClick(R.id.btn_demo_retrofit_contributors_with_user_info)
    public void onCntributorsWithUserInfo() {
        adapter.clear();

        Timber.d("onListUserName");

        subscription.add(api.contributors(cUsername.getText().toString(), cRepo.getText().toString())
                .flatMap(s -> Observable.from(s))
                .flatMap(new Func1<Contributor, Observable<Pair<User, Contributor>>>() {
                    @Override
                    public Observable<Pair<User, Contributor>> call(Contributor contributor) {
                        Observable<User> userObservable = api.user(contributor.login)
                                .filter(new Func1<User, Boolean>() {
                                    @Override
                                    public Boolean call(User user) {
                                        return !TextUtils.isEmpty(user.email) && !TextUtils.isEmpty(user.name);
                                    }
                                });

                        return Observable.zip(userObservable,
                                Observable.just(contributor), new Func2<User, Contributor, Pair<User, Contributor>>() {
                                    @Override
                                    public Pair<User, Contributor> call(User user, Contributor contributor) {
                                        return new Pair<User, Contributor>(user, contributor);
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Pair<User, Contributor>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Retrofit call 2 completed ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e,
                                "error while getting the list of contributors along with full names");
                    }

                    @Override
                    public void onNext(Pair<User, Contributor> userContributorPair) {
                        User user = userContributorPair.first;
                        Contributor contributor = userContributorPair.second;

                        adapter.add(format("%s(%s) has made %d contributions to %s",
                                user.name,
                                user.email,
                                contributor.contributions,
                                cRepo.getText().toString()));

                        adapter.notifyDataSetChanged();

                        Timber.d("%s(%s) has made %d contributions to %s",
                                user.name,
                                user.email,
                                contributor.contributions,
                                cRepo.getText().toString());
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


        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

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
    @Bind(R.id.demo_retrofit_contributors_with_user_info_username)
    EditText cUsername;
    @Bind(R.id.demo_retrofit_contributors_with_user_info_repository)
    EditText cRepo;

//    @Bind(R.id.log_list)
//    ListView resultList;

    private GithubApi api;
    private CompositeSubscription subscription = new CompositeSubscription();
}
