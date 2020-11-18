package it.univaq.disim.mwt.postd.repository.jpa;

import it.univaq.disim.mwt.postd.domain.UserClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserClass, Long> {
    Optional<UserClass> findByUsername(String username);
    Optional<List<UserClass>> findByUsernameContains(String username);
    Page<UserClass> findByUsernameContains(String username, Pageable pageable);
    Optional<List<UserClass>> findByEmailContains(String email);
    Page<UserClass> findByEmailContains(String email, Pageable pageable);
}
