package vn.appCosmetic.ServiceAPI.Bank;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.appCosmetic.Model.Bank;
import vn.appCosmetic.Model.ResponseBank;

public interface APIBankService {
    @GET(" ")
    Call<ResponseBank> getAllBank();
}
