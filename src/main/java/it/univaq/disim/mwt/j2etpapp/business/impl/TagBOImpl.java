package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.TagBO;
import it.univaq.disim.mwt.j2etpapp.domain.TagClass;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TagBOImpl implements TagBO {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<TagClass> findAll() {
        return (List<TagClass>) tagRepository.findAll();
    }

    @Override
    public TagClass findById(String id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public TagClass findByName(String name) {
        return tagRepository.findByName(name).orElse(null);
    }

    @Override
    public TagClass findByNameContains(String name) {
        return tagRepository.findByNameContains(name).orElse(null);
    }

    @Override
    public void save(TagClass tag) {
        tagRepository.save(tag);
    }

    @Override
    public void saveAll(List<TagClass> tags) {
        tagRepository.saveAll(tags);
    }

    @Override
    public void saveAll(TagClass... tags) {
        tagRepository.saveAll(Arrays.asList(tags));
    }

    @Override
    public void deleteById(String id) {
        tagRepository.deleteById(id);
    }

    @Override
    public void delete(TagClass tag) {
        tagRepository.delete(tag);
    }

    @Override
    public Long count() {
        return tagRepository.count();
    }
}
