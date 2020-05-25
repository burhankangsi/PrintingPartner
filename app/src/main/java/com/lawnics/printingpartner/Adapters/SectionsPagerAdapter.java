package com.lawnics.printingpartner.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lawnics.printingpartner.PreviousOrdersFrag;
import com.lawnics.printingpartner.R;
import com.lawnics.printingpartner.RecentOrdersFrag;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.Tab_text_1, R.string.Tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new PreviousOrdersFrag();
                break;

            case 1:
                fragment = new RecentOrdersFrag();
                break;
        }
        return fragment;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       return mContext.getResources().getString(TAB_TITLES[position]);

//        switch (position)
//        {
//            case 0:
//                return "Previous Orders";
//
//            case 1:
//                return "Recent Orders";
//        }
//        return null;

    }

    @Override
    public int getCount() {
        // Show 2 pages
        return 2;
    }
}
