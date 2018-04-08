package com.entrepreneurs.entrepreneurs.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

;import com.entrepreneurs.entrepreneurs.R;
import com.entrepreneurs.entrepreneurs.entities.Member;
import com.entrepreneurs.entrepreneurs.fragments.GroupProgress;
import com.entrepreneurs.entrepreneurs.fragments.MembersProgress;
import com.entrepreneurs.entrepreneurs.fragments.PersonalDepositHistory;
import com.entrepreneurs.entrepreneurs.fragments.PersonalProgress;
import com.entrepreneurs.entrepreneurs.entities.PaymentInformation;
import com.entrepreneurs.entrepreneurs.repository.Session;
import com.entrepreneurs.entrepreneurs.util.Constants;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PersonalProgress.OnFragmentInteractionListener,
        PersonalDepositHistory.OnListFragmentInteractionListener,
        MembersProgress.OnFragmentInteractionListener, GroupProgress.OnFragmentInteractionListener {

    // hold the fragment that is currently being displayed.
    private Class currentlyDisplayedFragment;
    // hold the currently displayed menu item
    private MenuItem currentlyDisplayedDrawerMenuItem;
    // number of times the 'onback' is pressed
    private int onBackCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        Member currentMember = (Member) Session.getSession().getAttribute(Constants.CURRENT_MEMBER_KEY);
        if(!currentMember.getName().equals(Constants.MEMBER_1)){
            // hide the floating action button if the user is not the admin.
            // the button is used to load the administrator functions such as manually adding payment, etc
            fab.setVisibility(View.INVISIBLE);
        }
        fab.setOnClickListener(view -> {
            // launch the manual payment activity
            Intent intent = new Intent(Dashboard.this, ManualPayment.class);
            Dashboard.this.startActivity(intent);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.currentlyDisplayedFragment = this.currentlyDisplayedFragment == null ? PersonalProgress.class : this.currentlyDisplayedFragment;

        Fragment fragment;
        try {
            fragment = (Fragment) this.currentlyDisplayedFragment.newInstance();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        // close the drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        // load the currently selected menu drawer menu item
        this.onNavigationItemSelected(this.currentlyDisplayedDrawerMenuItem);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, Entrepreneurs.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = null == item ? R.id.my_progress : item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        this.currentlyDisplayedDrawerMenuItem = item;

        if (id == R.id.my_progress) {
            fragmentClass = PersonalProgress.class;
        } else if (id == R.id.my_deposit_history) {
            fragmentClass = PersonalDepositHistory.class;
        } else if (id == R.id.members_progress) {
            fragmentClass = MembersProgress.class;
        } else if (id == R.id.group_progress) {
            fragmentClass = GroupProgress.class;
        } else {
            fragmentClass = PersonalProgress.class;
        }

        try {

            // show the progress bar indicating loading
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.flContent, fragment)
                    .commit();

            // store the fragment currently being displated
            this.currentlyDisplayedFragment = fragmentClass;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(PaymentInformation item) {

    }*/

    @Override
    public void onResume() {
        super.onResume();
        restoreFragment();
    }

    private void restoreFragment() {
        try {
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContent, (Fragment) this.currentlyDisplayedFragment.newInstance())
                    .commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.restoreFragment();
    }


    public void viewPaymentHistory(View view) {
        int id = view.getId();
        String memberName = null;
        switch (id) {
            case R.id.member_2_payment_history:
                memberName = Constants.MEMBER_2;
                break;
            case R.id.member_3_payment_history:
                memberName = Constants.MEMBER_3;
                break;
                default:
                    memberName = Constants.MEMBER_1;
                    break;

        }

        PersonalDepositHistory personalDepositHistory = new PersonalDepositHistory();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.MEMBER_HISTORY_KEY, memberName);

        personalDepositHistory.setArguments(bundle);

        FragmentTransaction replace = this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, personalDepositHistory);
        replace.commit();
    }

    public void loadMembersProgress(View view) {
        FragmentTransaction replace = this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContent, new MembersProgress());
        replace.commit();
    }


    @Override
    public void setActivityTitle(String string) {
        this.setTitle(string);
    }
}
