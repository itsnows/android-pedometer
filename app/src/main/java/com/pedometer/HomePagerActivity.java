package com.pedometer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePagerActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private ViewPager viewPager;
    private ViewPagerFragmentPagerAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            navigation.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pager);
        initView();
    }

    private void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        adapter = new ViewPagerFragmentPagerAdapter();
        adapter.addTabItem(getString(R.string.homepage_tabs_status), new StatusFragment());
        adapter.addTabItem(getString(R.string.homepage_tabs_statistics), new StatisticsFragment());
        adapter.addTabItem(getString(R.string.homepage_tabs_setting), new SettingFragment());
        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerFragmentPagerAdapter extends FragmentPagerAdapter {
        private final String KEY_TITLE = "title";
        private final String KEY_FRAGMENT = "fragment";
        private List<Map<String, Object>> tabs;

        public ViewPagerFragmentPagerAdapter() {
            super(getSupportFragmentManager());
            this.tabs = new ArrayList<>(0);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) tabs.get(position).get(KEY_FRAGMENT);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return (CharSequence) tabs.get(position).get(KEY_TITLE);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        }

        public void addTabItem(CharSequence title, Fragment fragment) {
            Map<String, Object> tab = new HashMap<>(0);
            tab.put(KEY_TITLE, title);
            tab.put(KEY_FRAGMENT, fragment);
            tabs.add(tab);
        }
    }

}
