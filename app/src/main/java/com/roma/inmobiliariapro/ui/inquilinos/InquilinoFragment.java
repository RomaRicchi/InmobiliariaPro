package com.roma.inmobiliariapro.ui.inquilinos;

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
import com.roma.inmobiliariapro.databinding.FragmentInquilinoBinding;
import com.roma.inmobiliariapro.ui.adapters.InmuebleAdapter;
import com.roma.inmobiliariapro.ui.viewsModels.InmuebleViewModel;

import java.util.ArrayList;

public class InquilinoFragment extends Fragment {
    private FragmentInquilinoBinding binding;
    private InmuebleViewModel vm;
    private InmuebleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInquilinoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(InmuebleViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recyclerInquilinos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new InmuebleAdapter(new ArrayList<>(), getContext(),"DETALLES INQUILINO");
        recyclerView.setAdapter(adapter);

        vm.getInmueblesAlquiladosMutable().observe(getViewLifecycleOwner(), inmuebles -> {
            if(inmuebles != null) {
                adapter = new InmuebleAdapter(inmuebles, getContext(), "DETALLES INQUILINO");
                recyclerView.setAdapter(adapter);
            }
        });

        vm.getInmublesAlquilados();
    }
}