package com.wseditor.wseditor.service;

import com.wseditor.wseditor.model.Document;
import com.wseditor.wseditor.model.dto.request.NewVersionRequest;
import com.wseditor.wseditor.model.dto.request.UpdateDocumentRequest;

import java.util.List;

public interface DocumentService {

    void deleteDocument(Integer id);
    Document getDocumentById(Integer id) throws Exception;
    List<Document> getAllMain();
    List<Document> findAllByMainDocId(Integer id);
    void updateDocument(Integer id, UpdateDocumentRequest updateDocumentRequest);
    Document addNewVersion(Integer id, NewVersionRequest newVersionRequest);
    void addNewDocument();
    List<Document> findAllOtherVersion(Integer id);
}
