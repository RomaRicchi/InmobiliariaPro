package com.roma.inmobiliariapro.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.data.model.response.Inquilino;
import com.roma.inmobiliariapro.databinding.FragmentInquilinoDetalleBinding;

public class InquilinoDetalleFragment extends Fragment {
    private FragmentInquilinoDetalleBinding binding;
    private InquilinoDetalleViewModel inquilinoDetalleVM;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInquilinoDetalleBinding.inflate(inflater, container, false);
        inquilinoDetalleVM = new ViewModelProvider(requireActivity()).get(InquilinoDetalleViewModel.class);

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                inquilinoDetalleVM.getContrato(inmueble);
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inquilinoDetalleVM.getContratoMutable().observe(getViewLifecycleOwner(), contrato -> {
            if (contrato != null && contrato.getInquilino() != null) {
                Inquilino inquilino = contrato.getInquilino();
                binding.etNombreInquilino.setText(inquilino.getNombre());
                binding.etApellidoInquilino.setText(inquilino.getApellido());
                binding.etDniInquilino.setText(inquilino.getDni());
                binding.etTelefonoInquilino.setText(inquilino.getTelefono());
                binding.etEmailInquilino.setText(inquilino.getEmail());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
