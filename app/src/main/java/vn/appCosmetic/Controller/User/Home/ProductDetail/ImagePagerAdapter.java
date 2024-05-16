package vn.appCosmetic.Controller.User.Home.ProductDetail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ImagePagerAdapter extends FragmentPagerAdapter {
    private List<String> imageUrls;

    public ImagePagerAdapter(FragmentManager fm, List<String> imageUrls) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new ImageFragment(imageUrls.get(position));
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }
}