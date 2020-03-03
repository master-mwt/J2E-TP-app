package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.ServiceBO;
import it.univaq.disim.mwt.j2etpapp.domain.Service;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@Transactional
public class ServiceBOImpl implements ServiceBO {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<Service> findAllServices() {
        return (List<Service>) serviceRepository.findAll();
    }

    @Override
    public void save(Service service) {
        serviceRepository.save(service);
    }
}
