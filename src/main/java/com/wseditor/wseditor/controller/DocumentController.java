package com.wseditor.wseditor.controller;


import com.wseditor.wseditor.model.Document;
import com.wseditor.wseditor.model.dto.request.NewVersionRequest;
import com.wseditor.wseditor.model.dto.request.UpdateDocumentRequest;
import com.wseditor.wseditor.service.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import java.security.Principal;
import java.util.List;

@Controller
public class DocumentController {

    @Autowired
    @Qualifier(value = "documentService")
    DocumentServiceImpl documentService;


    @RequestMapping(value = "/homepage", method = RequestMethod.GET)
    public ModelAndView getAllDocuments(ModelAndView model, Principal principal, @RequestParam(value = "errorMessage", required = false) String errorMessage) {

        if (errorMessage != null) {

            model.addObject("errorMessage", errorMessage);
        }
        model.addObject("username", principal.getName());
        model.addObject("documents", documentService.getAllMain());
        model.setViewName("homepage");

        return model;
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public ModelAndView logoutSuccessfulPage() {

        ModelAndView model = new ModelAndView(new RedirectView("/"));
        return model;
    }

    //Document

    @RequestMapping(value = "/document/{id}", method = {RequestMethod.GET})
    public ModelAndView getDocument(@PathVariable String id, ModelAndView model, Principal principal) {

        try {

            Document document = documentService.getDocumentById(Integer.parseInt(id));
            model.addObject("username", principal.getName());
            model.addObject("document", document);
            model.addObject("version", documentService.findAllByMainDocId(Integer.parseInt(id)));
            model.setViewName("document");

        } catch (Exception e) {

            model = new ModelAndView(new RedirectView("/homepage"));
            model.addObject("errorMessage", e.getMessage());
        }
        return model;
    }

    @RequestMapping(value = "/addDocument", method = {RequestMethod.POST})
    public ResponseEntity<?> addDocument() {
        documentService.addNewDocument();
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteDocument/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity<?> deleteDocument(@PathVariable String id) {

        documentService.deleteDocument(Integer.parseInt(id));
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }


    @RequestMapping(value = "updateDocument/{id}", method = {RequestMethod.PUT})
    public ResponseEntity<?> updateDocument(@PathVariable String id, @RequestBody UpdateDocumentRequest updateDocumentRequest) {

        documentService.updateDocument(Integer.parseInt(id), updateDocumentRequest);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }


    //Version

    @RequestMapping(value = "/version/{id}", method = {RequestMethod.GET})
    public ModelAndView getVersion(@PathVariable String id, ModelAndView model, Principal principal) {

        try {

            Document version = documentService.getDocumentById(Integer.parseInt(id));
            model.addObject("username", principal.getName());
            model.addObject("version", documentService.findAllOtherVersion(Integer.parseInt(id)));
            model.addObject("document", version);
            model.addObject("mainDocumentId", version.getMainDocId());
            model.setViewName("version");

        } catch (Exception e) {
            model = new ModelAndView(new RedirectView("/homepage"));
            model.addObject("errorMessage", e.getMessage());
        }
        return model;
    }


    //Get all version by mainDocumentId
    @RequestMapping(value = "/showVersionTable/{id}", method = {RequestMethod.GET})
    public @ResponseBody
    List<Document> showVersion(@PathVariable String id) {
        return documentService.findAllByMainDocId(Integer.parseInt(id));

    }

    //Add new Version
    @RequestMapping(value = "addNewVersion/{id}", method = {RequestMethod.POST})
    public @ResponseBody
    String addNewVersion(@PathVariable String id, @RequestBody NewVersionRequest newVersionRequest, ModelAndView modelAndView) {
        if (newVersionRequest.getVersionName().length() != 0) {

            Integer newVersionId = documentService.addNewVersion(Integer.parseInt(id), newVersionRequest).getId();
            return newVersionId.toString();
        } else {
            return null;
        }
    }


}
