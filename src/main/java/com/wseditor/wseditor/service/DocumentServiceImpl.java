package com.wseditor.wseditor.service;

import com.wseditor.wseditor.model.Document;
import com.wseditor.wseditor.model.dto.request.NewVersionRequest;
import com.wseditor.wseditor.model.dto.request.UpdateDocumentRequest;
import com.wseditor.wseditor.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;


    @Override
    public Document getDocumentById(Integer id) throws FileNotFoundException
    {
        Optional<Document> docOptional =  documentRepository.findById(id);
        if (docOptional.isPresent()){
            return  docOptional.get();
        }
        else
        {
            throw new FileNotFoundException("Document not found");
        }
    }

    @Override
    public void addNewDocument()
    {
        Document document = new Document();
        documentRepository.save(document);
        document.setType(Document.DocumentType.MAIN);
        document.setName("new file " +  document.getId());
        documentRepository.save(document);
    }


    @Override
    public void deleteDocument(Integer id) {
        try {
            Document document = documentRepository.getDocumentById(id);
            if(document.getType().equals((Document.DocumentType.MAIN).toString()))
            {
                for (Document version : documentRepository.findAllByMainDocId(id)) {
                    documentRepository.deleteById(version.getId());
                }
            }
            documentRepository.deleteById(id);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void updateDocument(Integer id, UpdateDocumentRequest updateDocumentRequest)
    {

        try {
            Document document = documentRepository.getDocumentById(id);
            document.setText(updateDocumentRequest.getDocumentText());
            document.setName(updateDocumentRequest.getDocumentName());
            documentRepository.save(document);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


    }


    @Override
    public List<Document> getAllMain() {

        return documentRepository.findAllByType(Document.DocumentType.MAIN);
    }


    @Override
    public List<Document> findAllByMainDocId(Integer id)
    {
        return  documentRepository.findAllByMainDocId(id);
    }


    //Version

    @Override
    public Document addNewVersion(Integer id, NewVersionRequest newVersionRequest)
    {
        Document document = new Document(newVersionRequest.getVersionName(), newVersionRequest.getVersionText(), id);
        document.setType(Document.DocumentType.EXTRA);
        return documentRepository.save(document);
    }


    @Override
    public List<Document> findAllOtherVersion(Integer id)
    {
        List<Document> versionTable = new ArrayList<>();
        try {
            Document version = getDocumentById(id);
            versionTable = documentRepository.findAllByMainDocId(version.getMainDocId());
            versionTable.remove(version);

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());

        }
        return versionTable;
    }
}

