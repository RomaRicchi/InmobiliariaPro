package com.roma.inmobiliariapro.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.databinding.FragmentInmuebleBinding;
import com.roma.inmobiliariapro.ui.adapters.InmuebleAdapter;
import com.roma.inmobiliariapro.ui.viewsModels.InmuebleViewModel;

import java.util.ArrayList;
import java.util.List;

public class InmuebleFragment extends Fragment {
    private FragmentInmuebleBinding binding;
    private InmuebleViewModel vm;
    private InmuebleAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(InmuebleViewModel.class);


        RecyclerView recyclerView = binding.recyclerInmuebles;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new InmuebleAdapter(new ArrayList<>(), getContext(),"detalles");
        recyclerView.setAdapter(adapter);

        vm.getInmueblesMutable().observe(getViewLifecycleOwner(), inmuebles -> {
            if(inmuebles != null) {
                adapter = new InmuebleAdapter(inmuebles, getContext(), "detalles");
                recyclerView.setAdapter(adapter);
            }
        });

        vm.getInmuebles();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
