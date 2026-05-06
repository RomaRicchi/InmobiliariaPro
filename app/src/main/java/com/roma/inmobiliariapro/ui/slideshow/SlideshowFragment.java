package com.roma.inmobiliariapro.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.roma.inmobiliariapro.data.model.response.Propietario;
import com.roma.inmobiliariapro.databinding.FragmentSlideshowBinding;
import com.roma.inmobiliariapro.utils.SessionManager;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SlideshowViewModel slideshowViewModel;
    private SessionManager sessionManager;
    private boolean isEditing = false;
    private Propietario currentPropietario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        sessionManager = new SessionManager(requireContext());

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupDarkModeSwitch();
        setupObservers();
        setupListeners();

        slideshowViewModel.cargarPerfil();

        return root;
    }

    private void setupDarkModeSwitch() {
        binding.switchDarkMode.setChecked(sessionManager.isDarkMode());
        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked != sessionManager.isDarkMode()) {
                sessionManager.setDarkMode(isChecked);
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    private void setupObservers() {
        slideshowViewModel.getPropietario().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                currentPropietario = propietario;
                binding.etNombre.setText(propietario.getNombre());
                binding.etApellido.setText(propietario.getApellido());
                binding.etDni.setText(propietario.getDni());
                binding.etTelefono.setText(propietario.getTelefono());
                binding.etEmail.setText(propietario.getEmail());
            }
        });
    }

    private void setupListeners() {
        binding.btnEditarGuardar.setOnClickListener(v -> {
            if (!isEditing) {
                setEditingEnabled(true);
                binding.btnEditarGuardar.setText("Guardar");
                isEditing = true;
            } else {
                guardarCambios();
            }
        });
    }

    private void setEditingEnabled(boolean enabled) {
        binding.etNombre.setEnabled(enabled);
        binding.etApellido.setEnabled(enabled);
        binding.etTelefono.setEnabled(enabled);
    }

    private void guardarCambios() {
        if (currentPropietario != null) {
            currentPropietario.setNombre(binding.etNombre.getText().toString());
            currentPropietario.setApellido(binding.etApellido.getText().toString());
            currentPropietario.setTelefono(binding.etTelefono.getText().toString());

            slideshowViewModel.actualizarPerfil(currentPropietario);
            
            setEditingEnabled(false);
            binding.btnEditarGuardar.setText("Editar");
            isEditing = false;
            Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
