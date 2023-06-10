package com.samta.ai.controller;

import com.samta.ai.entity.Customer;
import com.samta.ai.handler.CustomException;
import com.samta.ai.service.CustomerService;
import com.samta.ai.service.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;
    private VendorService vendorService;
    @PostMapping
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer){
        return customerService.addCustomer(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable int id,@Valid @RequestBody Customer customer) throws CustomException {
        return customerService.updateCustomer(id,customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int id) throws CustomException {
        return customerService.deleteCustomer(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable int id) throws CustomException {
        return customerService.getCustomer(id);
    }

    @GetMapping("/stores-products")
    public ResponseEntity<?> getStoresProducts() throws CustomException {
        return vendorService.getAllStores();
    }

    @GetMapping("/store-products/{storeId}")
    public ResponseEntity<?> getStoreProducts(@PathVariable("storeId") int storeId) throws CustomException {
        return vendorService.getProductsByStore(storeId);
    }

    @GetMapping("/store-product/{storeId}/{productId}")
    public ResponseEntity<?> getStoreProducts(@PathVariable("storeId") int storeId,@PathVariable("productId") int productId) throws CustomException {
        return vendorService.getProductByStore(storeId,productId);
    }

}
