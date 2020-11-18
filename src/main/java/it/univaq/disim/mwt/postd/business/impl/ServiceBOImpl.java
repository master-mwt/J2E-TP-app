package it.univaq.disim.mwt.postd.business.impl;

import it.univaq.disim.mwt.postd.business.ServiceBO;
import it.univaq.disim.mwt.postd.domain.ServiceClass;
import it.univaq.disim.mwt.postd.repository.jpa.ServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ServiceBOImpl implements ServiceBO {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<ServiceClass> findAll() {
        return (List<ServiceClass>) serviceRepository.findAll();
    }

    @Override
    public ServiceClass findByName(String name) {
        return serviceRepository.findByName(name).orElse(null);
    }

    @Override
    public void save(ServiceClass service) {
        serviceRepository.save(service);
    }

    @Override
    public void saveAll(List<ServiceClass> services) {
        serviceRepository.saveAll(services);
    }

    @Override
    public void saveAll(ServiceClass... services) {
        serviceRepository.saveAll(Arrays.asList(services));
    }

    @Override
    public void deleteById(Long id) {
        serviceRepository.deleteById(id);
        log.info("Deleted service with id " + id);
    }

    @Override
    public void delete(ServiceClass service) {
        Long serviceId = service.getId();

        serviceRepository.delete(service);

        log.info("Deleted service with id " + serviceId);
    }

    @Override
    public Long count() {
        return serviceRepository.count();
    }
}
