package fxtransaction;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import banksystem.service.ServiceContainer;


@SpringBootApplication
@ComponentScan(basePackages = {"fxtransaction", "banksystem.service", "banksystem.repository"})
@EnableJpaRepositories(basePackages = "banksystem.repository")
@EntityScan(basePackages = "banksystem.entity") 
public class Main {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        ServiceContainer container = context.getBean(ServiceContainer.class);
		Scanner s = new Scanner(System.in);
		new Service(s, container);
	}
}
