package it.univaq.disim.mwt.j2etpapp.repository.jpa;

import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChannelRoleRepository extends CrudRepository<UserChannelRole, Long> {
    Optional<List<UserChannelRole>> findByChannelId(Long channelId);
    Optional<List<UserChannelRole>> findByChannelIdAndRoleId(Long channelId, Long RoleId);
    Optional<UserChannelRole> findByChannelIdAndUserId(Long channelId, Long userId);
}
