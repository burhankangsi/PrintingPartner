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

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.Tab_text_1, R.string.Tab_text_2};
    private Context mContext;
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();


    public SectionsPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        Fragment fragment = null;
//        switch (position)
//        {
//            case 0:
//                fragment = new PreviousOrdersFrag();
//                break;
//
//            case 1:
//                fragment = new RecentOrdersFrag();
//                break;
//        }
//        return fragment;

        Fragment fragment = null;
        if (position == 0) {
            fragment = new RecentOrdersFrag();
        } else if (position == 1) {
            fragment = new PreviousOrdersFrag();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
     //  return mContext.getResources().getString(TAB_TITLES[position]);

     //   return fragmentTitle.get(position);

        switch (position)
        {
            case 0:
                return "Recent Orders";

            case 1:
                return "Previous Orders";
        }
        return null;

    }

    @Override
    public int getCount() {
        // Show 2 pages
         return 2;
        //return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title)
    {
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }
}
