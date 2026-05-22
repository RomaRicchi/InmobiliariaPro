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
import android.widget.ArrayAdapter;

import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.databinding.FragmentInmuebleCreateBinding;
import com.roma.inmobiliariapro.utils.FileUtil;

import java.io.File;

public class InmuebleCreateFragment extends Fragment {
    private FragmentInmuebleCreateBinding binding;
    private InmuebleCreateViewModel inmuebleCreateVM;
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleCreateBinding.inflate(inflater, container, false);
        inmuebleCreateVM = new ViewModelProvider(requireActivity()).get(InmuebleCreateViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupListeners();
        setupObservers();
        setSpinnerUso();
        setSpinnerTipo();
        setSpinnerAmbientes();
    }

    private void setupListeners() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if(uri != null) {
                        inmuebleCreateVM.setUriImage(uri);
                        binding.ivInmuebleNueva.setImageURI(uri);
                        binding.ivInmuebleNueva.setAlpha(1.0f);
                        binding.ivInmuebleNueva.setPadding(0, 0, 0, 0);
                    }
                }
        );

        binding.cardImagenInmueble.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        binding.btnGuardarInmueble.setOnClickListener(v -> {
            inmuebleCreateVM.crearInmueble(
                    binding.etDireccionCreate.getText().toString(),
                    binding.autoUso.getText().toString(),
                    binding.autoTipo.getText().toString(),
                    binding.autoAmbientes.getText().toString(),
                    binding.etSuperficieCreate.getText().toString(),
                    binding.etPrecioCreate.getText().toString(),
                    binding.swDisponibleCreate.isChecked()
            );
        });
    }

    private void setupObservers() {
        inmuebleCreateVM.getCreateInmuebleState().observe(getViewLifecycleOwner(), status -> {
            switch (status.getStatus()) {
                case LOADING:
                    setLoadingState(true);
                    break;
                case SUCCESS:
                    setLoadingState(false);
                    inmuebleCreateVM.resetCreateState(); // Resetear para evitar re-navegación al volver
                    Navigation.findNavController(requireView()).navigateUp();
                    break;
                case ERROR:
                    break;
                case WARNING:
                    if (status.getFieldName().equals("direccion")){
                        binding.etDireccionCreate.requestFocus();
                        binding.etDireccionCreate.setError("El campo Dirección es obligatorio.");
                    }
                    if (status.getFieldName().equals("uso")){
                        binding.autoUso.requestFocus();
                        binding.autoUso.setError("El campo Uso es obligatorio.");

                    }
                    if (status.getFieldName().equals("ambientes")){
                        binding.autoAmbientes.requestFocus();
                        binding.autoAmbientes.setError("El campo ambientes es obligatorio.");
                    }
                    if (status.getFieldName().equals("superficie")){
                        binding.etSuperficieCreate.requestFocus();
                        binding.etSuperficieCreate.setError("El campo superficie es obligatorio.");
                    }
                    if (status.getFieldName().equals("precio")){
                        binding.etPrecioCreate.requestFocus();
                        binding.etPrecioCreate.setError("El campo precio es obligatorio.");
                    }
                    if (status.getFieldName().equals("tipo")){
                        binding.autoTipo.requestFocus();
                        binding.autoTipo.setError("El campo Tipo es obligatorio.");
                    }
                    if (status.getFieldName().equals("ambientesNumero")){
                        binding.autoAmbientes.requestFocus();
                        binding.autoAmbientes.setError("El campo ambientes deben ser mayor a 0.");
                    }
                    if (status.getFieldName().equals("superficieNumero")){
                        binding.etSuperficieCreate.requestFocus();
                        binding.etSuperficieCreate.setError("El campo superficie deben ser mayor a 0.");
                    }
                    if (status.getFieldName().equals("precioNumero")){
                        binding.etPrecioCreate.requestFocus();
                        binding.etPrecioCreate.setError("El campo precio deben ser mayor a 0.");
                    }
                    break;
                case INFO:
                    break;
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

    private void setSpinnerUso() {
        String[] opciones = {"Comercial", "Residencial"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, // Un layout simple para cada fila
                opciones
        );

        binding.autoUso.setAdapter(adapter);
    }

    private void setSpinnerAmbientes() {
        String[] opciones = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, // Un layout simple para cada fila
                opciones
        );

        binding.autoAmbientes.setAdapter(adapter);
        binding.autoAmbientes.setText(opciones[0], false);
    }

    private void setSpinnerTipo() {
        String[] opciones = {"Local", "Depósito", "Casa", "Departamento"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, // Un layout simple para cada fila
                opciones
        );

        binding.autoTipo.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
