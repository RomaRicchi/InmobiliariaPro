package com.roma.inmobiliariapro.ui.inmuebles;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roma.inmobiliariapro.data.model.response.Inmueble;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleCreateBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(InmuebleViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if(uri != null) {
                        imageUri = uri;
                        binding.ivInmuebleNueva.setImageURI(uri);
                    }
                }
        );

        binding.ivInmuebleNueva.setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });

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

        vm.getCreateInmuebleState().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case WARNING:
                    //cambiar de color los inputs
                    break;
                case LOADING:
                    //agregar circulo de cargando
                    binding.btnGuardarInmueble.setEnabled(false);
                    break;
                case SUCCESS:
                case ERROR:
                    //quitar circulo de cargando
                    binding.btnGuardarInmueble.setEnabled(true);
                    break;
            }
        });
    }
}