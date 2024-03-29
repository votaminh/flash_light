package com.vinalinux.findphone.base.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.vinalinux.findphone.App
import com.vinalinux.findphone.dialog.DialogShowAdsLoading
import com.vinalinux.findphone.utils.LocaleHelper
import com.vinalinux.findphone.utils.SpManager
import com.vinalinux.findphone.utils.getDeviceLanguage
import com.vinalinux.findphone.utils.setAppLanguage

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {
    lateinit var viewBinding: V

    private val dialogLoadingAd by lazy { DialogShowAdsLoading(this) }

    open fun onBack() {
        finish()
    }

    override fun attachBaseContext(newBase: Context?) {
        val locale = getDeviceLanguage()
        val language = SpManager.getInstance(this).getLanguage()
//            CommonSharedPreferences.getInstance().getString(DataLocal.KEY_LANGUAGE, locale)
//                ?: locale
        super.attachBaseContext(LocaleHelper.onAttach(newBase, language.languageCode))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        setAppLanguage(SpManager.getInstance(this).getLanguage().languageCode)

        super.onCreate(savedInstanceState)
        hideSystemUI()
//        setStatusBarColor(ContextCompat.getColor(this, initStatusBarColor()))
        viewBinding = provideViewBinding()
        setContentView(viewBinding.root)
        initViews()
        initData()
        initObserver()
        handleOnBackPressed()
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }

    open fun initAdmobInterId(): String = ""
    open fun admobConfigLoadInter() = true

    open var isReloadInter = true

    open fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })
    }

//    open fun initStatusBarColor(): Int = R.color.colorPrimary

    fun replaceFragment(id: Int, fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(fragment::class.java.simpleName)
            replace(id, fragment, fragment::class.java.simpleName)
        }
    }

    fun addFragment(id: Int, fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(fragment::class.java.simpleName)
            add(id, fragment, fragment::class.java.simpleName)
        }
    }


//    private fun hideSystemUI() {
//        val decorView = window.decorView
//        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        decorView.systemUiVisibility = uiOptions
//    }
//
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) {
//            hideSystemUI()
//        }
//    }

    abstract fun provideViewBinding(): V

    open fun initViews() {}
    open fun initData() {}
    open fun initObserver() {}

    fun showLoading() {

    }

    fun showToast(mes: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, mes, duration).show()
    }

    fun hideLoading() {

    }

    private fun setStatusBarColor(color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }
    }
}