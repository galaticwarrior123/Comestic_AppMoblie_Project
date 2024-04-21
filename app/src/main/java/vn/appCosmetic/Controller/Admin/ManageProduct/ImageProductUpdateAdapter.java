package vn.appCosmetic.Controller.Admin.ManageProduct;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.appCosmetic.Model.Product;
import vn.appCosmetic.R;

public class ImageProductUpdateAdapter extends RecyclerView.Adapter<ImageProductUpdateAdapter.ImageProductUpdateViewHolder>{

    List<Uri> imageList;
    Context context;
    LayoutInflater inflater;

    public ImageProductUpdateAdapter(List<Uri> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ImageProductUpdateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.col_item_image_update_product, parent, false);
        return new ImageProductUpdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageProductUpdateViewHolder holder, int position) {
        Uri uri = imageList.get(position);
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.imgProductUpdate);
        holder.btnDeleteImageProductUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageProductUpdateViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProductUpdate;
        Button btnDeleteImageProductUpdate;
        public ImageProductUpdateViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductUpdate = itemView.findViewById(R.id.imgProduct);
            btnDeleteImageProductUpdate = itemView.findViewById(R.id.btndelimage);
        }
    }
}


