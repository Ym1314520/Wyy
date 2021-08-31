package com.example.wyy.login_register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.wyy.R;
import com.example.wyy.login_register.login_fragment.CountLogin;
import com.example.wyy.login_register.login_fragment.PhoneLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    ViewPager2 vp2;
    BottomNavigationView bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        vp2=findViewById(R.id.login_vp2);
        bottom=findViewById(R.id.bottom);
        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new CountLogin());
        fragments.add(new PhoneLogin());
        vp2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });

        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottom.getMenu().getItem(position).setChecked(true);
            }
        });

        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                vp2.setCurrentItem(item.getOrder());
                return true;
            }
        });
    }
}
