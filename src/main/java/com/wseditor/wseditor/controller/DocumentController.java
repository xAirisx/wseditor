package com.wseditor.wseditor.controller;


import com.wseditor.wseditor.domain.Document;
import com.wseditor.wseditor.service.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class DocumentController {

    @Autowired
    @Qualifier(value="documentService")
    DocumentServiceImpl documentService;

    @RequestMapping(value = "/getAllDocuments", method = RequestMethod.GET)
    public String getAllDocuments(Model model)
    {
        model.addAttribute("documents", documentService.getAll());
        System.out.println("Controller see all");
        return "docs";
    }

    @RequestMapping(value = "/document")
    public String getDocument(@RequestParam String name, Model model) {
        model.addAttribute("document", documentService.getDocumentByName(name));
        System.out.println("Controller");
        System.out.println(documentService.getDocumentByName(name).getText());
        return "document";
    }

    @RequestMapping(value = "/updatedocument")
    public void updateDocument(@RequestParam String id, @RequestParam String text)
    {
        System.out.println("Saving");
        documentService.changeTextById(Integer.parseInt(id), text);
    }

    @RequestMapping(value = "/savedocument")
    public void updateDocument(@RequestParam Document doc)
    {
        documentService.editDocument(doc);
    }

    @RequestMapping(value = "/adddocument")
    public void addDocument(@RequestParam (value="name", defaultValue = "new file") String name, Model model)
    {
        System.out.println("Please work");
        Document doc = new Document(name);
        documentService.addDocument(doc);
        getDocument(name, model);
    }
}
