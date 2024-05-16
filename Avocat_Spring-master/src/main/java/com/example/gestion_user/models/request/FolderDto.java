package com.example.gestion_user.models.request;

import com.example.gestion_user.entities.enums.FolderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FolderDto {

    @NotBlank
    private String name;
    private FolderStatus status;
}
