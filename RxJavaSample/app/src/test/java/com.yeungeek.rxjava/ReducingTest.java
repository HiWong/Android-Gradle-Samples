package com.yeungeek.rxjava;

import junit.framework.TestCase;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

/**
 * Created by yeungeek on 2015/12/12.
 */
public class ReducingTest extends TestCase {
    public void testFilter() {
        Observable.range(0, 10).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer % 2 == 0;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });
    }
}
