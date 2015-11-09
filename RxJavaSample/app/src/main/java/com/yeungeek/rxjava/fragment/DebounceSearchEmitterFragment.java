package com.yeungeek.rxjava.fragment;

import android.os.Bundle;
import android.widget.EditText;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.yeungeek.rxjava.R;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static java.lang.String.format;

/**
 * Created by yeungeek on 2015/9/16.
 */
public class DebounceSearchEmitterFragment extends BaseFragment{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupLogger();

        Observable<TextViewTextChangeEvent> textChangeEventObservable = RxTextView.textChangeEvents(inputSearchText);

        subscription = textChangeEventObservable.compose(bindToLifecycle())
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSearchObserver());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_debounce;
    }

    @OnClick(R.id.clr_debounce)
    public void clearLog(){
        logs.clear();
        adapter.clear();
    }
    // -----------------------------------------------------------------------------------
    // Main Rx entities
    private Observer<TextViewTextChangeEvent> getSearchObserver(){
     return new Observer<TextViewTextChangeEvent>() {
         @Override
         public void onCompleted() {
             Timber.d("--------- onComplete");
         }

         @Override
         public void onError(Throwable e) {
             Timber.e(e, "--------- Woops on error!");
             log("Dang error. check your logs");
         }

         @Override
         public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
             log(format("Searching for %s", textViewTextChangeEvent.text().toString()));
         }
     };
    }

    @Bind(R.id.input_txt_debounce)
    EditText inputSearchText;

    private Subscription subscription;
}
