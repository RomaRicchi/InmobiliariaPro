package com.roma.inmobiliariapro.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.databinding.FragmentInmuebleDetalleBinding;

import java.util.Locale;

public class InmuebleDetalleFragment extends Fragment {

    private InmuebleDetalleViewModel mViewModel;
    private FragmentInmuebleDetalleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);
        binding = FragmentInmuebleDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recuperar el inmueble del Bundle pasado desde el Adapter
        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                mViewModel.setInmueble(inmueble);
            }
        }

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        mViewModel.getInmueble().observe(getViewLifecycleOwner(), inmueble -> {
            if (inmueble != null) {
                binding.tvDetalleDireccion.setText(inmueble.getDireccion());
                binding.tvDetallePrecio.setText(String.format(Locale.getDefault(), "$ %.2f", inmueble.getPrecio()));
                binding.tvDetalleUso.setText(inmueble.getUso());
                binding.tvDetalleTipo.setText(inmueble.getTipo());
                binding.tvDetalleAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
                binding.tvDetalleSuperficie.setText(String.format(Locale.getDefault(), "%d m²", inmueble.getSuperficie()));
                binding.cbDetalleDisponible.setChecked(inmueble.isEstado());

                // Cargar imagen con Glide
                if (inmueble.getImagenFullUrl() != null) {
                    Glide.with(requireContext())
                            .load(inmueble.getImagenFullUrl())
                            .placeholder(R.drawable.inmobiliaria_foto)
                            .error(R.drawable.inmobiliaria_foto)
                            .into(binding.ivDetalleImagen);
                } else {
                    binding.ivDetalleImagen.setImageResource(R.drawable.inmobiliaria_foto);
                }
            }
        });
    }

    private void setupListeners() {
        // El botón "Volver a la lista" del diseño
        binding.btnVolver.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
