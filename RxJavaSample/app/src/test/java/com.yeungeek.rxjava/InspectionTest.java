package com.yeungeek.rxjava;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by yeungeek on 2015/12/12.
 */
public class InspectionTest extends TestCase {
    public void testAll() {
        Observable.just(0, 10, 10, 2, 8).all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer % 2 == 0;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                System.out.println(aBoolean);
            }
        });
    }

    public void testAllTake() throws IOException {
        Observable<Long> o = Observable.interval(150, TimeUnit.MICROSECONDS).take(5);

        o.all(new Func1<Long, Boolean>() {
            @Override
            public Boolean call(Long aLong) {
                return aLong < 3;
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                System.out.println(aBoolean);
            }
        });

        o.subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }
        });

        System.in.read();
    }


}
