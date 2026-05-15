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
            if (propietario != null) {
                binding.etNombre.setText(propietario.getNombre());
                binding.etApellido.setText(propietario.getApellido());
                binding.etDni.setText(propietario.getDni());
                binding.etTelefono.setText(propietario.getTelefono());
                binding.etEmail.setText(propietario.getEmail());
            }
        });

        propietarioViewModel.getUpdateState().observe(getViewLifecycleOwner(), status -> {
            switch (status.getStatus()) {
                case WARNING:
                    //warning cambiar de color los inputs
                    if ("nombre".equals(status.getFieldName())) {
                        binding.etNombre.requestFocus();
                        binding.etNombre.setError("Campo obligatorio");
                    }
                    if ("telefono".equals(status.getFieldName())) {
                        binding.etTelefono.requestFocus();
                        binding.etTelefono.setError("Campo obligatorio");
                    }
                    if ("apellido".equals(status.getFieldName())) {
                        binding.etApellido.requestFocus();
                        binding.etApellido.setError("Campo obligatorio");
                    }
                    break;
                case LOADING:
                    setEnabledInput(false);
                    binding.btnEditarGuardar.setEnabled(false);
                    binding.btnEditarGuardar.setText("CARGANDO");
                    // deberiamos poner el circulito cargando y no cambiar el nombre del boton a cargando
                    break;
                case SUCCESS:
                    setEnabledInput(false);
                    binding.btnEditarGuardar.setEnabled(true);
                    binding.btnEditarGuardar.setText("EDITAR");
                    //desactivar el circulito de cargando
                    break;
                case ERROR:
                    setEnabledInput(true);
                    binding.btnEditarGuardar.setEnabled(true);
                    binding.btnEditarGuardar.setText("GUARDAR");
                    //desacttivar el circulito de cargando
                    break;
            }
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
        propietarioViewModel.updatePropietario(
                binding.etNombre.getText().toString().trim(),
                binding.etApellido.getText().toString().trim(),
                binding.etTelefono.getText().toString().trim()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
