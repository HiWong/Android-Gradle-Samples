package com.yeungeek.rxjava;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Arrays;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by yeungeek on 2015/8/26.
 */
public class RxJavaBaseTest extends TestCase {
    @Test
    public void testHelloWorld() {
        final String printStr = "Hello,world!";

        Observable<String> myObser = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(printStr);
                subscriber.onCompleted();
            }
        });

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.print(s);
                assertEquals(printStr, s);
            }
        };

        myObser.subscribe(mySubscriber);
    }

    @Test
    public void testHelloWorldSimple() {
        final String printStr = "Hello,world!";
        Observable.just(printStr).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.print(s);
                assertEquals(printStr, s);
            }
        });
    }

    @Test
    public void testHelloWorldLambda() {
        final String printStr = "Hello,world!";
        Observable.just(printStr).subscribe(s ->
                        System.out.print(s)
        );
    }

    @Test
    public void testOperatorSimple() {
        Observable.just("Hello, world").map(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                return s + " - Me";
            }
        }).subscribe(s -> System.out.println(s));

        Observable.just("Hello world").map(s -> s + " - Lambda").subscribe(s -> System.out.println(s));

        Observable.just("Hello world").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                return s.hashCode();
            }
        }).subscribe(s -> System.out.println(Integer.toString(s)));

        Observable.just("Hello world").map(s -> s.hashCode()).map(i -> Integer.toHexString(i).toUpperCase()).subscribe(s -> System.out.println(s));
    }

    @Test
    public void testOperator() {
        Observable.from(Arrays.asList("url1", "url2", "url3")).subscribe(s -> System.out.println(s));
        //flatMap
    }

    @Test
    public void testPowerful() {
        Observable.just("Hello,world!")
                .map(s -> funcThrow0(s))
                .subscribe(new Subscriber<Func0<String>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Ouch!");
                    }

                    @Override
                    public void onNext(Func0<String> stringFunc0) {
                        System.out.println(stringFunc0);
                    }
                });
    }

    <R> Func0<R> funcThrow0(R r) {
        return new Func0<R>() {
            @Override
            public R call() {
                throw new TestException();
            }
        };
    }
}
