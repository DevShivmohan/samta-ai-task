package com.samta.ai;

import com.samta.ai.constants.ApiConstants;
import com.samta.ai.entity.Vendor;
import com.samta.ai.handler.CustomException;
import com.samta.ai.repository.VendorRepository;
import com.samta.ai.service.VendorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Date;

@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class SamtaAiApplication {

	private VendorRepository vendorRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(SamtaAiApplication.class, args);
	}

	/**
	 * insert one admin at the beginning of the execution
	 * @throws CustomException
	 */
	@PostConstruct
	public void insertOneAdmin() throws CustomException {
		try {
			var admin= vendorRepository.save(new Vendor(0,"Shiv@gmail.com",bCryptPasswordEncoder.encode("shiv"), ApiConstants.ADMIN,new Date()));
			log.info(admin.toString());
		}catch (Exception e){

		}
	}

}
