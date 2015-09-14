package com.yeungeek.rxjava.fragment;

import android.widget.Button;

import com.yeungeek.rxjava.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yeungeek on 2015/9/10.
 */
public class MainFragment extends BaseFragment{

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @OnClick(R.id.demo_rx_android)
    public void demoRxAndroid(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(DemoRxAndroidFragment.class.toString())
                .replace(R.id.main_content,
                        new DemoRxAndroidFragment(),
                        DemoRxAndroidFragment.class.toString())
                .commit();
    }


    @Bind(R.id.demo_rx_android)
    Button demoRxAndroid;
}
