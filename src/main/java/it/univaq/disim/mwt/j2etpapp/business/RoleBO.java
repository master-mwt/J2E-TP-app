package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.RoleClass;

import java.util.List;

public interface RoleBO {

    List<RoleClass> findAll();
    RoleClass findById(Long id);
    RoleClass findByName(String name);
    void save(RoleClass role);
    void saveAll(List<RoleClass> roles);
    void saveAll(RoleClass... roles);
    void deleteById(Long id);
    void delete(RoleClass role);
    Long count();

}
