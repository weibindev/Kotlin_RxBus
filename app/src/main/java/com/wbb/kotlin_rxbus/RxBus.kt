package com.wbb.kotlin_rxbus

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.ReplaySubject

/**
 *
 * @author vico
 * @date 2018-10-15
 */
object RxBus {
    private val TAG = javaClass.simpleName

    /**
     * 用于保存RxBus事件的所有订阅，并在需要时正确的取消订阅。
     */
    private val disposablesMap: HashMap<Any, CompositeDisposable?> by lazy {
        HashMap<Any, CompositeDisposable?>()
    }

    /**
     * 避免直接使用此属性，因为它仅在内联函数中使用而暴露
     */
    val rxBus = ReplaySubject.create<Any>().toSerialized()


    /**
     * 向RxBus发送一个事件，这个事件可以来自任意一个线程
     */
    fun send(event: Any) {
        rxBus.onNext(event)
    }

    /**
     * 订阅特定类型T的事件。可以从任何线程调用
     */
    inline fun <reified T : Any> observe(): Observable<T> {
        return rxBus.ofType(T::class.java)
    }

    /**
     * 从RxBus事件中取消注册订阅者
     * 调用订阅的取消订阅方法
     */
    fun unRegister(disposable: Any) {
        val compositeDisposable = disposablesMap[disposable]
        if (compositeDisposable == null) {
            Log.w(TAG, "Trying to unregister subscriber that wasn't registered")
        } else {
            compositeDisposable.clear()
            disposablesMap.remove(disposable)
        }
    }

    internal fun register(disposable: Any, composite: Disposable) {
        var compositeDisposable = disposablesMap[disposable]
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable.add(composite)
        disposablesMap[disposable] = compositeDisposable
    }
}

/**
 * 注册订阅以便以后正确注销它以避免内存泄漏
 * @param disposable 拥有此订阅的订阅对象
 */
fun Disposable.registerInBus(disposable: Any) {
    RxBus.register(disposable, this)
}