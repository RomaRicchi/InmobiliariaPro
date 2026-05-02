package com.roma.inmobiliariapro.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.roma.inmobiliariapro.data.model.Propietario;
import com.roma.inmobiliariapro.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SlideshowViewModel slideshowViewModel;
    private boolean isEditing = false;
    private Propietario currentPropietario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupObservers();
        setupListeners();

        slideshowViewModel.cargarPerfil();

        return root;
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
        // DNI y Email suelen ser solo lectura en este tipo de sistemas
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
