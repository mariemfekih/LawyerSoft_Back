package com.example.gestion_user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFailureListener {

    @Autowired
    private LoginAttemptService loginAttemptService;
    //Cette méthode est annotée avec @EventListener pour indiquer à Spring qu'elle doit être exécutée lorsqu'un événement de type AuthenticationFailureBadCredentialsEvent est publié.
    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        Object principal = event.getAuthentication().getPrincipal();//Récupère le principal de l'objet d'authentification qui a échoué.
        if(principal instanceof String) {//si le principal est une chaîne (username)
            String username = (String) event.getAuthentication().getPrincipal();
            loginAttemptService.addUserToLoginAttemptCache(username);//Ajoute le nom d'utilisateur à la cache des tentatives de connexion
        }

    }
}
