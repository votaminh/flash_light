package com.flash.light.component.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.flash.light.component.alert.FlashAlertFragment;
import com.flash.light.component.blinks.FlashBlinksFragment;
import com.flash.light.component.light.FlashLightFragment;
import com.flash.light.component.setting.SettingFragment;

import java.util.List;

public final class ViewPagerAdapter extends FragmentStateAdapter {
    List<Fragment> fragments = List.of(new FlashAlertFragment(), new FlashLightFragment(), new FlashBlinksFragment(), new SettingFragment());
    public int getItemCount() {
        return fragments.size();
    }

    public ViewPagerAdapter(AppCompatActivity appCompatActivity) {
        super((FragmentActivity) appCompatActivity);
    }

    @NonNull
    public Fragment createFragment(int i) {
        return fragments.get(i);
    }
}
