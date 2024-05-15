package vn.appCosmetic.Controller.User.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.appCosmetic.R;

public class UserFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("UserFragment", "User fragment created");
        return inflater.inflate(R.layout.fragment_user, container, false);
    }
}