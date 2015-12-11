package com.yeungeek.rxjava;

import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Created by yeungeek on 2015/12/10.
 */
public class SubjectTest extends TestCase {

    public void testPublishSubject() {
        PublishSubject<Integer> publishSubject = PublishSubject.create();
        publishSubject.onNext(1);
        publishSubject.subscribe(new SubAction());

        publishSubject.onNext(2);
        publishSubject.onNext(3);
        publishSubject.onNext(4);
    }

    public void testReplaySubject() {
        ReplaySubject<Integer> replaySubject = ReplaySubject.create();
        replaySubject.subscribe(new SubAction("Early: "));
        replaySubject.onNext(0);
        replaySubject.onNext(1);
        replaySubject.subscribe(new SubAction("Later: "));
        replaySubject.onNext(2);
    }

    public void testReplaySubjectLimit() throws Exception {
        ReplaySubject<Integer> replaySubject =/* ReplaySubject.createWithSize(2);*/
                ReplaySubject.createWithTime(150, TimeUnit.MICROSECONDS, Schedulers.immediate());
        replaySubject.onNext(0);
        Thread.sleep(100);
        replaySubject.onNext(1);
        Thread.sleep(100);
        replaySubject.onNext(2);
        replaySubject.subscribe(new SubAction("Later: "));
        replaySubject.onNext(3);
    }

    public void testBehaviorSubject() {
//        BehaviorSubject<Integer> s = BehaviorSubject.create();
//        s.onNext(0);
//        s.onNext(1);
//        s.onNext(2);
        //1.
//        s.subscribe(new SubAction("Later: "));
//        s.onNext(3);

        //2.
//        s.onCompleted();
//        s.subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("error");
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                System.out.println("Later: " + integer);
//            }
//        });

        //3.
        BehaviorSubject<Integer> s = BehaviorSubject.create(0);
        s.subscribe(new SubAction("Later: "));
        s.onNext(1);
    }

    public void testAsyncSubject() {
        AsyncSubject<Integer> s = AsyncSubject.create();
        s.subscribe(new SubAction());
        s.onNext(0);
        s.onCompleted();
        s.onNext(1);
        s.onNext(2);
//        s.onCompleted();
    }

    private class SubAction implements Action1<Integer> {
        public SubAction() {
        }

        public SubAction(String name) {
            this.name = name;
        }

        @Override
        public void call(Integer integer) {
            name = name == null ? "" : name;
            System.out.println(name + integer);
        }

        private String name;
    }
}
