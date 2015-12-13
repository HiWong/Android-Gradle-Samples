package com.yeungeek.rxjava;

import junit.framework.TestCase;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by yeungeek on 2015/12/12.
 */
public class TransforTest extends TestCase {
    public void testMap() {
        Observable.range(0, 5).map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer + 3;
            }
        }).subscribe(new PrintSubscriber("Map: "));
    }

    public void testFlatMap() {
        Observable.just(2).flatMap(new Func1<Integer, Observable<?>>() {
            @Override
            public Observable<?> call(Integer integer) {
                return Observable.range(0, integer);
            }
        }).subscribe(new PrintSubscriber("Flat Map:"));
    }

//    public void testFlatMapIterable() {
//        Observable.range(1, 3).flatMapIterable(new Func1<Integer, Iterable<?>>() {
//            @Override
//            public Iterable<?> call(Integer integer) {
//                return rang;
//            }
//        }, new Func2<Integer, Object, Object>() {
//            @Override
//            public Object call(Integer integer, Object o) {
//                return null;
//            }
//        });
//    }
}
