package com.wseditor.wseditor.controller;


import com.wseditor.wseditor.model.Document;
import com.wseditor.wseditor.service.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.jws.WebParam;
import javax.print.Doc;
import java.security.Principal;
import java.util.Optional;

@Controller
public class DocumentController {

    @Autowired
    @Qualifier(value="documentService")
    DocumentServiceImpl documentService;


    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public ModelAndView getAllDocuments(ModelAndView model, Principal principal)
    {
        model.addObject("username", principal.getName());
        model.addObject("documents", documentService.getAll());
        model.setViewName("homepage");
        return model;
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public ModelAndView logoutSuccessfulPage() {

        System.out.println("logout");
        ModelAndView model = new ModelAndView(new RedirectView("/"));
        return model;
    }

    @RequestMapping(value = "/document")
    public ModelAndView getDocument(@RequestParam String id, ModelAndView model, Principal principal) {

        model.addObject("username", principal.getName());
        Optional<Document> docOptional =  documentService.getDocumentById(Integer.parseInt(id));
        if (docOptional.isPresent()){
            Document doc = docOptional.get();
            model.addObject("document", doc);
            model.setViewName("document");
        }
        else
        {
            model = new ModelAndView(new RedirectView("/homepage"));
            model.addObject("errorMessage", "Document not found!");
        }
        return model;
    }

    @RequestMapping(value = "/deleteDocument")
    public ModelAndView deleteDocument(@RequestBody String id)
    {
        documentService.delete(Integer.parseInt(id));
        ModelAndView model = new ModelAndView(new RedirectView("homepage"));
        return model;
    }

    @RequestMapping(value = "/addDocument")
    public ModelAndView addDocument()
    {
        Document doc = new Document();
        documentService.addDocument(doc);
        doc.setName("new file " +  doc.getId());
        documentService.editDocument(doc);
        ModelAndView model = new ModelAndView(new RedirectView("homepage"));
        return model;
    }

    @RequestMapping(value = "/updateDocument")
    public ResponseEntity<?> updateDocument(@RequestBody Document document) {

        documentService.editDocument(document);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
}
