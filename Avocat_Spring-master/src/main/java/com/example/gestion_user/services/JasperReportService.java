package com.example.gestion_user.services;

import com.example.gestion_user.entities.Case;
import com.example.gestion_user.entities.report.Report;
import com.example.gestion_user.entities.report.StringResult;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface JasperReportService {
    void generateReport(List<Case> cases) throws JRException;
    String generatePdf(Map<String, Object> params);
    StringResult createReport(Report report) throws SQLException;
   // StringResult generatePdfReport(Map<String, Object> params) throws SQLException, IOException, JRException ;
}
