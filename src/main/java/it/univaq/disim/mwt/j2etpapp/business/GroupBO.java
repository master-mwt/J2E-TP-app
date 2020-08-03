package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.GroupClass;

import java.util.List;

public interface GroupBO {

    List<GroupClass> findAllGroups();
    void save(GroupClass groupClass);

}
