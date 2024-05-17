package vn.appCosmetic.Controller.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.appCosmetic.Model.Bank;
import vn.appCosmetic.Model.ResponseBank;
import vn.appCosmetic.R;
import vn.appCosmetic.ServiceAPI.Bank.APIBankService;
import vn.appCosmetic.ServiceAPI.Bank.RetrofitBank;

public class BankActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBank;
    private List<Bank> bankList = new ArrayList<>();
    private BankAdapter bankAdapter;
    private APIBankService apiBankService;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        recyclerViewBank = findViewById(R.id.rcViewBank);
        recyclerViewBank.setLayoutManager(new GridLayoutManager(this, 2));



        apiBankService = RetrofitBank.getRetrofit().create(APIBankService.class);
        apiBankService.getAllBank().enqueue(new Callback<ResponseBank>() {
            @Override
            public void onResponse(Call<ResponseBank> call, Response<ResponseBank> response) {
                if (response.isSuccessful()) {
                    bankList = response.body().getData();
                    bankAdapter = new BankAdapter(bankList, BankActivity.this);
                    recyclerViewBank.setAdapter(bankAdapter);
                } else {
                    Toast.makeText(BankActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBank> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(BankActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BankActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack = findViewById(R.id.imageButtonBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });

    }
}