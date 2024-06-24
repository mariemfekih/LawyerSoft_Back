package com.example.gestion_user.models.request;

import com.example.gestion_user.entities.Court;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrialDto {
    private String title;

    private String description;

    private String judgement;
    private Court courtInstance;

}
