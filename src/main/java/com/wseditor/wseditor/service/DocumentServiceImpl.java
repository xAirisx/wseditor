package com.wseditor.wseditor.service;

import com.wseditor.wseditor.model.Document;
import com.wseditor.wseditor.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document addDocument(Document document) {

        Document savedDocument = documentRepository.saveAndFlush(document);
        return savedDocument;
    }

    @Override
    public void delete(Document document) {
        documentRepository.delete(document);
    }

    @Override
    public Document editDocument(Document document) {
        return documentRepository.saveAndFlush(document);
    }

    @Override
    public List<Document> getAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentByName(String name)
    {
        return documentRepository.findByName(name);
    }

}

