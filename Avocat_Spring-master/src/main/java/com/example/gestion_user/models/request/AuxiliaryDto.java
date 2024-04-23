package com.example.gestion_user.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuxiliaryDto {

    private String firstName;

    private String lastName;

    private String cin;

    private String email;

    private String job;

    private String phone;

    private String city;

    private Date birthDate;
}
