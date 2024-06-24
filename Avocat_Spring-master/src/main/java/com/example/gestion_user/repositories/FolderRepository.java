package com.example.gestion_user.repositories;

import com.example.gestion_user.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByName(String name);

    @Query("SELECT f FROM Folder f WHERE f.parentFolder.idFolder = :parentId")
    List<Folder> findByParentFolderId(@Param("parentId") Long parentId);



    @Query("SELECT f FROM Folder f JOIN f.customerInstance c JOIN c.user u WHERE u.id = :userId AND (f.parentFolder IS NULL)")
    List<Folder> findFoldersByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Folder f SET f.name = :newName WHERE f.idFolder = :folderId")
    void updateNameById(Long folderId, String newName);

    Optional<Folder> findByPath(String path);

    List<Folder> findByParentFolder(Folder folder);
}
