package com.yeungeek.rxjava.fragment;

import android.os.Bundle;
import android.widget.Button;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.jakewharton.rxbinding.view.RxView;
import com.yeungeek.rxjava.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by yeungeek on 2015/9/16.
 */
public class BufferDemoFragment extends BaseFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupLogger();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_buffer;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscription = getBufferedSubscription();
    }

    @Override
    public void onPause() {
        super.onPause();
        subscription.unsubscribe();
    }

    /**********
     * main rx
     *********/
    @RxLogObservable
    private Subscription getBufferedSubscription() {
        return RxView.clicks(mTapBtn).map(s -> {       //use lam
            Timber.d("--------- GOT A TAP");
            log("GOT A TAP");
            return 1;
        }).buffer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("----- onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "--------- Woops on error!");
                        log("Dang error! check your logs");
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Timber.d("--------- onNext");
                        if (integers.size() > 0) {
                            log(String.format("%d taps", integers.size()));
                        } else {
                            Timber.d("--------- No taps received ");
                        }
                    }
                });
    }

    @Bind(R.id.btn_start_operation)
    Button mTapBtn;

    private Subscription subscription;
}
