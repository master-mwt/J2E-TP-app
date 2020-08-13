package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.TagClass;

import java.util.List;

public interface TagBO {

    List<TagClass> findAll();
    TagClass findById(String id);
    TagClass findByName(String name);
    void save(TagClass tag);
    void saveAll(List<TagClass> tags);
    void saveAll(TagClass... tags);
    void deleteById(String id);
    void delete(TagClass tag);
    Long count();

}
