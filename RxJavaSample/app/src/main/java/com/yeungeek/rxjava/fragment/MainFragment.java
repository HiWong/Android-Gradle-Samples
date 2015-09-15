package com.yeungeek.rxjava.fragment;

import android.support.v4.app.Fragment;
import android.widget.Button;

import com.yeungeek.rxjava.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yeungeek on 2015/9/10.
 */
public class MainFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @OnClick(R.id.demo_rx_android)
    public void demoRxAndroid() {
        startFragment(new DemoRxAndroidFragment(), DemoRxAndroidFragment.class.toString());
    }

    @OnClick(R.id.demo_concurrency)
    public void demoConcurrency() {
        startFragment(new ConcurrencyWithSchedulersDemoFragment(), ConcurrencyWithSchedulersDemoFragment.class.toString());
    }

    private void startFragment(final Fragment fragment, final String classString) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(classString)
                .replace(R.id.main_content,
                        fragment,
                        classString)
                .commit();
    }

    @Bind(R.id.demo_rx_android)
    Button demoRxAndroid;
    @Bind(R.id.demo_concurrency)
    Button demoConcurrency;
}
