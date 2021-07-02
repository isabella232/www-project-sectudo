package com.example.synradar_sectudo.helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.synradar_sectudo.learningmodule.ExpFragment;
import com.example.synradar_sectudo.learningmodule.IntroFragment;
import com.example.synradar_sectudo.learningmodule.MitFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch(position){
            case 1:
                return new ExpFragment();
            case 2:
                return new MitFragment();

        }
        return new IntroFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}