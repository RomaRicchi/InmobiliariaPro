package com.roma.inmobiliariapro.ui.contratos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.data.model.response.Pago;
import com.roma.inmobiliariapro.databinding.FragmentContratoDetalleBinding;
import com.roma.inmobiliariapro.ui.viewsModels.ContratoInquilinoViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContratoDetalleFragment extends Fragment {
    private FragmentContratoDetalleBinding binding;
    private ContratoInquilinoViewModel vm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContratoDetalleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(ContratoInquilinoViewModel.class);

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                vm.getContrato(inmueble, true);
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm.getContratoMutable().observe(getViewLifecycleOwner(), contrato -> {
            if(contrato != null) {
                binding.etContratoDireccion.setText(contrato.getInmueble().getDireccion());
                binding.etContratoInquilino.setText(contrato.getInquilino().getNombreFull());
                binding.etContratoMonto.setText(String.format(Locale.getDefault(), "$ %.2f", contrato.getMontoAlquiler()));
                binding.etContratoFechaInicio.setText(contrato.getFechaInicio());
                binding.etContratoFechaFin.setText(contrato.getFechaFin());
            }
        });

        vm.getPagosMutable().observe(getViewLifecycleOwner(), pagos -> {
            if(pagos != null && !pagos.isEmpty()){
                binding.btnPagos.setEnabled(true);
            }
        });

        binding.btnPagos.setOnClickListener(v -> {

            Navigation.findNavController(v).navigate(R.id.action_contratoDetalleFragment_to_pagoFragment);
        });
    }
}