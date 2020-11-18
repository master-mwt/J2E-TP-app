package it.univaq.disim.mwt.postd.business;

import it.univaq.disim.mwt.postd.domain.TagClass;

import java.util.List;

public interface TagBO {

    List<TagClass> findAll();
    TagClass findById(String id);
    TagClass findByName(String name);
    List<TagClass> findByNameContains(String name);
    Page<TagClass> findByNameContainsPaginated(String name, int page, int size);
    void save(TagClass tag);
    void saveAll(List<TagClass> tags);
    void saveAll(TagClass... tags);
    void deleteById(String id);
    void delete(TagClass tag);
    Long count();

}
