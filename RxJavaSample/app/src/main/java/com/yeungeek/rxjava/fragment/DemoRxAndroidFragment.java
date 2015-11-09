package com.yeungeek.rxjava.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Button;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.yeungeek.rxjava.R;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by yeungeek on 2015/9/10.
 */
public class DemoRxAndroidFragment extends BaseFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();

        backgroundHandler = new Handler(backgroundThread.getLooper());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demo_rx_android;
    }

    @OnClick(R.id.scheduler_example)
    public void schedule() {
        onRunSchedulerExampleButtonClicked();
    }

    void onRunSchedulerExampleButtonClicked() {
        sampleObservable()
                // Run on a background thread
                .subscribeOn(HandlerScheduler.from(backgroundHandler))
                        // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                        schedulerTv.setText("onCompleted");
                        schedulerTv.setClickable(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError()", e);
                        schedulerTv.setText("onError()");
                        schedulerTv.setClickable(false);
                    }

                    @Override
                    public void onNext(String string) {
                        Log.d(TAG, "onNext(" + string + ")");
                        schedulerTv.setText("onNext(" + string + ")");
                        schedulerTv.setClickable(false);
                    }
                });
    }

    @RxLogObservable
    static Observable<String> sampleObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                try {
                    // Do some long running operation
                    Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                } catch (InterruptedException e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }

    static class BackgroundThread extends HandlerThread {
        BackgroundThread() {
            super("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        }
    }

    private Handler backgroundHandler;
    private final static String TAG = "DemoRxAndroidFragment";

    @Bind(R.id.scheduler_example)
    Button schedulerTv;
}
