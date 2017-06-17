package dev.tornaco.tasker.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import dev.tornaco.tasker.app.fragment.HowToPage;
import dev.tornaco.tasker.app.fragment.ModulePackageViewer;

public class MainActivity extends AppCompatActivity {

    private Fragment mPageViewerPage, mHowToPage, mSettingsPage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showPage(mPageViewerPage);
                    return true;
                case R.id.navigation_dashboard:
                    if (mHowToPage == null) {
                        mHowToPage = new HowToPage();
                    }
                    showPage(mHowToPage);
                    return true;
                case R.id.navigation_notifications:
                    if (mSettingsPage == null) {
                        mSettingsPage = new HowToPage();
                    }
                    showPage(mSettingsPage);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        mPageViewerPage = new ModulePackageViewer();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, mPageViewerPage);
        transaction.commit();
    }

    private void showPage(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }
}
