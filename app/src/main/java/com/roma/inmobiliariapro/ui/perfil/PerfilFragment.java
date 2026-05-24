package com.roma.inmobiliariapro.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.roma.inmobiliariapro.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel perfilVM;
//    private SettingManager settingManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        perfilVM = new ViewModelProvider(requireActivity()).get(PerfilViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        settingManager = new SettingManager(requireContext());
//        boolean darkMode = settingManager.isDarkMode();
//        binding.switchDarkMode.setChecked(darkMode);

        setupObservers();
        setupListeners();

        perfilVM.getPropietario();
    }

    private void setupObservers() {
        perfilVM.getPropietarioMutable().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                binding.etNombre.setText(propietario.getNombre());
                binding.etApellido.setText(propietario.getApellido());
                binding.etDni.setText(propietario.getDni());
                binding.etTelefono.setText(propietario.getTelefono());
                binding.etEmail.setText(propietario.getEmail());
            }
        });

        perfilVM.getUpdateState().observe(getViewLifecycleOwner(), status -> {
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

//        binding.switchDarkMode.setOnCheckedChangeListener((btnView, isCheked) -> {
//            settingManager.setDarkMode(isCheked);
//            AppCompatDelegate.setDefaultNightMode(isCheked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
//        });
    }

    private void setEnabledInput(boolean enabled) {
        binding.etNombre.setEnabled(enabled);
        binding.etApellido.setEnabled(enabled);
        binding.etTelefono.setEnabled(enabled);
    }

    private void guardarCambios() {
        perfilVM.updatePropietario(
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
