package it.univaq.disim.mwt.postd.repository.jpa;

import it.univaq.disim.mwt.postd.domain.ImageClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends CrudRepository<ImageClass, Long> {
    Optional<ImageClass> findByCaption(String caption);
}
