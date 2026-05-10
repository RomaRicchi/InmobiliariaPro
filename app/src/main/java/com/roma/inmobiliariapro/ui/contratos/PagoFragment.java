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

import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.databinding.FragmentPagoBinding;
import com.roma.inmobiliariapro.ui.adapters.InmuebleAdapter;
import com.roma.inmobiliariapro.ui.adapters.PagoAdapter;
import com.roma.inmobiliariapro.ui.viewsModels.ContratoInquilinoViewModel;

import java.util.ArrayList;

public class PagoFragment extends Fragment {
    private FragmentPagoBinding binding;
    private ContratoInquilinoViewModel vm;
    private PagoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPagoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(ContratoInquilinoViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recyclerPagos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PagoAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        vm.getPagosMutable().observe(getViewLifecycleOwner(), pagos -> {
            if(pagos != null) {
                adapter = new PagoAdapter(pagos, getContext());
                recyclerView.setAdapter(adapter);
            }
        });

        vm.forceRefreshPagos();
    }
}