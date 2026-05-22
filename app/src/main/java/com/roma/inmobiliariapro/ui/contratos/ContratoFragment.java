package com.roma.inmobiliariapro.ui.contratos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roma.inmobiliariapro.databinding.FragmentContratoBinding;
import com.roma.inmobiliariapro.ui.adapters.InmuebleAdapter;
import com.roma.inmobiliariapro.ui.inmuebles.InmuebleViewModel;

import java.util.ArrayList;


public class ContratoFragment extends Fragment {
    private FragmentContratoBinding binding;
    private ContratoViewModel contratoVM;
    private InmuebleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contratoVM = new ViewModelProvider(this).get(ContratoViewModel.class);
        binding = FragmentContratoBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recyclerContratos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new InmuebleAdapter(new ArrayList<>(), getContext(),"DETALLES CONTRATO");
        recyclerView.setAdapter(adapter);

        contratoVM.getInmueblesAlquiladosMutable().observe(getViewLifecycleOwner(), inmuebles -> {
            if(inmuebles != null) {
                adapter = new InmuebleAdapter(inmuebles, getContext(), "DETALLES CONTRATO");
                recyclerView.setAdapter(adapter);
            }
        });

        contratoVM.getInmublesAlquilados();
    }
}