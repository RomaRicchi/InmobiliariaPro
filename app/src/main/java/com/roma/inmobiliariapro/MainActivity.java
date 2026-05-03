package com.roma.inmobiliariapro;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.roma.inmobiliariapro.data.repository.PropietarioRepository;
import com.roma.inmobiliariapro.databinding.ActivityMainBinding;
import com.roma.inmobiliariapro.utils.SessionManager;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        if (binding.appBarMain.fab != null) {
            binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).setAnchorView(R.id.fab).show());
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_transform, R.id.nav_reflow, R.id.nav_slideshow, R.id.nav_settings)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationView navigationView = binding.navView;
        if (navigationView != null) {
            NavigationUI.setupWithNavController(navigationView, navController);

            View headerView = navigationView.getHeaderView(0);
            TextView name = headerView.findViewById(R.id.textName);
            TextView email = headerView.findViewById(R.id.textEmail);
            ImageView profileImage = headerView.findViewById(R.id.imageProfile);

            SessionManager sessionManager = new SessionManager(this);
            PropietarioRepository repository = new PropietarioRepository(sessionManager);

            repository.obtenerPerfil().observe(this, propietario -> {
                if (propietario != null) {
                    name.setText(propietario.getNombre() + " " + propietario.getApellido());
                    email.setText(propietario.getEmail());
                    
                    // Cargar imagen de perfil con Glide
                    Glide.with(this)
                            .load(R.mipmap.ic_launcher_round) // Usamos el launcher como placeholder/default
                            .circleCrop()
                            .into(profileImage);
                }
            });
        }

        BottomNavigationView bottomNavigationView = binding.appBarMain.contentMain.bottomNavView;
        if (bottomNavigationView != null) {
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
