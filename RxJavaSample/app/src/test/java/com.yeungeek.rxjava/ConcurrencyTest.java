package com.yeungeek.rxjava;

import junit.framework.TestCase;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yeungeek on 2015/12/13.
 */
public class ConcurrencyTest extends TestCase {
    public void testSubscribe() throws IOException {
        System.out.println("Main: " + Thread.currentThread().getId());

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println("subscriber on " + Thread.currentThread().getId());
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()) //Schedulers.io()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("received: " + integer + " on " + Thread.currentThread().getId());
                    }
                });

        System.out.println("Finished main: " + Thread.currentThread().getId());

        System.in.read();
    }

    public void testObserver() throws IOException {
        Observable.create(o -> {
            System.out.println("Created on " + Thread.currentThread().getId());
            o.onNext(1);
            o.onNext(2);
            o.onCompleted();
        }).observeOn(Schedulers.newThread())
                .subscribe(i -> System.out.println("Received " + i + " on " + Thread.currentThread().getId()));

        System.in.read();
    }

    public void testObserverAndSub() throws IOException {
        Observable.create(o -> {
            System.out.println("Created on " + Thread.currentThread().getId());
            o.onNext(1);
            o.onNext(2);
            o.onCompleted();
        })      .subscribeOn(Schedulers.io()) //Schedulers.newThread()
                .doOnNext(i -> System.out.println("Before " + i + " on " + Thread.currentThread().getId()))
//                .subscribeOn(Schedulers.io()) //Schedulers.newThread()
                .observeOn(Schedulers.newThread())
                .doOnNext(i -> System.out.println("After" + i + " on " + Thread.currentThread().getId())).subscribe();

        System.in.read();
    }
}
