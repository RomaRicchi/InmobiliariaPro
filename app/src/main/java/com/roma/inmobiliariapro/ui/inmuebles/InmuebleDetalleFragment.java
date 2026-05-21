package com.roma.inmobiliariapro.ui.inmuebles;

import android.graphics.Color; // Librería correcta para colores
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.databinding.FragmentInmuebleDetalleBinding;
import com.roma.inmobiliariapro.ui.viewsModels.InmuebleViewModel;

import java.util.Locale;

public class InmuebleDetalleFragment extends Fragment {

    private InmuebleViewModel vm;
    private FragmentInmuebleDetalleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInmuebleDetalleBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InmuebleViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                vm.setInmueble(inmueble);
            }
        }

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        // Unificamos toda la actualización de la UI en un solo lugar
        vm.getInmuebleMutable().observe(getViewLifecycleOwner(), inmueble -> {
            if (inmueble != null) {
                binding.tvDetalleDireccion.setText(inmueble.getDireccion());
                binding.tvDetallePrecio.setText(String.format(Locale.getDefault(), "$ %.2f", inmueble.getPrecio()));
                binding.tvDetalleUso.setText(inmueble.getUso());
                binding.tvDetalleTipo.setText(inmueble.getTipo());
                binding.tvDetalleAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
                binding.tvDetalleSuperficie.setText(String.format(Locale.getDefault(), "%d m²", inmueble.getSuperficie()));
                binding.cbDetalleDisponible.setChecked(inmueble.isEstado());

                // --- APLICACIÓN DEL CAMBIO SOLICITADO ---
                if (inmueble.isEstado()) {
                    binding.btnToggleEstado.setText("Desactivar disponibilidad");
                    binding.btnToggleEstado.setBackgroundColor(Color.parseColor("#D32F2F")); // Rojo
                } else {
                    binding.btnToggleEstado.setText("Activar disponibilidad");
                    binding.btnToggleEstado.setBackgroundColor(Color.parseColor("#388E3C")); // Verde
                }
                // ----------------------------------------

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

        // Observador del estado de la petición (Loading)
        vm.getToggleEstadoState().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                switch (status) {
                    case LOADING:
                        binding.btnToggleEstado.setEnabled(false);
                        break;
                    case SUCCESS:
                    case ERROR:
                        binding.btnToggleEstado.setEnabled(true);
                        break;
                }
            }
        });
    }

    private void setupListeners() {
        // Uso btnToggleEstado que es el ID que manejas para la acción
        binding.btnToggleEstado.setOnClickListener(v -> {
            vm.toggleEstadoInmueble();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
