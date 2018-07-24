package com.wseditor.wseditor.service;

import com.wseditor.wseditor.model.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Document addDocument(Document document);
    void delete(Integer id);
    Document editDocument(Document document);
    Document getDocumentByName(String name);
    Iterable<Document> getAll();
    Optional<Document> getDocumentById(Integer id);

}
