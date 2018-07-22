package com.wseditor.wseditor.repository;

import com.wseditor.wseditor.model.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer> {

   Document findByName(String name);

}
