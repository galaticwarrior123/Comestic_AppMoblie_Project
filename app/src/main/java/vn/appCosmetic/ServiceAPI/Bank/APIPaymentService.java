package vn.appCosmetic.ServiceAPI.Bank;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.appCosmetic.Model.Payment;
import vn.appCosmetic.Model.ResponsePayment;

public interface APIPaymentService {

    @POST("payment/createPayment")
    Call<ResponsePayment> createPayment(@Body Payment payment);
}
