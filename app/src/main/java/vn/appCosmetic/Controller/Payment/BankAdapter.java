package vn.appCosmetic.Controller.Payment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.appCosmetic.Model.Bank;
import vn.appCosmetic.R;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder>{
    List<Bank> bankList;
    Context context;
    LayoutInflater inflater;

    public BankAdapter(List<Bank> bankList, Context context) {
        this.bankList = bankList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item_bank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bank bank = bankList.get(position);
        Glide.with(context).load(Uri.parse(bank.getLogo())).into(holder.imgBank);
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBank;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBank = itemView.findViewById(R.id.imageViewBank);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = bankList.get(getAdapterPosition()).getId();
                    if(id==24){
                        Intent intent = new Intent(context, PaymentActivity.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
