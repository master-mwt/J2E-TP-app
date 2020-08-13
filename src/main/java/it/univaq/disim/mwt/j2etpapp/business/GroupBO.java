package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.GroupClass;

import java.util.List;

public interface GroupBO {

    List<GroupClass> findAll();
    GroupClass findByName(String name);
    void save(GroupClass group);
    void saveAll(List<GroupClass> groups);
    void saveAll(GroupClass... groups);
    void deleteById(Long id);
    void delete(GroupClass group);
    Long count();

}
