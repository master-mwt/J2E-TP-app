package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.Service;

import java.util.List;

public interface ServiceBO {

    List<Service> findAllServices();
    void save(Service service);

}
