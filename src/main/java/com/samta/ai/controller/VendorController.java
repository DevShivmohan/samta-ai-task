package com.samta.ai.controller;

import com.samta.ai.entity.Store;
import com.samta.ai.entity.Vendor;
import com.samta.ai.handler.CustomException;
import com.samta.ai.service.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendor")
@AllArgsConstructor
public class VendorController {
    private VendorService vendorService;
    @PostMapping
    public ResponseEntity<?> addVendor(@RequestBody Vendor vendor) throws CustomException {
        return vendorService.addVendor(vendor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVendor(@PathVariable("id") int id, @RequestBody Vendor vendor) throws CustomException {
        return vendorService.updateVendor(id,vendor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVendor(@PathVariable("id") int id) throws CustomException {
        return vendorService.deleteVendor(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVendor(@PathVariable("id") int id) throws CustomException {
        return vendorService.getVendor(id);
    }

    @GetMapping
    public ResponseEntity<?> getVendors() {
        return vendorService.getVendors();
    }

    @PostMapping("/store")
    public ResponseEntity<?> addStore(@RequestBody Store store){
        return vendorService.addStore(store);
    }

}
