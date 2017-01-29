package cn.ben.learnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

@SuppressWarnings("UnusedAssignment")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Observable的创建
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                //执行一些其他操作
                //.............
                //执行完毕，触发回调，通知观察者
                e.onNext("我来发射数据");
            }
        });

        // Observer的创建
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                //观察者接收到通知,进行相关操作
                System.out.println("我接收到数据了");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        // 订阅
        observable.subscribe(observer);

        // Observable的其他创建方式
        // just中传递的参数将直接在Observer的onNext()方法中接收到
        observable = Observable.just("Hello");

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Hello" + i);
        }
        // 注意：Collection接口是Iterable接口的子接口
        // 所以所有Collection接口的实现类都可以作为Iterable对象直接传入fromIterable()方法。
        observable = Observable.fromIterable(list);

        // 当观察者订阅时，才创建Observable
        // 并且针对每个观察者创建都是一个新的Observable
        // TODO: 2017/1/29 以何种方式创建这个Observable对象，当满足回调条件后，就会进行相应的回调。
        observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just("hello");
            }
        });

        Observable<Long> longObservable = Observable.interval(2, TimeUnit.SECONDS);

        // 第二个为发送的个数，如果为0则不发送，负数则抛异常
        Observable<Integer> intObservable = Observable.range(1, 20);

        // 在一个给定的延迟后发射一个特殊的值
        longObservable = Observable.timer(2, TimeUnit.SECONDS);

        intObservable = Observable.just(123).repeat();

        // 以Consumer为例，我们可以实现简便式的观察者模式
        Observable.just("hello").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        });

        intObservable = Observable.just("hello").map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return s.length();
            }
        });

        Observable<Object> objObservable = Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        });

        Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object s) throws Exception {
                String newStr = (String) s;
                return newStr.charAt(5) - '0' > 5;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println((String) o);
            }
        });

        Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).take(5).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object s) throws Exception {
                System.out.println((String) s);
            }
        });

        Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).take(5).doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("准备工作");
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object s) throws Exception {
                System.out.println((String) s);
            }
        });
    }
}
