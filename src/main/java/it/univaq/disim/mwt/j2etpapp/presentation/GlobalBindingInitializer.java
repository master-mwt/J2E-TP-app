package it.univaq.disim.mwt.j2etpapp.presentation;

import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class GlobalBindingInitializer {

    @Autowired
    private ApplicationProperties properties;

    @InitBinder
    public void binder(WebDataBinder binder) {
        binder.addCustomFormatter(new DateFormatter(properties.getDateFormat()));
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(properties.getDateFormat()), false));
    }
}
