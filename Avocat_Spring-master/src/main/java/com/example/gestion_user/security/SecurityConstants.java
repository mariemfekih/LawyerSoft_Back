package com.example.gestion_user.security;

public class SecurityConstants {

    //public static final long EXPIRATION_TIME = 8 * 60 * 60 * 1000;
    public static final long EXPIRATION_TIME = 360 * 24 * 60 * 60 * 1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String GET_ARRAYS_LLC = "OACA, Bureau d'ordre";  // a commenter
    public static final String GET_ARRAYS_ADMINISTRATION ="Bureau D'ordre";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You don't have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "/user/login", "/user/register", "/user/image/**" };
    //public static final String[] PUBLIC_URLS = { "**" };



}
