package vn.appCosmetic.ServiceAPI.Image;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface APIImageService {
    @DELETE("{id}")
    Call<Void> deleteImage(@Path("id") int id);
}
