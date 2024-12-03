package com.fyp.confit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.fyp.confit.ui.home.HomeFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private String LoggedInWith;
    public User sessionUser;
    public static BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setStatusBarTransparent();

        bottomNavigationView = findViewById(R.id.nav_view);

        sessionUser=new User();

        Intent i= getIntent();
        Bundle b= new Bundle();
        b= i.getExtras();
        sessionUser= (User) b.getSerializable("sessionUser");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.logout_menu_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Leaving ?")
                .setMessage("Are you sure you want to Logout ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with sending operation
                        SharedPreferences pref;
                        SharedPreferences.Editor editor;

                        pref = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE);

                        if (pref.getBoolean("LoggedIn", false)) {
                            editor = pref.edit();

                            editor.putBoolean("LoggedIn", false);
                            editor.putString("email", null);
                            editor.putString("password", null);
                            editor.putString("token", null);
                            editor.putString("source", null);

                            editor.apply();
                        }
                        Intent i1=new Intent(MainActivity.this, LoginPage.class);
                        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i1);

                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.

                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.logout_alert)
                .show();

        return true;
    }

    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor( Color.TRANSPARENT);
        }
    }

    @Override
    public void onBackPressed(){

        super.onBackPressed();
    }


}