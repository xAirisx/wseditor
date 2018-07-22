package com.wseditor.wseditor.controller;


import com.wseditor.wseditor.config.WebSocketHandler;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class DocumentController {

    @Autowired
    @Qualifier(value="documentService")
    DocumentServiceImpl documentService;

    @Autowired
    WebSocketHandler webSocketHandler;

    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public ModelAndView getAllDocuments(ModelAndView model, Principal principal)
    {
        model.addObject("username", principal.getName());
        model.addObject("documents", documentService.getAll());
        model.setViewName("homepage");
        return model;
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public ModelAndView logoutSuccessfulPage(ModelAndView model) {

        System.out.println("logout");
        model = new ModelAndView(new RedirectView("/"));
        return model;
    }

    @RequestMapping(value = "/document")
    public ModelAndView getDocument(@RequestParam String name, ModelAndView model, Principal principal) {

        model.addObject("users", webSocketHandler.users);
        model.addObject("username", principal.getName());
        model.addObject("document", documentService.getDocumentByName(name));
        System.out.println(documentService.getDocumentByName(name).getText());
        model.setViewName("document");
        return model;
    }

    @RequestMapping(value = "/deleteDocument")
    public ModelAndView deleteDocument(@RequestBody String id, ModelAndView model)
    {
        documentService.delete(Integer.parseInt(id));
        model = new ModelAndView(new RedirectView("homepage"));
        return model;
    }

    @RequestMapping(value = "/addDocument")
    public ModelAndView addDocument(ModelAndView model)
    {
        Document doc = new Document();
        documentService.addDocument(doc);
        doc.setName("new file " +  doc.getId());
        documentService.editDocument(doc);
        model = new ModelAndView(new RedirectView("homepage"));
        return model;
    }

    @RequestMapping(value = "/updateDocument")
    public void updateDocument(@RequestBody Document document) {

        documentService.editDocument(document);
    }
}
