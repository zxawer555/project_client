package self.edu.project_client.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import self.edu.project_client.R;
import self.edu.project_client.method.database.DatabaseHandler;
import self.edu.project_client.method.service.ElderlyLocationService;
import self.edu.project_client.method.sharepreference.CustomerSharePreference;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static ViewPager viewPager;
    public static ViewPagerAdapter viewPagerAdapter;

    public static TextView tvLogin;
    public static TextView tvSignUp;
    public static ProgressBar loadingProgressBar;

    public static ImageView imgUserIcon;
    public static TextView tvUserName;
    public static TextView tvEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // P.S.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        CustomerSharePreference.mContext = this;

        loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);

        viewPagerAdapter.addFragment(new GeneralFragment());
        viewPagerAdapter.addFragment(new LoginFragment());
        viewPagerAdapter.addFragment(new RegisterFragment());
        viewPagerAdapter.addFragment(new ElderlyDetailFragment());

        viewPager.setAdapter(viewPagerAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        tvLogin = (TextView) header.findViewById(R.id.tvLogin);
        tvSignUp = (TextView) header.findViewById(R.id.tvSignUp);

        imgUserIcon = (ImageView) header.findViewById(R.id.imgUserIcon);
        tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        tvEmail = (TextView) header.findViewById(R.id.tvEmail);

        if (CustomerSharePreference.getInstance().getBooleanPreference(CustomerSharePreference.Login)) {
            String userName = CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.First_Name) +
                    " " + CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.Last_Name);
            String email = CustomerSharePreference.getInstance().getStringPreference(CustomerSharePreference.Email);

            setUserInfo("", userName, email);
        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }

        changeFragment(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_command) {

        }

        closeDrawer();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.tvLogin:
                if (CustomerSharePreference.getInstance().getBooleanPreference(CustomerSharePreference.Login)) {
                    CustomerSharePreference.getInstance().setBooleanPreference(CustomerSharePreference.Login, false);
                    CustomerSharePreference.getInstance().setStringPreference(CustomerSharePreference.Email, "");
                    CustomerSharePreference.getInstance().setStringPreference(CustomerSharePreference.First_Name, "");
                    CustomerSharePreference.getInstance().setStringPreference(CustomerSharePreference.Last_Name, "");
                    DatabaseHandler.getInstance(this).deleteAllContact();

                    Intent intent = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    changeFragment(1);
                }
                break;

            case R.id.tvSignUp:
                changeFragment(2);
                break;
        }

        closeDrawer();
    }

    public void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public static void changeFragment(int id) {
        viewPager.setCurrentItem(id);
        viewPagerAdapter.notifyDataSetChanged();
    }

    public static void setUserInfo(String photoLink, String userName, String email) {
        tvSignUp.setVisibility(View.GONE);

        tvLogin.setText(viewPager.getResources().getString(R.string.logout));

        tvUserName.setText(userName);
        tvEmail.setText(email);
    }
}
