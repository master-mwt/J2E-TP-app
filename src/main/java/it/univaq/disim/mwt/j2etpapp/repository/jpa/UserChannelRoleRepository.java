package it.univaq.disim.mwt.j2etpapp.repository.jpa;

import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChannelRoleRepository extends CrudRepository<UserChannelRole, Long> {
    List<UserChannelRole> findUserChannelRolesByChannelId(Long channel_id);
}
