package iamhuy.bbike;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import iamhuy.bbike.PageFragment;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;

    private Context context;
    private List<Fragment> myFragments;

    public SampleFragmentPagerAdapter(FragmentManager fm, List<Fragment> myFrags, Context context) {
        super(fm);
        myFragments = myFrags;
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
       /* switch (position) {
            case 0: return FirstFragment.newInstance(null,null);
            case 1: return SecondFragment.newInstance(null,null);
            case 2: return ThirdFragment.newInstance(null,null);
            case 3: return FourthFragment.newInstance(null,null);
            default: return null;
        }*/
        return myFragments.get(position);
    }


}