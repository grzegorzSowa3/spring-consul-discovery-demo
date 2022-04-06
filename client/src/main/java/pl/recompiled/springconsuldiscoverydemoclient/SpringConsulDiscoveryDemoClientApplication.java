package pl.recompiled.springconsuldiscoverydemoclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringConsulDiscoveryDemoClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringConsulDiscoveryDemoClientApplication.class, args);
	}

}
