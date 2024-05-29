package com.example.gestion_user.services.servicesImpl;

import com.example.gestion_user.entities.Appointment;
import com.example.gestion_user.entities.User;
import com.example.gestion_user.repositories.AppointmentRepository;
import com.example.gestion_user.services.AppointmentService;
import com.example.gestion_user.services.EmailService;
import com.example.gestion_user.services.UserService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.google.api.client.json.JsonFactory;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String APPLICATION_NAME = "Calendar API";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "client_secret.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private List < Appointment > appointmentsList;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Override
    public Appointment createAppointment(Appointment appointment) throws MessagingException, IOException, GeneralSecurityException {
        List<User> userList = new ArrayList<>();
        for (User user : appointment.getUsers()) {
            Optional<User> user1 = userService.getUserByid(user.getId());
            userList.add(user1.get());
        }

        String link = this.newAppointment(userList,appointment);
        appointment.setLinkHangout(link);
        for (User e : userList) {
            emailService.sendEmailCalendar(appointment, e);
        }
        return appointmentRepository.save(appointment);
    }




    @Override
    public Optional<Appointment> getAppointment(Long appointmentId) {

        return appointmentRepository.findById(appointmentId);
    }

    @Override
    public List<Appointment> getAllAppointment() {

        return appointmentRepository.findAll();
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) {

        return appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointment(Long appointmentId) {

        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    public List<Appointment> getAppointmentsByUser(User user) {

        return appointmentRepository.getAppointmentsByUsersOrderByDate(user);
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream inputStream = new ClassPathResource("credentials.json").getInputStream();
        InputStream inputStreamTokens = new ClassPathResource(TOKENS_DIRECTORY_PATH).getInputStream();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStreamTokens));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(String.valueOf(inputStream))))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public String newAppointment(List<User> attendeeEmails, Appointment appointment) throws IOException, GeneralSecurityException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Credential credential = getCredentials(httpTransport);
        Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Create a new appointment with conference data
        List<EventAttendee> attendees=new ArrayList<>();
        com.google.api.services.calendar.model.Event.Organizer organizer =new com.google.api.services.calendar.model.Event.Organizer();
        com.google.api.services.calendar.model.Event appointment2 = new com.google.api.services.calendar.model.Event();
        organizer.setEmail("mariembf6@gmail.com");
        appointment2.setOrganizer(organizer);
        appointment2.setSummary(appointment.getTitle());
        appointment2.setDescription(appointment.getDescription());

        DateTime startDateTime = new DateTime(String.valueOf(appointment.getStartTime()));
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Paris");
        appointment2.setStart(start);

        DateTime endDateTime = new DateTime(String.valueOf(appointment.getEndTime()));
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Paris");
        appointment2.setEnd(end);

        for(User userAttendee:attendeeEmails){
            EventAttendee attendee = new EventAttendee();
            attendee.setEmail(userAttendee.getEmail());
            attendees.add(attendee);
        }

        appointment2.setAttendees(attendees);
        ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey()
                .setType("hangoutsMeet");
        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest()
                .setRequestId(UUID.randomUUID().toString())
                .setConferenceSolutionKey(conferenceSolutionKey);
        ConferenceData conferenceData = new ConferenceData()
                .setCreateRequest(createConferenceRequest);
        EventReminder c =new EventReminder();
        c.setMinutes(10);

        appointment2.setConferenceData(conferenceData);
        service.events().insert("primary", appointment2).execute();
        com.google.api.services.calendar.model.Event createdAppointment = service.events().insert("primary", appointment2).setConferenceDataVersion(1).setSendNotifications(true).execute();

        // Print the Google Meet link
        String meetLink = createdAppointment.getHangoutLink();

        System.out.printf("Google Meet link: %s\n", meetLink);
        System.out.println(createdAppointment);

        // handle the exception
        System.out.println("TEST");
        return meetLink;
    }


    public void AppointmentServiceImplementation(List <Appointment> appointmentsList) {
        this.appointmentsList = appointmentsList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Appointments");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Type Appointment", style);
        createCell(row, 2, "Title", style);
        createCell(row, 3, "Description", style);
        createCell(row, 4, "Date", style);
        createCell(row, 5, "Start Time", style);
        createCell(row, 6, "End Time .", style);
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else {
            cell.setCellValue((String) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Appointment record: appointmentsList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getIdAppointment(), style);
            createCell(row, columnCount++, record.getType().toString(), style);
            createCell(row, columnCount++, record.getTitle(), style);
            createCell(row, columnCount++, record.getDescription(), style);
            createCell(row, columnCount++, record.getDate().toString(), style);
            createCell(row, columnCount++, record.getStartTime().toString(), style);
            createCell(row, columnCount++, record.getEndTime().toString(), style);
        }
    }

    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
