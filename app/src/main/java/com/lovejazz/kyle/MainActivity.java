package com.lovejazz.kyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Deleting toolbar
        try {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        } catch (NullPointerException e) {
            Log.d("MainRegisterActivity", "Toolbar produced null pointer exception");
        }
        setContentView(R.layout.activity_main);
        //CreatingAdapter for ViewPager
        final SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ViewPager pager = findViewById(R.id.main_pager);
        pager.setAdapter(pagerAdapter);
        //BottomNavigation items react on clicks.
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        pager.setCurrentItem(0);
                        break;
                    case R.id.nav_create:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.nav_settings:
                        pager.setCurrentItem(2);
                }
                return false;
            }
        });
        //Connecting ViewPager with BottomNavigationItem
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //Creating adapter, which will provide information to ViewPager
    private static class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PasswordsFragment();
                case 1:
                    return new CreateFragment();
                case 2:
                    return new SettingsFragment();
            }
            return new PasswordsFragment();
        }

        //Returns the number of fragments
        @Override
        public int getCount() {
            return 3;
        }
    }
}


