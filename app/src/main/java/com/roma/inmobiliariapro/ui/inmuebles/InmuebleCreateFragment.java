package com.roma.inmobiliariapro.ui.inmuebles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roma.inmobiliariapro.databinding.FragmentInmuebleCreateBinding;
import com.roma.inmobiliariapro.ui.viewsModels.InmuebleViewModel;

public class InmuebleCreateFragment extends Fragment {
    private FragmentInmuebleCreateBinding binding;
    private InmuebleViewModel vm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleCreateBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(InmuebleViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //observer y listener....
    }
}