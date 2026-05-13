package com.roma.inmobiliariapro.ui.perfil;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.databinding.FragmentCambiarClaveBinding;
import com.roma.inmobiliariapro.ui.viewsModels.PropietarioViewModel;

public class CambiarClaveFragment extends DialogFragment {
    private FragmentCambiarClaveBinding binding;
    private PropietarioViewModel vm;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCambiarClaveBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(requireActivity()).get(PropietarioViewModel.class);

        vm.getChangePasswordState().observe(getViewLifecycleOwner(), status -> {
            switch (status) {
                case WARNING:
                    // cambiar colores de los inputs
                    break;
                case LOADING:
                    binding.btnEditarPasswordDialog.setEnabled(false);
                    //poner el circulito de cargando
                    break;
                case SUCCESS:
                    //quitar el circulito de cargando
                    binding.btnEditarPasswordDialog.setEnabled(true);
                    dismiss();
                    //cerrar DialogFragment
                    break;
                case ERROR:
                    //quitar el circulito de cargando
                    binding.btnEditarPasswordDialog.setEnabled(true);
                    break;
            }
        });

        binding.btnEditarPasswordDialog.setOnClickListener(v -> {
            String passActual = binding.etCurrentPassword.getText().toString();
            String passNueva = binding.etNewPassword.getText().toString();

            vm.cambiarContrasena(passActual, passNueva);
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {

            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}