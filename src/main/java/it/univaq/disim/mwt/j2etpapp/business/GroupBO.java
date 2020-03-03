package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.Group;

import java.util.List;

public interface GroupBO {

    List<Group> findAllGroups();
    void save(Group group);

}
