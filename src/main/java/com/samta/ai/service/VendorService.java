package com.samta.ai.service;

import com.samta.ai.entity.Store;
import com.samta.ai.entity.Vendor;
import com.samta.ai.handler.CustomException;
import com.samta.ai.handler.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface VendorService {
    ResponseEntity<?> addVendor(Vendor vendor) throws CustomException;
    ResponseEntity<?> updateVendor(int id,Vendor vendor) throws CustomException;
    ResponseEntity<?> deleteVendor(int id) throws CustomException, CustomException;
    ResponseEntity<?> getVendor(int id) throws CustomException;
    ResponseEntity<?> getVendors();
    ResponseEntity<?> addStore(Store store);
    ResponseEntity<?> addProducts(Integer storeId, MultipartFile multipartFile) throws CustomException;
    ResponseEntity<?> getAllStores();
    ResponseEntity<?> getProductByStore(int storeId,int productId) throws CustomException;
    ResponseEntity<?> getProductsByStore(int storeId) throws CustomException;
}
