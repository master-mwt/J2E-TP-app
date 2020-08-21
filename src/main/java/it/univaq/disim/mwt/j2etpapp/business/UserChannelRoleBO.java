package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;

import java.util.List;

public interface UserChannelRoleBO {

    List<UserChannelRole> findAll();
    List<UserChannelRole> findByChannelId(Long channelId);
    List<UserChannelRole> findByChannelIdAndRoleId(Long channelId, Long roleId);
    UserChannelRole findByChannelIdAndUserId(Long channelId, Long userId);
    void save(UserChannelRole userChannelRole);
    void saveAll(List<UserChannelRole> userChannelRoles);
    void saveAll(UserChannelRole... userChannelRoles);
    void deleteById(Long id);
    void delete(UserChannelRole userChannelRole);
    Long count();

}
