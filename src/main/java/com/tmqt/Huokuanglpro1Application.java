package com.tmqt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tmqt.dao")
public class Huokuanglpro1Application {

	public static void main(String[] args) {
		SpringApplication.run(Huokuanglpro1Application.class, args);
	}
}
