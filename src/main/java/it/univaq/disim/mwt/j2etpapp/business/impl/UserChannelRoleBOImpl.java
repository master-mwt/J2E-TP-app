package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.UserChannelRoleBO;
import it.univaq.disim.mwt.j2etpapp.domain.UserChannelRole;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserChannelRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class UserChannelRoleBOImpl implements UserChannelRoleBO {

    @Autowired
    private UserChannelRoleRepository userChannelRoleRepository;

    @Override
    public List<UserChannelRole> findAll() {
        return (List<UserChannelRole>) userChannelRoleRepository.findAll();
    }

    @Override
    public List<UserChannelRole> findByChannelId(Long channelId) {
        return userChannelRoleRepository.findByChannelId(channelId).orElse(null);
    }

    @Override
    public Page<UserChannelRole> findByChannelIdPaginated(Long channelId, int page, int size) {
        return new Page<>(userChannelRoleRepository.findByChannelId(channelId, PageRequest.of(page, size)));
    }

    @Override
    public List<UserChannelRole> findByChannelIdAndRoleId(Long channelId, Long roleId) {
        return userChannelRoleRepository.findByChannelIdAndRoleId(channelId, roleId).orElse(new ArrayList<>());
    }

    @Override
    public UserChannelRole findByChannelIdAndUserId(Long channelId, Long userId) {
        return userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
    }

    @Override
    public void save(UserChannelRole userChannelRole) {
        userChannelRoleRepository.save(userChannelRole);
    }

    @Override
    public void saveAll(List<UserChannelRole> userChannelRoles) {
        userChannelRoleRepository.saveAll(userChannelRoles);
    }

    @Override
    public void saveAll(UserChannelRole... userChannelRoles) {
        userChannelRoleRepository.saveAll(Arrays.asList(userChannelRoles));
    }

    @Override
    public void deleteById(Long id) {
        userChannelRoleRepository.deleteById(id);
    }

    @Override
    public void delete(UserChannelRole userChannelRole) {
        userChannelRoleRepository.delete(userChannelRole);
    }

    @Override
    public Long count() {
        return userChannelRoleRepository.count();
    }
}
