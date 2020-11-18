package it.univaq.disim.mwt.postd.repository.jpa;

import it.univaq.disim.mwt.postd.domain.ChannelClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends PagingAndSortingRepository<ChannelClass, Long> {
    Optional<ChannelClass> findByName(String name);
    Optional<List<ChannelClass>> findByNameContains(String name);
    Page<ChannelClass> findByNameContains(String name, Pageable pageable);
}
