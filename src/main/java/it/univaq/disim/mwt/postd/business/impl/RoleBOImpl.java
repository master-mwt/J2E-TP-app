package it.univaq.disim.mwt.postd.business.impl;

import it.univaq.disim.mwt.postd.business.RoleBO;
import it.univaq.disim.mwt.postd.domain.RoleClass;
import it.univaq.disim.mwt.postd.repository.jpa.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RoleBOImpl implements RoleBO {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleClass> findAll() {
        return (List<RoleClass>) roleRepository.findAll();
    }

    @Override
    public RoleClass findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public RoleClass findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    @Override
    public void save(RoleClass role) {
        roleRepository.save(role);
    }

    @Override
    public void saveAll(List<RoleClass> roles) {
        roleRepository.saveAll(roles);
    }

    @Override
    public void saveAll(RoleClass... roles) {
        roleRepository.saveAll(Arrays.asList(roles));
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
        log.info("Deleted role with id " + id);
    }

    @Override
    public void delete(RoleClass role) {
        Long roleId = role.getId();

        roleRepository.delete(role);

        log.info("Deleted role with id " + roleId);
    }

    @Override
    public Long count() {
        return roleRepository.count();
    }
}
