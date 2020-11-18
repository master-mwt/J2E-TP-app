package it.univaq.disim.mwt.postd.repository.mongo;

import it.univaq.disim.mwt.postd.domain.TagClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends PagingAndSortingRepository<TagClass, String> {
    Optional<TagClass> findByName(String name);
    Optional<List<TagClass>> findByNameContains(String name);
    Page<TagClass> findByNameContains(String name, Pageable pageable);
}
