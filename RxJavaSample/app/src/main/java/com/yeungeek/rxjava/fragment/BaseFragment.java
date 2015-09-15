package com.yeungeek.rxjava.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.RxLifecycle;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by yeungeek on 2015/9/10.
 */
public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public final <T> Observable.Transformer<T, T> bindToLifecycle() {
        return RxLifecycle.bindFragment(this.lifecycleSubject);
    }

    protected final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    protected abstract int getLayoutId();
}
