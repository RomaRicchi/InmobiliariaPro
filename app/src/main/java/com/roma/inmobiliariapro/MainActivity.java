package com.roma.inmobiliariapro;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.roma.inmobiliariapro.databinding.ActivityMainBinding;
import com.roma.inmobiliariapro.ui.BaseActivity;
import com.roma.inmobiliariapro.ui.login.LoginActivity;
import com.roma.inmobiliariapro.ui.viewsModels.PropietarioViewModel;
import com.roma.inmobiliariapro.utils.ColorUtil;
import com.roma.inmobiliariapro.utils.MessageManager;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

public class MainActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPreferesManager sharedPreferesManager;
    private ActivityMainBinding binding;
    private PropietarioViewModel propietarioVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferesManager = new SharedPreferesManager(this);
        propietarioVM = new ViewModelProvider(this).get(PropietarioViewModel.class);

        // Aplicar tema guardado (Modo Oscuro/Claro)
        if (sharedPreferesManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Cargar datos del propietario
        propietarioVM.getPropietario();

        setSupportActionBar(binding.appBarMain.toolbar);

        if (binding.appBarMain.fab != null) {
            binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Acción personalizada", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).setAnchorView(R.id.fab).show());
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        // Destinos de nivel superior
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_inmuebles, R.id.nav_inquilinos, R.id.nav_perfil, R.id.nav_contratos)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Fix Visual: Forzamos el ícono de menú hamburguesa personalizado y color blanco
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (mAppBarConfiguration.getTopLevelDestinations().contains(destination.getId())) {
                binding.appBarMain.toolbar.setNavigationIcon(R.drawable.menu_hamburguesa);
                binding.appBarMain.toolbar.setNavigationIconTint(Color.WHITE);
            }
        });

        // Configuración de la navegación lateral (Drawer)
        NavigationView navigationView = binding.navView;
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configuración de la navegación inferior (Bottom Navigation) - CORREGIDO
        BottomNavigationView bottomNavView = binding.appBarMain.contentMain.bottomNavView;
        if (bottomNavView != null) {
            NavigationUI.setupWithNavController(bottomNavView, navController);
        }

        // Manejo del Logout con confirmación
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                showLogoutConfirmation();
                return true;
            }
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                binding.drawerLayout.closeDrawers();
            }
            return handled;
        });

        // Configurar Header del Drawer
        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.textName);
        TextView email = headerView.findViewById(R.id.textEmail);
        ImageView profileImage = headerView.findViewById(R.id.imageProfile);

        propietarioVM.getPropietarioMutable().observe(this, propietario -> {
            if (propietario != null) {
                String nombreCompleto = (propietario.getNombre() != null) ? 
                        (propietario.getNombre() + " " + propietario.getApellido()) : 
                        propietario.getFullName();
                
                name.setText(nombreCompleto);
                email.setText(propietario.getEmail());

                Glide.with(this)
                        .load(R.drawable.user) 
                        .circleCrop()
                        .placeholder(R.drawable.user)
                        .into(profileImage);
            }
        });

        // Observador global para mensajes del sistema
        MessageManager.getUiMessageMutable().observe(this, uiMessage -> {
            if (uiMessage != null) {
                Snackbar snackbar = Snackbar.make(binding.getRoot(), uiMessage.message, Snackbar.LENGTH_SHORT);
                View view = snackbar.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                params.gravity = Gravity.TOP;
                params.topMargin = 250;
                view.setLayoutParams(params);
                view.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(this, ColorUtil.getColorByStatus(uiMessage.status))));
                snackbar.show();
            }
        });
    }

    private void showLogoutConfirmation() {
        Snackbar.make(binding.getRoot(), "¿Desea cerrar la sesión?", Snackbar.LENGTH_LONG)
                .setAction("Cerrar Sesión", v -> logout())
                .show();
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
