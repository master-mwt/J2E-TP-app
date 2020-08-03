package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.ServiceBO;
import it.univaq.disim.mwt.j2etpapp.domain.ServiceClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServiceBOImpl implements ServiceBO {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<ServiceClass> findAllServices() {
        return (List<ServiceClass>) serviceRepository.findAll();
    }

    @Override
    public void save(ServiceClass service) {
        serviceRepository.save(service);
    }
}
