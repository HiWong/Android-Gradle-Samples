package com.yeungeek.rxjava;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * Created by yeungeek on 2015/12/13.
 */
public class TimeShiftedTest extends TestCase {
    public void testBuffer() {
        Observable.range(0, 10).buffer(4).subscribe(new PrintSubscriber("Buffer: "));
    }

    public void testBufferBySigle() throws IOException {
        Observable.interval(100, TimeUnit.MILLISECONDS).take(10)
                .buffer(Observable.interval(250, TimeUnit.MILLISECONDS))
                .subscribe(System.out::println);

        System.in.read();
    }
}
