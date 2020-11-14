package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.GroupBO;
import it.univaq.disim.mwt.j2etpapp.domain.GroupClass;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
public class GroupBOImpl implements GroupBO {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<GroupClass> findAll() {
        return (List<GroupClass>) groupRepository.findAll();
    }

    @Override
    public GroupClass findByName(String name) {
        return groupRepository.findByName(name).orElse(null);
    }

    @Override
    public void save(GroupClass group) {
        groupRepository.save(group);
    }

    @Override
    public void saveAll(List<GroupClass> groups) {
        groupRepository.saveAll(groups);
    }

    @Override
    public void saveAll(GroupClass... groups) {
        groupRepository.saveAll(Arrays.asList(groups));
    }

    @Override
    public void deleteById(Long id) {
        groupRepository.deleteById(id);
        log.info("Deleted group with id " + id);
    }

    @Override
    public void delete(GroupClass group) {
        Long groupId = group.getId();

        groupRepository.delete(group);

        log.info("Deleted group with id " + groupId);
    }

    @Override
    public Long count() {
        return groupRepository.count();
    }
}
