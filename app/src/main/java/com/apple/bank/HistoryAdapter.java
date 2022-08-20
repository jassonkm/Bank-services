package com.apple.bank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apple.bank.databinding.ItemListBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory> {
    private List<DataTransf>historyList;

    public HistoryAdapter(List<DataTransf>historyList){
        this.historyList=historyList;
    }
    @NonNull
    @Override
    public ViewHolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        ItemListBinding itemlist=ItemListBinding.inflate(layoutInflater,parent,false);
        RecyclerView.ViewHolder data=null;
        data=(RecyclerView.ViewHolder)(new HistoryAdapter.ViewHolderHistory(itemlist));
        return (ViewHolderHistory) data;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHistory holder, int position) {
        DataTransf transf=historyList.get(position);
        holder.binding.textType.setText(transf.getType());
        holder.binding.textDes.setText(transf.getDescription());
        holder.binding.textAmount.setText(transf.getAmount());
        holder.binding.textDate.setText(transf.getDate());
        holder.binding.textEstate.setText(transf.getEstate().toString());

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolderHistory extends RecyclerView.ViewHolder {

        private final ItemListBinding binding;

        public ViewHolderHistory(@NonNull ItemListBinding binding) {
            super((View)binding.getRoot());
            this.binding=binding;
        }
    }
}
