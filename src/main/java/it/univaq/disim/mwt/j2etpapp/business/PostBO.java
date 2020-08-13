package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.PostClass;

import java.util.List;

public interface PostBO {

    List<PostClass> findAll();
    PostClass findById(String id);
    void save(PostClass post);
    void saveAll(List<PostClass> posts);
    void saveAll(PostClass... posts);
    void deleteById(String id);
    void delete(PostClass post);
    Long count();

}
