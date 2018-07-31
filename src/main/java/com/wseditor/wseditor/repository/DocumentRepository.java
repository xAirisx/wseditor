package com.wseditor.wseditor.repository;

import com.wseditor.wseditor.model.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer> {


    List<Document> findAllByType(Document.DocumentType type);
    List<Document> findAllByMainDocId(Integer id);
    Document getDocumentById(Integer id);



}
