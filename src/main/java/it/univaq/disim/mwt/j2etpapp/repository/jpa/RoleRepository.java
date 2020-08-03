package it.univaq.disim.mwt.j2etpapp.repository.jpa;

import it.univaq.disim.mwt.j2etpapp.domain.RoleClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleClass, Long> {
}
