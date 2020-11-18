package it.univaq.disim.mwt.postd.repository.jpa;

import it.univaq.disim.mwt.postd.domain.UserChannelRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChannelRoleRepository extends CrudRepository<UserChannelRole, Long> {
    Optional<List<UserChannelRole>> findByChannelId(Long channelId);
    Optional<List<UserChannelRole>> findByUserId(Long userId);
    Page<UserChannelRole> findByChannelId(Long channelId, Pageable pageable);
    Optional<List<UserChannelRole>> findByChannelIdAndRoleId(Long channelId, Long RoleId);
    Optional<UserChannelRole> findByChannelIdAndUserId(Long channelId, Long userId);
}
