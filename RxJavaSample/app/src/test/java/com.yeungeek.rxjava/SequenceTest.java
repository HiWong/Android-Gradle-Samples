package com.yeungeek.rxjava;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by yeungeek on 2015/12/11.
 */
public class SequenceTest extends TestCase {
    public void testJust() {
        Observable<String> o = Observable.just("one", "two", "three");

        o.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });

        /**
         * one
         two
         three
         completed
         */
    }

    public void testEmpty() {
        Observable<String> o = Observable.empty();
        o.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(String s) {
                System.out.println("Received: " + s);
            }
        });
    }

    public void testDefer() throws Exception {
//        Observable<Long> o = Observable.just(System.currentTimeMillis());
        Observable<Long> o = Observable.defer(new Func0<Observable<Long>>() {
            @Override
            public Observable<Long> call() {
                return Observable.just(System.currentTimeMillis());
            }
        });

        o.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println(aLong);
            }
        });

        Thread.sleep(1000);

        o.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println(aLong);
            }
        });
    }

    public void testCreate() {
        Observable<String> o = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onCompleted();
            }
        });

        o.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(String s) {
                System.out.println("received: " + s);
            }
        });
    }

    public void testInterval() throws IOException {
        Observable<Long> o = Observable.interval(1000, TimeUnit.MICROSECONDS);

        o.subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("e = [" + e + "]");
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }
        });

        System.in.read();
    }

    public void testTimer() throws IOException {
        Observable<Long> o = Observable.timer(1, TimeUnit.SECONDS);
//        Observable<Long> o = Observable.interval(2L, 1L, TimeUnit.SECONDS);
        o.subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("e = [" + e + "]");
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }
        });

        System.in.read();
    }
}
