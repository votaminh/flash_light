package com.flash.light.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.flash.light.utils.FlashHelper
import com.flash.light.utils.SpManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Provides
    @Singleton
    fun provideSpManager(@ApplicationContext context: Context): SpManager {
        return SpManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideFlashHelper(): FlashHelper {
        return FlashHelper.getInstance()
    }
}