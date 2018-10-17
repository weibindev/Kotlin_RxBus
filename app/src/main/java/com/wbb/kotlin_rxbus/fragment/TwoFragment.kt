package com.wbb.kotlin_rxbus.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wbb.kotlin_rxbus.R
import com.wbb.kotlin_rxbus.RxBus
import com.wbb.kotlin_rxbus.event.MessageOneEvent
import com.wbb.kotlin_rxbus.event.MessageTwoEvent
import com.wbb.kotlin_rxbus.registerInBus
import kotlinx.android.synthetic.main.fragment_two.*

/**
 *
 * @author vico
 * @date 2018-10-17
 */
class TwoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserve()
    }

    private fun initView() {
        mBtnFragment.setOnClickListener {
            RxBus.send(MessageTwoEvent("来自TwoFragment的消息"))
        }
    }

    private fun initObserve() {
        RxBus.observe<MessageOneEvent>().subscribe { t: MessageOneEvent? ->
            kotlin.run {
                mTvTwoFragment.text = t?.msg
            }
        }.registerInBus(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.unRegister(this)
    }
}