package com.example.gestion_user;

import com.example.gestion_user.services.JasperReportService;
import com.example.gestion_user.services.servicesImpl.JasperReportServiceImpl;
import net.sf.jasperreports.engine.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.gestion_user.constants.FileConstant.USER_FOLDER;

@SpringBootApplication
@SpringBootConfiguration
public class GestionUserApplication {

    public static void main(String[] args) {

         SpringApplication.run(GestionUserApplication.class, args);
      /*  try {
            // Compile the Jasper report
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\templates\\contratLocation.jrxml");

            // Create a data source (you can use JREmptyDataSource if no data source is required)
            JRDataSource datasource = new JREmptyDataSource();

            // Set parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("firstPartyName", "Your Actual First Party Name"); // Replace with actual name
            parameters.put("firstPartyID", "12345678"); // Replace with actual ID
            parameters.put("firstPartyDate", "2024-05-01"); // Replace with actual date (YYYY-MM-DD format)
            parameters.put("secondPartyName", "Your Actual Second Party Name"); // Replace with actual name
            parameters.put("secondPartyID", "11223344"); // Replace with actual ID
            parameters.put("secondPartyDate", "2024-05-01"); // Replace with actual date (YYYY-MM-DD format)
            parameters.put("term1", "This is the first term content."); // Replace with actual term content
            parameters.put("term2", "This is the second term content."); // Replace with actual term content
            parameters.put("term3", "This is the third term content.");
            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
            // Specify the output directory
            String outputDirectory = "C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\output\\";
            // Create the output directory if it doesn't exist
            File directory = new File(outputDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Export the report to PDF
            String outputFile = outputDirectory + "contratLocation.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);
            System.out.println("First Party Name: " + parameters.get("firstPartyName"));

            System.out.println("Report exported successfully to: " + outputFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
       // new File(USER_FOLDER).mkdirs();
    }

   @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
 @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

   /* @Bean
    public JasperReportService jasperReportService() {
        return new JasperReportServiceImpl();
    }*/



}
