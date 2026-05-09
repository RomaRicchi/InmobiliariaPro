package com.roma.inmobiliariapro;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.roma.inmobiliariapro.databinding.ActivityMainBinding;
import com.roma.inmobiliariapro.ui.login.LoginActivity;
import com.roma.inmobiliariapro.ui.viewsModels.PropietarioViewModel;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferesManager sharedPreferesManager;
    private ActivityMainBinding binding;
    private PropietarioViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        vm = new ViewModelProvider(this).get(PropietarioViewModel.class);

        sharedPreferesManager = new SharedPreferesManager(this);

        if (sharedPreferesManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        vm.getPropietario();


        setSupportActionBar(binding.appBarMain.toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_inmuebles, R.id.nav_inquilinos, R.id.nav_perfil, R.id.nav_contratos)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationView navigationView = binding.navView;
        NavigationUI.setupWithNavController(navigationView, navController);

        // Manejo del Logout
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                logout();
                return true;
            }
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                binding.drawerLayout.closeDrawers();
            }
            return handled;
        });

        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.textName);
        TextView email = headerView.findViewById(R.id.textEmail);
        ImageView profileImage = headerView.findViewById(R.id.imageProfile);

        vm.getPropietarioMutable().observe(this, propietario -> {
            if(propietario != null) {
                name.setText(propietario.getFullName());
                email.setText(propietario.getEmail());

                Glide.with(this)
                        .load(R.mipmap.ic_launcher_round) // Usamos el launcher como placeholder/default
                        .circleCrop()
                        .into(profileImage);
            }
        });
    }

    private void logout() {
        sharedPreferesManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        NavigationView navView = findViewById(R.id.nav_view);
        if (navView == null) {
            getMenuInflater().inflate(R.menu.overflow, menu);
        }
        return result;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.nav_settings) {
//            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//            navController.navigate(R.id.nav_settings);
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
