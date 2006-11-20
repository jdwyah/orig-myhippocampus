package com.aavu.server.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.server.web.domain.FileUploadBean;

public class FileUploadController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(FileUploadController.class);
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		log.debug("onSubmit");
		
		// cast the bean
        FileUploadBean bean = (FileUploadBean) command;

    	log.debug("bean  "+bean);
        
        //let's see if there's content there
        byte[] file = bean.getFile();
        if (file == null) {
             // hmm, that's strange, the user did not upload anything
        	log.warn("Uploaded null");
        }else{
        	log.debug("Uploaded ok");
        }

        

		return new ModelAndView(getSuccessView(),"string","Success");
		
	}

	@Override
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {		
		// to actually be able to convert Multipart instance to byte[]
		// we have to register a custom editor
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		// now Spring knows how to handle multipart object and convert them
	}


}
