package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ImageClass;

public interface ImageBO {
    ImageClass findByCaption(String caption);
}
