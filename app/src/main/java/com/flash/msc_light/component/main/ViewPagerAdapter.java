package com.flash.msc_light.component.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.flash.msc_light.component.feature.FlashAlertCloneMscFragment;
import com.flash.msc_light.component.feature.FlashBlinksCloneMscFragment;
import com.flash.msc_light.component.feature.FlashLightCloneMscFragment;
import com.flash.msc_light.component.feature.SettingCloneMscFragment;

import java.util.List;

public final class ViewPagerAdapter extends FragmentStateAdapter {
    List<Fragment> fragments = List.of(new FlashAlertCloneMscFragment(), new FlashLightCloneMscFragment(), new FlashBlinksCloneMscFragment(), new SettingCloneMscFragment());
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
