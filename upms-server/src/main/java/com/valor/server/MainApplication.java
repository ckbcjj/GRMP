package com.valor.server;

import com.comnon.geolocate.GeoIPService;
import com.mfc.config.ConfigTools3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.valor.server"})
public class MainApplication {

	public static void main(String[] args) {
		ConfigTools3.load("config");
		GeoIPService.loadGeoIpData();
		SpringApplication.run(MainApplication.class, args);

		System.out.println("****************UPMS SERVER START*************************");
	}

}
