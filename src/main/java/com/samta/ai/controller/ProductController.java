package com.samta.ai.controller;

import com.samta.ai.handler.CustomException;
import com.samta.ai.repository.StoreRepository;
import com.samta.ai.service.VendorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private VendorService vendorService;
    @PostMapping(value = "/bulk/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBulkUpload(@PathVariable("id") int id, @RequestParam(value = "file", required = true) final MultipartFile file) throws CustomException {
        return vendorService.addProducts(id,file);
    }
}
