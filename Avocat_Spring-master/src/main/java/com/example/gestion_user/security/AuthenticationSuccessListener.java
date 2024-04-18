package com.example.gestion_user.security;

import com.example.gestion_user.entities.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    @Autowired
    private LoginAttemptService loginAttemptService;
    //Cette méthode est déclenchée chaque fois qu'un événement AuthenticationSuccessEvent est publié dans le contexte de l'application
    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof UserPrincipal) {
            UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.removeUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
