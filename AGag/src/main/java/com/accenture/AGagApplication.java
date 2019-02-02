package com.accenture;

import com.accenture.utils.Util;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class AGagApplication {

	public static void main(String[] args) {
		//TODO check if correct practice
		if (args.length > 0){
			Util.PATH_TO_UPLOAD_DIR = args[0];
		}
		SpringApplication.run(AGagApplication.class, args);
	}

}

