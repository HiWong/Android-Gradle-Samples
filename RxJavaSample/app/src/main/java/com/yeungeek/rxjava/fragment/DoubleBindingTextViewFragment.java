package com.yeungeek.rxjava.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.yeungeek.rxjava.R;

import butterknife.Bind;
import butterknife.OnTextChanged;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by yeungeek on 2015/10/14.
 */
public class DoubleBindingTextViewFragment extends BaseFragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        resultEmitterSubject = PublishSubject.create();
        subscription = resultEmitterSubject.asObservable().subscribe(new Action1<Float>() {
            @Override
            public void call(Float aFloat) {
                result.setText(String.valueOf(aFloat));
            }
        });

        onNumberChanged();
        num1Et.requestFocus();
    }

    @OnTextChanged({R.id.double_binding_num1, R.id.double_binding_num2})
    public void onNumberChanged() {
        float num1 = 0;
        float num2 = 0;

        if (!TextUtils.isEmpty(num1Et.getText().toString())) {
            num1 = Float.parseFloat(num1Et.getText().toString());
        }

        if (!TextUtils.isEmpty(num2Et.getText().toString())) {
            num2 = Float.parseFloat(num2Et.getText().toString());
        }

        resultEmitterSubject.onNext(num1 + num2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != subscription) {
            subscription.unsubscribe();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_double_binding_textview;
    }

    @Bind(R.id.double_binding_num1)
    EditText num1Et;
    @Bind(R.id.double_binding_num2)
    EditText num2Et;
    @Bind(R.id.double_binding_result)
    TextView result;

    PublishSubject<Float> resultEmitterSubject;
    Subscription subscription;
}
