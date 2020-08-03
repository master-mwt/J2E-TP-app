package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.GroupBO;
import it.univaq.disim.mwt.j2etpapp.domain.GroupClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupBOImpl implements GroupBO {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<GroupClass> findAllGroups() {
        return (List<GroupClass>) groupRepository.findAll();
    }

    @Override
    public void save(GroupClass groupClass) {
        groupRepository.save(groupClass);
    }
}
