package com.roma.inmobiliariapro.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.roma.inmobiliariapro.R;
import com.roma.inmobiliariapro.data.model.response.Inmueble;
import com.roma.inmobiliariapro.databinding.ItemInmuebleBinding;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolder> {
    private List<Inmueble> inmuebles;
    private Context context;
    private String navigateTo;

    public InmuebleAdapter(List<Inmueble> inmuebles, Context context, String navigateTo) {
        this.inmuebles = inmuebles;
        this.context = context;
        this.navigateTo = navigateTo.toUpperCase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInmuebleBinding binding = ItemInmuebleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = inmuebles.get(position);
        holder.binding.tvDireccion.setText(inmueble.getDireccion());
        holder.binding.tvPrecio.setText("$ " + inmueble.getPrecio());
        holder.binding.tvUsoTipo.setText(inmueble.getUso() + " - " + inmueble.getTipo());

        if (inmueble.getImagenFullUrl() != null) {
            Glide.with(context)
                    .load(inmueble.getImagenFullUrl())
                    .into(holder.binding.ivImagenInmueble);
        } else {
            Glide.with(context)
                    .load(R.drawable.inmobiliaria_foto)
                    .into(holder.binding.ivImagenInmueble);
        }

        // Navegación al detalle
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("inmueble", inmueble);

            if(navigateTo.equals("DETALLES")) {
                Navigation.findNavController(v).navigate(R.id.action_nav_inmuebles_to_inmuebleDetalleFragment, bundle);
            } else if (navigateTo.equals("CONTRATOS")) {
                Navigation.findNavController(v).navigate(R.id.action_nav_inmuebles_to_inmuebleDetalleFragment, bundle);
            } else {
                Navigation.findNavController(v).navigate(R.id.action_nav_inmuebles_to_inmuebleDetalleFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inmuebles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemInmuebleBinding binding;

        public ViewHolder(ItemInmuebleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
