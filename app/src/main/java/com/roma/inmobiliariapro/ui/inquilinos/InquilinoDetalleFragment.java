package com.roma.inmobiliariapro.ui.inquilinos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.databinding.FragmentInquilinoDetalleBinding;
import com.roma.inmobiliariapro.ui.viewsModels.ContratoInquilinoViewModel;

public class InquilinoDetalleFragment extends Fragment {
    private FragmentInquilinoDetalleBinding binding;
    private ContratoInquilinoViewModel vm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInquilinoDetalleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(ContratoInquilinoViewModel.class);

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                vm.getContrato(inmueble, false);
            }
        }

        return inflater.inflate(R.layout.fragment_inquilino_detalle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm.getContratoMutable().observe(getViewLifecycleOwner(), contrato -> {
            // EN CONTRATO ESTA EL INQUILINO
            // SETTEAR INPUTS CON LOS DATOS DEL INQUILINO
            // EJEMPLO: binding.etNombreInquilino.setText(contrato.getInquilino().getNombre()
        });
    }
}