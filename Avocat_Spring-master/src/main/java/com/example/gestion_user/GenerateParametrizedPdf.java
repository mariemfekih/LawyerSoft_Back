package com.example.gestion_user;

import net.sf.jasperreports.engine.*;

import java.util.HashMap;
import java.util.Map;

public class GenerateParametrizedPdf {

    public static void main(String[] args) {
        try{
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\templates\\test.jrxml");
            JRDataSource datasource = new JREmptyDataSource();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("firstPartyName", "مريم");
            parameters.put("firstPartyID", "12345698");
            parameters.put("secondPartyName", "sarra");
            parameters.put("secondPartyID", "11223366");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);

            // Export the report to PDF
            String outputFile = "C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\output\\test2.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);
        }catch(Exception ex){ex.printStackTrace();}
    }
}
