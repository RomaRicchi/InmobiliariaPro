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

import java.util.ArrayList;
import java.util.List;

public class InmuebleFragment extends Fragment {

    private FragmentInmuebleBinding binding;
    private InmuebleAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InmuebleViewModel inmuebleViewModel =
                new ViewModelProvider(this).get(InmuebleViewModel.class);

        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerviewTransform;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new InmuebleAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        inmuebleViewModel.getInmuebles().observe(getViewLifecycleOwner(), (List<Inmueble> inmuebles) -> {
            if (inmuebles != null) {
                adapter = new InmuebleAdapter(inmuebles, getContext());
                recyclerView.setAdapter(adapter);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
