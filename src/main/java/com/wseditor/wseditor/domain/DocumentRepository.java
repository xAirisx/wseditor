package com.wseditor.wseditor.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    @Query("select d from Document d where d.name = :name")
    Document findByName(@Param("name") String name);

    @Modifying
    @Query(nativeQuery = true, value = "update Document d set d.text = :text where d.id = :id;")
    void changeTextById(@Param("id") int id, @Param("text") String text);
}
