package zerotoonecom.n17r.mubr.data;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import zerotoonecom.n17r.mubr.MenuFragment;
import zerotoonecom.n17r.mubr.Time.NamazTimeFragment;
import zerotoonecom.n17r.mubr.R;

/**
 * Created by Nurdaulet Yeltayev (yeltayev22) on 14.07.2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context mContext;


    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            // returns NamazTimeFragment
            return new NamazTimeFragment();
        } else {
            // returns MenuFragment
            return new MenuFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.namaz_time);
        } else {
            return mContext.getString(R.string.menu);
        }
    }
}
