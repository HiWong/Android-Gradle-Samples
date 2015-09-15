package com.yeungeek.rxjava.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.yeungeek.rxjava.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yeungeek on 2015/9/15.
 */
public class ConcurrencyWithSchedulersDemoFragment extends BaseFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupLogger();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_concurrency_schedulers;
    }

    @OnClick(R.id.btn_start_operation)
    public void startLongOperation() {
        progress.setVisibility(View.VISIBLE);
        log("Button Clicked");


        subscription = getObservable().compose(bindToLifecycle())       // Observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != subscription) {
            subscription.unsubscribe();
        }
    }

    private Observable<Boolean> getObservable() {
        return Observable.just(true).map(new Func1<Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean) {
                log("Within Observable");
                doSomeLongOperation_thatBlocksCurrentThread();
                return aBoolean;
            }
        });
    }

    private Observer<Boolean> getObserver() {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                log("On complete");
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Error in RxJava Demo concurrency");
                log(String.format("Boo! Error %s", e.getMessage()));
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                log(String.format("onNext with return value \"%b\"", aBoolean));
            }
        };
    }

    // -----------------------------------------------------------------------------------
    // Method that help wiring up the example (irrelevant to RxJava)

    private void doSomeLongOperation_thatBlocksCurrentThread() {
        log("performing long operation");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Timber.d("Operation was interrupted");
        }
    }

    private void setupLogger() {
        logs = new ArrayList<>();
        adapter = new LogAdapter(getActivity(), new ArrayList<>());
        logsList.setAdapter(adapter);
    }

    private void log(String logMsg) {
        if (isCurrentlyOnMainThread()) {
            logs.add(0, logMsg + " (main thread) ");
            adapter.clear();
            adapter.addAll(logs);
        } else {
            logs.add(0, logMsg + " (NOT main thread) ");

            // You can only do below stuff on main thread.
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    adapter.clear();
                    adapter.addAll(logs);
                }
            });
        }
    }

    private boolean isCurrentlyOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private class LogAdapter
            extends ArrayAdapter<String> {

        public LogAdapter(Context context, List<String> logs) {
            super(context, R.layout.layout_log, R.id.item_log, logs);
        }
    }

    @Bind(R.id.list_threading_log)
    ListView logsList;
    @Bind(R.id.progress_operation_running)
    ProgressBar progress;

    private LogAdapter adapter;
    private List<String> logs;
    private Subscription subscription;
}
