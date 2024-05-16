package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByName(String name);


   /* @Query("SELECT d.dossierName FROM Folder d")
    List<String> findAllNomDossiers();*/
    /*@Query("SELECT c.pdfContent FROM Courrier c WHERE c.dossier.idDossier = :idDossier")
    List<byte[]> findPdfsByDossierId(@Param("idDossier") Integer idDossier);*/


}
