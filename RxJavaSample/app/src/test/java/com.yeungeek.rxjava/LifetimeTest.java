package com.yeungeek.rxjava;

import junit.framework.TestCase;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

/**
 * Created by yeungeek on 2015/12/11.
 */
public class LifetimeTest extends TestCase {
    public void testSubscribing() {
        Subject<Integer, Integer> s = ReplaySubject.create();
        Subscription sub = s.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });

        s.onNext(0);
        s.onNext(1);
        //s.onError(new Exception("Oops"));

        //unsubscribe
        sub.unsubscribe();
        s.onNext(2);
    }

    public void testUnsubscribing() {
        Subscription s = Subscriptions.create(new Action0() {
            @Override
            public void call() {
                System.out.println("clear");
            }
        });

        s.unsubscribe();
    }
}
