package it.univaq.disim.mwt.j2etpapp.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // TODO: adjust error.html
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest req, Exception ex, Model model) {
        log.info("Exception Occured:: URL=" + req.getRequestURL() + ", method=" + req.getMethod());
        log.info((ex.getCause() != null) ? ex.getCause().getMessage() : ex.getMessage());

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        printWriter.flush();

        // TODO: debug
        ex.printStackTrace();

        String errorCause = (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getMessage();
        model.addAttribute("status", (ex instanceof AccessDeniedException) ? 403 : 500);
        model.addAttribute("message", errorCause);
        model.addAttribute("trace", stringWriter.toString());
        return "error";
    }
}
