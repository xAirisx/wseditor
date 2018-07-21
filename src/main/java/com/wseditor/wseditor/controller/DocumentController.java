package com.wseditor.wseditor.controller;


import com.wseditor.wseditor.model.Document;
import com.wseditor.wseditor.service.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DocumentController {

    @Autowired
    @Qualifier(value="documentService")
    DocumentServiceImpl documentService;

    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public String getAllDocuments(Model model)
    {
       //MyUserDetails d = (MyUserDetails) principal; , Principal principal
     //   String principalName = principal.getName();
        System.out.println("getAll");
        model.addAttribute("documents", documentService.getAll());
        return "homepage";
    }

    @RequestMapping(value = "/document")
    public String getDocument(@RequestParam String name, Model model) {
        model.addAttribute("document", documentService.getDocumentByName(name));
        System.out.println(documentService.getDocumentByName(name).getText());
        return "document";
    }

    @RequestMapping(value = "/deleteDocument")
    public String deleteDocument(@RequestBody String name, Model model)
    {
        Document doc = documentService.getDocumentByName(name);
        System.out.println("Deleting");
        documentService.delete(doc);
        return getAllDocuments(model);
    }

    @RequestMapping(value = "/addDocument")
    public String addDocument(Model model)
    {
        Document doc = new Document();
        documentService.addDocument(doc);
        doc.setName("new file " +  doc.getId());
        documentService.editDocument(doc);
        return getAllDocuments(model);
    }

    @RequestMapping(value = "/updateDocument")
    public String getDocument(@RequestBody Document document, Model model) {

        documentService.editDocument(document);
        return getDocument(document.getName(), model);
    }
}
