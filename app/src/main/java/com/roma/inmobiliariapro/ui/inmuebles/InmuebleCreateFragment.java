package com.roma.inmobiliariapro.ui.inmuebles;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roma.inmobiliariapro.databinding.FragmentInmuebleCreateBinding;
import com.roma.inmobiliariapro.ui.viewsModels.InmuebleViewModel;
import com.roma.inmobiliariapro.utils.FileUtil;

import java.io.File;

public class InmuebleCreateFragment extends Fragment {
    private FragmentInmuebleCreateBinding binding;
    private InmuebleViewModel vm;
    private ActivityResultLauncher<String> pickImageLauncher;
    private Uri imageUri;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleCreateBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(InmuebleViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();
        setupObservers();
    }

    private void setupListeners() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if(uri != null) {
                        imageUri = uri;
                        binding.ivInmuebleNueva.setImageURI(uri);
                        binding.ivInmuebleNueva.setAlpha(1.0f);
                        binding.ivInmuebleNueva.setPadding(0, 0, 0, 0);
                    }
                }
        );

        binding.cardImagenInmueble.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        binding.btnGuardarInmueble.setOnClickListener(v -> {
            if(imageUri == null) {
                vm.validarImage(null, null);
                return;
            }

            File imageFile;
            try {
                imageFile = FileUtil.from(requireContext(), imageUri);
            } catch (Exception e) {
                vm.validarImage(imageUri, e);
                return;
            }

            vm.crearInmueble(
                    imageFile,
                    binding.etDireccionCreate.getText().toString(),
                    binding.etUsoCreate.getText().toString(),
                    binding.etTipoCreate.getText().toString(),
                    binding.etAmbientesCreate.getText().toString(),
                    binding.etSuperficieCreate.getText().toString(),
                    binding.etPrecioCreate.getText().toString(),
                    binding.swDisponibleCreate.isChecked()
            );
        });
    }

    private void setupObservers() {
        vm.getCreateInmuebleState().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case LOADING:
                    setLoadingState(true);
                    break;
                case SUCCESS:
                    setLoadingState(false);
                    vm.resetCreateState(); // Resetear para evitar re-navegación al volver
                    Navigation.findNavController(requireView()).navigateUp();
                    break;
                case ERROR:
                case WARNING:
                case IDLE:
                    setLoadingState(false);
                    break;
            }
        });
    }

    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            binding.btnGuardarInmueble.setEnabled(false);
            binding.btnGuardarInmueble.setText("Cargando...");
            binding.loadingIndicator.setVisibility(View.VISIBLE);
        } else {
            binding.btnGuardarInmueble.setEnabled(true);
            binding.btnGuardarInmueble.setText("Guardar Inmueble");
            binding.loadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
