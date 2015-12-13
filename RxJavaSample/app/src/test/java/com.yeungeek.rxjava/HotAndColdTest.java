package com.yeungeek.rxjava;

import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;

/**
 * Created by yeungeek on 2015/12/13.
 */
public class HotAndColdTest extends TestCase {
    public void testCold() throws Exception {
        Observable<Long> o = Observable.interval(200, TimeUnit.MILLISECONDS);

        o.subscribe(i -> System.out.println("first: " + i));
        Thread.sleep(500);

        o.subscribe(i -> System.out.println("second: " + i));

        System.in.read();
    }

    public void testHot() throws Exception {
        ConnectableObservable<Long> o = Observable.interval(200, TimeUnit.MILLISECONDS).publish();
        Subscription s = o.connect();

        o.subscribe(i -> System.out.println("first: " + i));
//        Thread.sleep(500);
        Thread.sleep(1000);
        System.out.println("unsubscribe");
        s.unsubscribe();

//        o.subscribe(i -> System.out.println("second: " + i));
        Thread.sleep(1000);
        System.out.println("connect");
        s = o.connect();

        System.in.read();
    }
}
