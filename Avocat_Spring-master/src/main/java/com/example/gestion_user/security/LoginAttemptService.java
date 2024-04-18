package com.example.gestion_user.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptService {//suivre les tentatives de connexion des utilisateurs
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String, Integer> loginAttemptCache;//stocker les tentatives de connexion

    public LoginAttemptService() {//Initialise la cache des tentatives de connexion
        super();
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15,MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void removeUserFromLoginAttemptCache(String username) {
        loginAttemptCache.invalidate(username);
    }//Supprime l'utilisateur spécifié de la cache des tentatives de connexion

    public void addUserToLoginAttemptCache(String username) {//Ajoute un utilisateur à la cache des tentatives de connexion
        int attempts = 0;
        try {
            attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(username);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        loginAttemptCache.put(username,attempts);
    }

    public boolean hasExceededMaxAttempts(String username) {//Vérifie si l'utilisateur a dépassé le nombre maximum de tentatives autorisées en consultant la cache
        try {
            return loginAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
