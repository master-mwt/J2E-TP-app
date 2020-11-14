package it.univaq.disim.mwt.j2etpapp.utils;

import it.univaq.disim.mwt.j2etpapp.business.FileTypeException;
import it.univaq.disim.mwt.j2etpapp.business.UserChannelRoleBO;
import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.ImageClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import it.univaq.disim.mwt.j2etpapp.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class UtilsClass {

    @Autowired
    private UserChannelRoleBO userChannelRoleBO;

    public UserClass getPrincipal() {
        return (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser() : null;
    }

    public UserChannelRole getSubscription(ChannelClass channel, UserClass principal) {
        return (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsImpl) ? userChannelRoleBO.findByChannelIdAndUserId(channel.getId(), principal.getId()) : null;
    }

    public ImageClass fillImageData(String path, String contentType) throws IOException, FileTypeException {
        if(!checkContentTypeValidity(contentType)) {
            log.info("fillImageData: the file is not an image");
            throw new FileTypeException("The uploaded file is not an image");
        }

        ImageClass imageClass = new ImageClass();
        imageClass.setLocation(path);
        imageClass.setType(contentType);

        BufferedImage bimg = ImageIO.read(new File(path));
        imageClass.setSize(bimg.getWidth() + "x" + bimg.getHeight());

        return imageClass;
    }

    public boolean checkContentTypeValidity(String contentType) {
        return (contentType != null && contentType.contains("image"));
    }
}
