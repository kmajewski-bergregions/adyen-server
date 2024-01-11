package com.example.adyenserver;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/adyen")
public class AdyenController {

    @GetMapping
    public String generateSdkData(
            @RequestParam String token
    ) {
        try {
            OkHttpClient client = new OkHttpClient();

            String json = """
                    {
                        "merchantAccount": "BergRegions110POS",
                        "setupToken": "%s"
                    }
                    """.formatted(token);

            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url("https://checkout-test.adyen.com/checkout/possdk/v68/sessions")
                    .header("x-API-key", "AQEphmfuXNWTK0Qc+iSSl3Y/leGcQYRDHsIbCGozKW52PXLBt0sCAW5BchQQwV1bDb7kfNy1WIxIIkxgBw==-AtBiEReiO3PmlHWriCbPh9QCgqEmWex3Kjixj7j+2KU=-=@,&>G27>6$jJ:nQ")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();

                System.out.println("Generated token: " + responseBody);

                return Optional.ofNullable(new Gson().fromJson(responseBody, Map.class))
                        .map(x -> x.getOrDefault("sdkData", ""))
                        .map(x -> (String) x)
                        .orElse("Error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
