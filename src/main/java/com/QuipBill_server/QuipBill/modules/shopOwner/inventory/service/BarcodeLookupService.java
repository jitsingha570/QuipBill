package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.BarcodeLookupResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BarcodeLookupService {

    private final ProductRepository productRepository;

    public BarcodeLookupResponse lookup(String barcode) {

        if (barcode == null || barcode.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Barcode is required");
        }

        // Check product in local DB
        Optional<Product> productOpt = productRepository.findByBarcode(barcode);

        if (productOpt.isPresent()) {

            Product product = productOpt.get();

            ProductResponse response = ProductResponse.builder()
                    .productId(product.getProductId())
                    .shopId(product.getShop() != null ? product.getShop().getId() : null)
                    .productName(product.getProductName())
                    .barcode(product.getBarcode())
                    .price(product.getPrice())
                    .gstPercent(product.getGstPercent())
                    .gstEnabled(product.getGstEnabled())
                    .quantity(product.getStockQuantity())
                    .build();

            return BarcodeLookupResponse.builder()
                    .exists(true)
                    .product(response)
                    .build();
        }

        // If not found -> call OpenFoodFacts API
        try {

            String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";

            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(5000);
            requestFactory.setReadTimeout(5000);

            RestTemplate restTemplate = new RestTemplate(requestFactory);
            String apiResponse = restTemplate.getForObject(url, String.class);

            if (apiResponse == null || apiResponse.isBlank()) {
                throw new ApiException(HttpStatus.BAD_GATEWAY, "Barcode lookup service returned empty response");
            }

            JSONObject json = new JSONObject(apiResponse);

            if (json.getInt("status") == 1) {

                JSONObject productData = json.getJSONObject("product");

                String productName = productData.optString("product_name");

                ProductResponse response = ProductResponse.builder()
                        .productName(productName)
                        .barcode(barcode)
                        .quantity(null)
                        .build();

                return BarcodeLookupResponse.builder()
                        .exists(false)
                        .product(response)
                        .build();
            }

        } catch (RestClientException e) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "Failed to reach barcode lookup service");
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "Invalid response from barcode lookup service");
        }

        // Product not found anywhere
        return BarcodeLookupResponse.builder()
                .exists(false)
                .barcode(barcode)
                .build();
    }
}
