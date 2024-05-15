package vn.appCosmetic.Controller.User.Home.ProductDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import vn.appCosmetic.R;

public class ImageFragment extends Fragment {
    private String imageUrl;

    public ImageFragment(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(getContext()).load(imageUrl).into(imageView);
        return view;
    }
}