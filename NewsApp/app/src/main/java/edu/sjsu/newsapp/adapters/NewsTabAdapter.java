package edu.sjsu.newsapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

import edu.sjsu.newsapp.fragments.SingleNewsTabFragment;

/**
 * Created by akshaymathur on 8/15/17.
 */

public class NewsTabAdapter extends FragmentStatePagerAdapter {

    List<String> mSectionsList;


    public NewsTabAdapter(FragmentManager fragmentManager, List<String> sectionsList){
        super(fragmentManager);
        mSectionsList = sectionsList;
    }
    @Override
    public Fragment getItem(int position) {
        Log.d("CHECK", mSectionsList.get(position) + " is being loaded");
        return SingleNewsTabFragment.newInstance(mSectionsList.get(position));
    }

    @Override
    public int getCount() {
        return mSectionsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSectionsList.get(position);
    }
}
