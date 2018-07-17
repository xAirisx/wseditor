package com.wseditor.wseditor.service;

import com.wseditor.wseditor.domain.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentService {

    Document addDocument(Document document);
    void delete(Document document);
    Document editDocument(Document document);
    Document getDocumentByName(String name);
    List<Document> getAll();

}
