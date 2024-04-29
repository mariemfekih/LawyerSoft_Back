package com.example.gestion_user.models.request;

import com.example.gestion_user.entities.Contributor;
import com.example.gestion_user.entities.enums.ContributorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ContributorDto {
    private ContributorType type;
    private Long idAuxiliary; // Include the id of the auxiliary associated with the contributor



    // Static method to convert from Contributor entity to ContributorDTO
    public static ContributorDto fromEntity(Contributor contributor) {
        return new ContributorDto(
                contributor.getType(),
                contributor.getAuxiliary() != null ? contributor.getAuxiliary().getIdAuxiliary() : null
        );
    }
}
