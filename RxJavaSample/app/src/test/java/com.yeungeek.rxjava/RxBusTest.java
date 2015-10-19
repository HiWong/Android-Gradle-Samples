package com.yeungeek.rxjava;

import com.yeungeek.rxjava.bus.RxBus;

import junit.framework.TestCase;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by yeungeek on 2015/10/19.
 */
public class RxBusTest extends TestCase {
    private RxBus bus;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        bus = new RxBus();
    }

    public void testRxBus() throws Exception {
        bus.toObservable().filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return isNumber(o);
            }
        }).forEach(new Action1<Object>() {
            @Override
            public void call(Object o) {
                System.out.println("is number: " + o);
            }
        });

        bus.toObservable().filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return isString(o);
            }
        }).forEach(new Action1<Object>() {
            @Override
            public void call(Object o) {
                System.out.println(o);
            }
        });
        
        bus.toObservable().map(new Func1<Object, String>() {
            @Override
            public String call(Object o) {
                if (((Integer) o) > 10) {
                    return "Greater then 10";
                } else {
                    return "Less than or equal to 10";
                }
            }
        }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.err.println(throwable.getMessage());
            }
        }).retry().forEach(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

        bus.send(1);
        System.out.println("-----------------------");
        bus.send(11);
        System.out.println("-----------------------");
        bus.send("hello");
        System.out.println("-----------------------");
    }

    public boolean isNumber(Object o) {
        if (o instanceof Number) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isString(Object o) {
        if (o instanceof String) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
