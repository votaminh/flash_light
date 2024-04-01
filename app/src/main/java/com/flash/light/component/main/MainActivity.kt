package com.flash.light.component.main

import android.app.Activity
import android.content.Intent
import androidx.viewpager2.widget.ViewPager2
import com.flash.light.base.activity.BaseActivity
import com.flash.light.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }


    override fun provideViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        buildPagerHome()
    }

    private fun buildPagerHome() {
        viewBinding.viewPager2.run {
            offscreenPageLimit = 4
            isUserInputEnabled = true
            adapter = ViewPagerAdapter(this@MainActivity)
        }
    }
}