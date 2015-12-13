package com.yeungeek.rxjava;

import junit.framework.TestCase;

import rx.Observable;
import rx.functions.Func1;

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
}
