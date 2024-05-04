package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.constants.reportConstant;
import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.report.Report;
import com.example.gestion_user.entities.report.StringResult;
import com.example.gestion_user.services.JasperReportService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@AllArgsConstructor
@Service
public class JasperReportServiceImpl implements JasperReportService {
    @Autowired
    DataSource dataSource;
    @Autowired
    ResourceLoader resourceLoader;
    public String generatePdf(Map<String, Object> params) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\templates\\test.jrxml");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            String outputFile = reportConstant.REPORT_RESULT_FOLDER+"test.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);

            return "PDF généré avec succès";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Erreur lors de la génération du PDF : " + ex.getMessage();
        }
    }

    @Override
    public StringResult createReport(Report report) throws SQLException {
        try{
            Resource resource=resourceLoader.getResource("classpath:templates/"+report.getName()+".jasper");
            InputStream reportStream = resource.getInputStream();
            Map parameters = new HashMap<>();
            StringResult reportName = new StringResult();
            parameters.put("REPORT_FOLDER", resourceLoader.getResource("classpath:templates/" + report.getName() + ".jasper").getFile().getAbsolutePath());
            Connection conn=this.dataSource.getConnection();
            byte[] reportBytes = JasperRunManager.runReportToPdf(reportStream, parameters, conn);
            reportName.setName(report.getName()+".pdf");
            FileOutputStream fileOutputStream=new FileOutputStream(reportConstant.REPORT_RESULT_FOLDER+reportName.getName());
            fileOutputStream.write(reportBytes);
            fileOutputStream.close();
            return reportName;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }
        return null;
    }
  /*  @Override
    public StringResult generatePdfReport(Map<String, Object> params) throws SQLException, IOException, JRException {
        String reportName = (String) params.get("reportName");
        Resource resource = resourceLoader.getResource("classpath:templates/" + reportName + ".jasper");
        try (InputStream reportStream = resource.getInputStream();
             Connection conn = this.dataSource.getConnection();
             FileOutputStream fileOutputStream = new FileOutputStream(reportConstant.REPORT_RESULT_FOLDER + reportName + ".pdf")) {
            params.put("REPORT_FOLDER", resourceLoader.getResource("classpath:templates/" + reportName + ".jasper").getFile().getAbsolutePath());
            byte[] reportBytes = JasperRunManager.runReportToPdf(reportStream, params, conn);
            fileOutputStream.write(reportBytes);
            StringResult reportResult = new StringResult();
            reportResult.setName(reportName + ".pdf");
            return reportResult;
        }
    }*/

    public void generateReport(List<Case> cases) throws JRException {

        JasperReport jasperReport = JasperCompileManager.compileReport("C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\templates\\casesReport.jrxml");

        // Convert Java objects to JRDataSource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cases);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

        // Export the report to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\marie\\Documents\\Github\\LawyerSoft_Back\\Avocat_Spring-master\\src\\main\\resources\\output\\casesReport.pdf");
    }

}
