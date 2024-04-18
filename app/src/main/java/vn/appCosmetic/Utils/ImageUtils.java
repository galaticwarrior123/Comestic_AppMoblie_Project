package vn.appCosmetic.Utils;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImageUtils {
    public static void saveUriToFirebaseStorage(Context context, String uri, String path) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(path);
        Uri file = Uri.parse(uri);
        imageRef.putFile(file);


    }

}
