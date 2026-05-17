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
            if (status == null) return;
            
            switch (status.getStatus()) {
                case WARNING:
                    binding.btnEditarPasswordDialog.setEnabled(true);
                    if("newPassword".equals(status.getFieldName())){
                        binding.etNewPassword.setError("Campo obligatorio");
                        binding.etNewPassword.requestFocus();
                    }
                    if("currentPassword".equals(status.getFieldName())){
                        binding.etCurrentPassword.setError("Campo obligatorio");
                        binding.etCurrentPassword.requestFocus();
                    }
                    break;
                case LOADING:
                    binding.btnEditarPasswordDialog.setEnabled(false);
                    //poner el circulito de cargando
                    break;
                case SUCCESS:
                    //quitar el circulito de cargando
                    binding.btnEditarPasswordDialog.setEnabled(true);
                    vm.resetChangePasswordState(); // Reseteamos el estado para que se pueda volver a abrir
                    dismiss();
                    //cerrar DialogFragment
                    break;
                case ERROR:
                    //quitar el circulito de cargando
                    binding.btnEditarPasswordDialog.setEnabled(true);
                    vm.resetChangePasswordState(); // También reseteamos en error si queremos permitir reintentar sin trabas
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
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Aseguramos que el estado se resetee al cerrar el diálogo por cualquier medio (ej. click afuera)
        vm.resetChangePasswordState();
        binding = null;
    }
}
