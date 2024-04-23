package com.example.gestion_user.models.request;

import com.example.gestion_user.entities.enums.FeeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeeDto {


    @NotBlank
    private String reference ;

    private float amount ;

    private LocalDate date ;

    private FeeType type ;

    private String remain ;
}
