package com.roma.inmobiliariapro.ui.inmuebles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.roma.inmobiliariapro.data.model.Inmueble;
import com.roma.inmobiliariapro.databinding.ItemInmuebleBinding;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolder> {
    private List<Inmueble> inmuebles;
    private Context context;

    public InmuebleAdapter(List<Inmueble> inmuebles, Context context) {
        this.inmuebles = inmuebles;
        this.context = context;
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

        Glide.with(context)
                .load(inmueble.getImagenFullUrl())
                .into(holder.binding.ivImagenInmueble);
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
