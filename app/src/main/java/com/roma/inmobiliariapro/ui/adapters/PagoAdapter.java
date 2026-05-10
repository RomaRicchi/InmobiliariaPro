package com.roma.inmobiliariapro.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roma.inmobiliariapro.data.model.response.Pago;
import com.roma.inmobiliariapro.databinding.ItemPagoBinding;

import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.ViewHolder> {
    private List<Pago> pagos;
    private Context context;

    public PagoAdapter(List<Pago> pagos, Context context) {
        this.pagos = pagos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPagoBinding binding = ItemPagoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pago pago = pagos.get(position);

        holder.binding.tvDetalle.setText(pago.getDetalle());
        holder.binding.tvFechaPago.setText(pago.getFechaPago());
        holder.binding.tvImporte.setText("$ " + pago.getImporte());
        holder.binding.tvEstado.setText(pago.isEstado() ? "Pediente" : "Pagado");
    }

    @Override
    public int getItemCount() {
        return pagos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPagoBinding binding;
        public ViewHolder(ItemPagoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
