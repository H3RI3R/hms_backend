package com.example.main;

import com.example.main.ManageHotel.Service.AmenitiesService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

@OpenAPIDefinition(info=@Info(title = "Hotel Management Documentation", version = "1.0",description = "This is Hotel Management Whole Project Documentation"))
@SpringBootApplication
@EnableCaching
public class HmSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmSystemApplication.class, args);
		System.out.println("Hotel Management System");

	}

}
