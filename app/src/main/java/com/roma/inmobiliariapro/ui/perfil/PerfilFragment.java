package com.roma.inmobiliariapro.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.roma.inmobiliariapro.databinding.FragmentPerfilBinding;
import com.roma.inmobiliariapro.ui.viewsModels.PropietarioViewModel;
import com.roma.inmobiliariapro.utils.SharedPreferesManager;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PropietarioViewModel propietarioViewModel;
    private SharedPreferesManager sharedPreferesManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        propietarioViewModel = new ViewModelProvider(requireActivity()).get(PropietarioViewModel.class);
        sharedPreferesManager = new SharedPreferesManager(requireContext());

        setupDarkModeSwitch();
        setupObservers();
        setupListeners();

        propietarioViewModel.getPropietario();

        return binding.getRoot();
    }

    private void setupDarkModeSwitch() {
        binding.switchDarkMode.setChecked(sharedPreferesManager.isDarkMode());
        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked != sharedPreferesManager.isDarkMode()) {
                sharedPreferesManager.setDarkMode(isChecked);
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    private void setupObservers() {

        propietarioViewModel.getPropietarioMutable().observe(getViewLifecycleOwner(), propietario -> {
            binding.etNombre.setText(propietario.getNombre());
            binding.etApellido.setText(propietario.getApellido());
            binding.etDni.setText(propietario.getDni());
            binding.etTelefono.setText(propietario.getTelefono());
            binding.etEmail.setText(propietario.getEmail());

            if(binding.btnEditarGuardar.getText().toString().equalsIgnoreCase("CARGANDO")) {
                binding.btnEditarGuardar.setText("EDITAR");
                binding.btnEditarGuardar.setEnabled(true);
            }
        });

        propietarioViewModel.getMsgErrorMutable().observe(getViewLifecycleOwner(), error -> {
            if(error != null) Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
        });
    }

    private void setupListeners() {
        binding.btnEditarGuardar.setOnClickListener(v -> {

            String textBtn = binding.btnEditarGuardar.getText().toString();

            if(textBtn.equalsIgnoreCase("EDITAR")) {
                setEnabledInput(true);
                binding.btnEditarGuardar.setText("GUARDAR");
            }

            if(textBtn.equalsIgnoreCase("GUARDAR")) {
                setEnabledInput(false);
                binding.btnEditarGuardar.setText("CARGANDO");
                binding.btnEditarGuardar.setEnabled(false);
                guardarCambios();
            }
        });

        binding.btnEditarPassword.setOnClickListener(v -> {
            CambiarClaveFragment dialog = new CambiarClaveFragment();

            dialog.show(getParentFragmentManager(), "cambiar_clave");
        });
    }

    private void setEnabledInput(boolean enabled) {
        binding.etNombre.setEnabled(enabled);
        binding.etApellido.setEnabled(enabled);
        binding.etTelefono.setEnabled(enabled);
    }

    private void guardarCambios() {
        String nombre = binding.etNombre.getText().toString();
        String apellido = binding.etApellido.getText().toString();
        String telefono = binding.etTelefono.getText().toString();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(getContext(), "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        propietarioViewModel.updatePropietario(nombre, apellido, telefono);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
