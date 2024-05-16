package com.example.gestion_user.models.request;

import com.example.gestion_user.entities.enums.CaseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseDto {

    @NotBlank
    private String title;

    private String description;
     @NotBlank
     private String reference;

    private Date creationDate;

    private Date closingDate;

    private CaseType type;
}
