package iamhuy.bbike;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        List<Fragment> lstFm = new ArrayList<>();
        createFragment(lstFm);

        SampleFragmentPagerAdapter pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), lstFm, MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            int id = 0;
            switch (i) {
                case 0: id = R.drawable.logo_1;break;
                case 1: id = R.drawable.logo_2;break;
                case 2: id = R.drawable.logo_3;break;
                case 3: id = R.drawable.logo_4;break;
                default: break;
            }
            if (tab != null) {
                tab.setIcon(id);
            }
        }
    }

    private void createFragment(List<Fragment> lstFm) {
        lstFm.add(FirstFragment.newInstance(null, null));
        lstFm.add(SecondFragment.newInstance(null, null));
        lstFm.add(ThirdFragment.newInstance(null, null));
        lstFm.add(FourthFragment.newInstance(null, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
