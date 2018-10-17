package com.wbb.kotlin_rxbus.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wbb.kotlin_rxbus.R
import com.wbb.kotlin_rxbus.RxBus
import com.wbb.kotlin_rxbus.data.User
import com.wbb.kotlin_rxbus.event.UserEvent
import com.wbb.kotlin_rxbus.event.UserListEvent
import com.wbb.kotlin_rxbus.registerInBus
import kotlinx.android.synthetic.main.activity_two.*

class TwoActivity : AppCompatActivity() {

    private lateinit var mUserList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        initView()
        initObserve()
    }

    private fun initView() {
        mUserList = mutableListOf()
        mUserList.add(User("小白", 11))
        mUserList.add(User("小红", 12))
        mUserList.add(User("小黑", 13))
        mUserList.add(User("小绿", 14))
        mBtnReturn.setOnClickListener {
            RxBus.send(UserListEvent(mUserList))
            finish()
        }
    }

    private fun initObserve() {
        RxBus.observe<UserEvent>()
                .subscribe { t: UserEvent? ->
                    kotlin.run {
                        mContent.text = "name:${t?.user?.name} age:${t?.user?.age}"
                    }
                }
                .registerInBus(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.unRegister(this)
    }
}
