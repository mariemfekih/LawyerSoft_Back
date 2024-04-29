package com.example.gestion_user.models.request;

import com.example.gestion_user.entities.enums.CourtType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourtDto {
    @NotBlank
    private String adress;

    private CourtType type;

    private String phone;

    private String governorate;

    private String municipality;
}
