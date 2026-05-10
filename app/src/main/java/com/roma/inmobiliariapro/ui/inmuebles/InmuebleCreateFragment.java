package com.roma.inmobiliariapro.ui.inmuebles;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

            String direccion = binding.etDireccionCreate.getText().toString();
            String uso = binding.etUsoCreate.getText().toString();
            String tipo = binding.etTipoCreate.getText().toString();
            String ambientes = binding.etAmbientesCreate.getText().toString();
            String superficie = binding.etSuperficieCreate.getText().toString();
            String precio = binding.etPrecioCreate.getText().toString();
            boolean disponible = binding.swDisponibleCreate.isChecked();
            File imageFile;
            if(!vm.validarCampos(direccion, uso, tipo, ambientes, superficie, precio) || imageUri == null) {
                return ;
            }
            try {
                imageFile = FileUtil.from(requireContext(), imageUri);
            } catch (Exception e) {
                return;
            }

            Inmueble inmueble = new Inmueble();

            inmueble.setDireccion(direccion);
            inmueble.setUso(uso);
            inmueble.setTipo(tipo);
            inmueble.setAmbientes(Integer.parseInt(ambientes));
            inmueble.setSuperficie(Integer.parseInt(superficie));
            inmueble.setPrecio(Double.parseDouble(precio));
            inmueble.setEstado(disponible);

            vm.crearInmueble(imageFile, inmueble);
        });

    }
}