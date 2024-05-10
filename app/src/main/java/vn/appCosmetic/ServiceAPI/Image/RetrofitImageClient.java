package vn.appCosmetic.ServiceAPI.Image;

import retrofit2.Retrofit;

public class RetrofitImageClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/api/image/")
                    .build();
        }
        return retrofit;
    }
}
