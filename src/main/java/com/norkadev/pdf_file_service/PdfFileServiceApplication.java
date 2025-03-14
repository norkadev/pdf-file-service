package com.norkadev.pdf_file_service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PdfFileServiceApplication {

	@Bean
	public OpenAPI swaggerHeader() {
		return new OpenAPI()
				.info((new Info())
						.description("Services for the PDF File Service.")

						.title(StringUtils.substringBefore(getClass().getSimpleName(), "$"))
						.version("3.0.0"));
	}

	public static void main(String[] args) {
		SpringApplication.run(PdfFileServiceApplication.class, args);
	}

}
