package bupt.liao.fred.socialsearch.app.rxbus;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import rx.subjects.SerializedSubject;

/**
 * Created by Fred.Liao on 2018/1/2.
 * Email:fredliaobupt@qq.com
 * Description: RxBus.
 * Reference:https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 */

public class RxBus {

    private PublishSubject<Object> subject = PublishSubject.create();

    public RxBus() {
        subject = PublishSubject.create();
    }

    public void send(Object object) {
        subject.onNext(object);
    }

    public Observable<Object> getEvents() {
        return subject;
    }

    public boolean hasObservers() {
        return subject.hasObservers();
    }
}
