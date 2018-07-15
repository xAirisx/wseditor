package com.wseditor.wseditor.controller;


import com.wseditor.wseditor.service.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        return "docs";
    }
}
