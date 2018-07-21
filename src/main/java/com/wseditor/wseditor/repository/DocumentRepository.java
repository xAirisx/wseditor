package com.wseditor.wseditor.repository;

import com.wseditor.wseditor.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    @Query("select d from Document d where d.name = :name")
    Document findByName(@Param("name") String name);

}