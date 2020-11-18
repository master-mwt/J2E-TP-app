package it.univaq.disim.mwt.postd.repository.jpa;

import it.univaq.disim.mwt.postd.domain.RoleClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleClass, Long> {
    Optional<RoleClass> findByName(String name);
}
