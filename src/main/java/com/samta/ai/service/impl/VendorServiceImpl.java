package com.samta.ai.service.impl;

import com.samta.ai.constants.ApiConstants;
import com.samta.ai.entity.Product;
import com.samta.ai.entity.Store;
import com.samta.ai.entity.Vendor;
import com.samta.ai.handler.CustomException;
import com.samta.ai.repository.StoreRepository;
import com.samta.ai.repository.VendorRepository;
import com.samta.ai.service.VendorService;
import com.samta.ai.util.ExcelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class VendorServiceImpl implements VendorService {
    private VendorRepository vendorRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private StoreRepository storeRepository;
    @Override
    public ResponseEntity<?> addVendor(Vendor vendor) throws CustomException {
        vendor.setId(0);
        if(!(vendor.getRole().equalsIgnoreCase(ApiConstants.SUPERVISOR) || vendor.getRole().equalsIgnoreCase(ApiConstants.SALES_PERSON)))
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid role for vendors, you can use i.e. "+ApiConstants.SUPERVISOR+" and "+ApiConstants.SALES_PERSON);
        vendor.setCreatedAt(new Date());
        vendor.setPassword(bCryptPasswordEncoder.encode(vendor.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorRepository.save(vendor));
    }

    @Override
    public ResponseEntity<?> updateVendor(int id, Vendor vendor) throws CustomException {
        var vendorFromDB=vendorRepository.findById(id).
                orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Vendor not found with given id "+id));
        vendor.setId(id);
        vendor.setPassword(bCryptPasswordEncoder.encode(vendor.getPassword()));
        vendor.setCreatedAt(vendorFromDB.getCreatedAt());
        return ResponseEntity.status(HttpStatus.OK).body(vendorRepository.save(vendor));
    }

    @Override
    public ResponseEntity<?> deleteVendor(int id) throws CustomException {
        var vendorFromDB=vendorRepository.findById(id).
                orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Vendor not found with given id "+id));
        vendorRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(vendorFromDB);
    }

    @Override
    public ResponseEntity<?> getVendor(int id) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK).body(vendorRepository.findById(id).
                orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Vendor not found with given id "+id)));
    }

    @Override
    public ResponseEntity<?> getVendors() {
        return ResponseEntity.status(HttpStatus.OK).body(vendorRepository.findAll());
    }

    /**
     * add store
     * @param store
     * @return
     */
    @Override
    public ResponseEntity<?> addStore(Store store) {
        store.setId(0);
        store.setProducts(new ArrayList<>());
        return ResponseEntity.status(HttpStatus.CREATED).body(storeRepository.save(store));
    }

    /**
     * Add products from .xlsx file into the store
     * @param storeId
     * @param file
     * @return
     * @throws CustomException
     */
    @Override
    public ResponseEntity<?> addProducts(Integer storeId, MultipartFile file) throws CustomException {
        var storeFromDB = storeRepository.findById(storeId)
                        .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Store not found with given id "+storeId));
        storeFromDB.setProducts(new ArrayList<>());
        final var workBook = ExcelUtil.getWorkBookFromFile(file);
        final var workSheet = workBook.getSheetAt(0);
        final var numberOfRowsInWorkSheet = workSheet.getLastRowNum();
        final var products = new ArrayList<Product>();
        for (int rowIndex = 0; rowIndex < numberOfRowsInWorkSheet; rowIndex++) {
            final var currentRow = workSheet.getRow(rowIndex + 1);
            if (ExcelUtil.isRowEmpty(currentRow))
                continue;
            final var product=new Product();
            try {
                product.setName(String.valueOf(currentRow.getCell(0)));
                product.setType(String.valueOf(currentRow.getCell(1)));
                product.setManufacturer(String.valueOf(currentRow.getCell(2)));
                product.setPrice(Double.valueOf(currentRow.getCell(3).getRawValue()));
                product.setUnits(Double.valueOf(currentRow.getCell(4).getRawValue()));
            }catch (Exception e){
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Invalid value in excel sheet . error description "+e.getMessage());
            }
            products.add(product);
        }
        ExcelUtil.closeWorkbook(workBook);
        storeFromDB.setProducts(products);
        return ResponseEntity.status(HttpStatus.OK).body(storeRepository.save(storeFromDB));
    }

    /**
     * getting all stores and its products
     * @return
     */
    @Override
    public ResponseEntity<?> getAllStores() {
        return ResponseEntity.status(HttpStatus.OK).body(storeRepository.findAll());
    }

    /**
     * getting single product from a store
     * @param storeId
     * @param productId
     * @return
     * @throws CustomException
     */
    @Override
    public ResponseEntity<?> getProductByStore(int storeId, int productId) throws CustomException {
        var store= storeRepository.findById(storeId)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Store does not exists"));
        return ResponseEntity.status(HttpStatus.OK).body(store.getProducts().stream().filter(product -> product.getId()==productId)
                .collect(Collectors.toList()));
    }

    /**
     * getting all products from single store
     * @param storeId
     * @return
     * @throws CustomException
     */
    @Override
    public ResponseEntity<?> getProductsByStore(int storeId) throws CustomException {
        return ResponseEntity.status(HttpStatus.OK).body(storeRepository.findById(storeId)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND.value(), "Store does not exists")));
    }
}
