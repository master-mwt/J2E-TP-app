package it.univaq.disim.mwt.j2etpapp.presentation;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO: logging !!
    // TODO: adjust error.html
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest req, Exception ex, Model model) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        printWriter.flush();

        String errorCause = (ex.getCause() != null) ? ex.getCause().getMessage() : "";
        model.addAttribute("status", 500);
        model.addAttribute("message", errorCause);
        model.addAttribute("trace", stringWriter.toString());
        return "error";
    }
}
