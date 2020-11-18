package it.univaq.disim.mwt.postd.repository.jpa;

import it.univaq.disim.mwt.postd.domain.ServiceClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends CrudRepository<ServiceClass, Long> {
    Optional<ServiceClass> findByName(String name);
}
