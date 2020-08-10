package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ServiceClass;

import java.util.List;

public interface ServiceBO {

    List<ServiceClass> findAll();
    ServiceClass findByName(String name);
    void save(ServiceClass service);

}
