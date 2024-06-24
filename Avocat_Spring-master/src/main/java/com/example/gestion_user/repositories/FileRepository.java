package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.File;
import com.example.gestion_user.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File,Long> {
    Optional<File> findByPath(String path);
    void deleteById(Long id);
    @Query("SELECT f FROM File f WHERE f.folder.idFolder = :folderId")
    List<File> findByFolderId(@Param("folderId") Long folderId);


}
