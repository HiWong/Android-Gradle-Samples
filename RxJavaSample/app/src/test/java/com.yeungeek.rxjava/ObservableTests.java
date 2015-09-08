package com.yeungeek.rxjava;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * use mockio
 */
public class ObservableTests {

    @Mock
    Observable<Integer> w;

    private static final Func1<Integer, Boolean> IS_EVEN = new Func1<Integer, Boolean>() {
        @Override
        public Boolean call(Integer value) {
            return value % 2 == 0;
        }
    };

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreate() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("one");
                subscriber.onNext("two");
                subscriber.onNext("three");
                subscriber.onCompleted();
            }
        });

        Observer<String> observer = Mockito.mock(Observer.class);
        observable.subscribe(observer);

        verify(observer, times(1)).onNext("one");
        verify(observer, times(1)).onNext("two");
        verify(observer, times(1)).onNext("three");
        verify(observer, never()).onError(any(Throwable.class));
        verify(observer, times(1)).onCompleted();
    }

    @Test
    public void testOnSubscribeFails() {
        Observer<String> observer = mock(Observer.class);
        final RuntimeException exception = new RuntimeException("oops");
        Observable<String> o = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                throw exception;
            }
        });

        o.subscribe(observer);

        verify(observer, times(0)).onNext(anyString());
        verify(observer, times(0)).onCompleted();
        verify(observer, times(1)).onError(exception);
    }

    @Test
    public void fromArray() {
        String[] items = new String[]{"one", "two", "three"};
        //block
        final int count = Observable.from(items).count().toBlocking().single();

        System.out.println("count: " + count);
        assertEquals(3, count);
        assertEquals("two", Observable.from(items).skip(1).take(1).toBlocking().single());
        assertEquals("three", Observable.from(items).takeLast(1).toBlocking().single());
    }

    @Test
    public void fromIterable() {
        ArrayList<String> items = new ArrayList<>();
        items.add("one");
        items.add("two");
        items.add("three");

        assertEquals(new Integer(3), Observable.from(items).count().toBlocking().single());
        assertEquals("two", Observable.from(items).skip(1).take(1).toBlocking().single());
        assertEquals("three", Observable.from(items).takeLast(1).toBlocking().single());
    }


}
