package com.flash.light.component.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.flash.light.component.alert.FlashAlertFragment;
import com.flash.light.component.light.FlashLightFragment;

public final class ViewPagerAdapter extends FragmentStateAdapter {
    public int getItemCount() {
        return 4;
    }

    public ViewPagerAdapter(AppCompatActivity appCompatActivity) {
        super((FragmentActivity) appCompatActivity);
    }

    @NonNull
    public Fragment createFragment(int i) {
        if (i == 0) {
            return new FlashAlertFragment();
        }
        if (i == 1) {
            return new FlashLightFragment();
        }
        if (i == 2) {
            return new FlashAlertFragment();
        }
        if (i != 3) {
            return new FlashAlertFragment();
        }
        return new FlashAlertFragment();
    }
}
