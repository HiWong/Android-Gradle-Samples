package com.yeungeek.rxjava.bus;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by yeungeek on 2015/10/19.
 *
 * @see <p>
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 * </p>
 */
public class RxBus {
//    private final PublishSubject<Object> bus = PublishSubject.create();

    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        if (null != bus) {
            bus.onNext(o);
        }
    }

    @RxLogObservable
    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
