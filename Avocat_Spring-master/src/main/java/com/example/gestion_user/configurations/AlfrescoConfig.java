package com.example.gestion_user.configurations;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class AlfrescoConfig {
    @Bean
    public Session createSession() {

        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameters = new HashMap<String, String>();

        // user credentials
        parameters.put(SessionParameter.USER, "admin");
        parameters.put(SessionParameter.PASSWORD, "admin");

        // connection settings
        parameters.put(SessionParameter.ATOMPUB_URL, "http://localhost:8080/alfresco/api/-default-/public/cmis/versions/1.0/atom");
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        Session session = factory.getRepositories(parameters).get(0).createSession();
        return session;
    }
}
