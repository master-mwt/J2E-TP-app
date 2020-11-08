package it.univaq.disim.mwt.j2etpapp.repository.mongo;

import it.univaq.disim.mwt.j2etpapp.domain.NotificationClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationClass, String> {
}
