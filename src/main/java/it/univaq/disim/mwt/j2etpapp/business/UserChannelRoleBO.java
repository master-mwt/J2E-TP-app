package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;

import java.util.List;

public interface UserChannelRoleBO {
    List<UserChannelRole> findUserChannelRolesByChannelId(Long channel_id);
}
