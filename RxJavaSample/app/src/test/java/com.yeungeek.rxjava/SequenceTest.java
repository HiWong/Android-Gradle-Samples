package com.yeungeek.rxjava;

import junit.framework.TestCase;

import rx.Observable;
import rx.Observer;

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
}
