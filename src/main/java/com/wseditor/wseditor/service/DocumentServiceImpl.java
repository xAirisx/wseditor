package com.wseditor.wseditor.service;

import com.wseditor.wseditor.model.Document;
import com.wseditor.wseditor.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document addDocument(Document document) {

        Document savedDocument = documentRepository.save(document);
        return savedDocument;
    }

    @Override
    public void delete(Integer id) {
        documentRepository.deleteById(id);
    }

    @Override
    public Document editDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Iterable<Document> getAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentByName(String name)
    {
        return documentRepository.findByName(name);
    }

    @Override
    public Optional<Document> getDocumentById(Integer id)
    {
        return documentRepository.findById(id);

    }

}

