package com.wbb.kotlin_rxbus.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wbb.kotlin_rxbus.R
import com.wbb.kotlin_rxbus.RxBus
import com.wbb.kotlin_rxbus.data.User
import com.wbb.kotlin_rxbus.event.UserEvent
import com.wbb.kotlin_rxbus.event.UserListEvent
import com.wbb.kotlin_rxbus.registerInBus
import kotlinx.android.synthetic.main.activity_one.*
import org.jetbrains.anko.startActivity

class OneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one)

        initView()
        initObserve()
    }

    private fun initView() {
        val user = User("小明", 15)

        mBtnActivity.setOnClickListener {
            RxBus.send(UserEvent(user))
            startActivity<TwoActivity>()
        }

        mBtnFragment.setOnClickListener {
            startActivity<ThreeActivity>()
        }
    }

    private fun initObserve() {
        RxBus.observe<UserListEvent>()
                .subscribe { t: UserListEvent? ->
                    kotlin.run {
                        mContent.text=t?.users?.toString()
                    }
                }.registerInBus(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.unRegister(this)
    }

}
