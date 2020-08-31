package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.TagClass;

import java.util.List;
import java.util.Set;

public interface PostBO {

    List<PostClass> findAll();
    List<PostClass> findAllOrderByCreatedAtDesc();
    Page<PostClass> findAllOrderByCreatedAtDescPaginated(int page, int size);
    List<PostClass> findByChannelId(Long channelId);
    Page<PostClass> findByChannelIdOrderByCreatedAtDescPaginated(Long channelId, int page, int size);
    List<PostClass> findByUserId(Long userId);
    List<PostClass> findByTagsContains(Set<TagClass> tags);
    Page<PostClass> findByTagsContainsPaginated(Set<TagClass> tags, int page, int size);
    List<PostClass> findByTitleContains(String title);
    Page<PostClass> findByTitleContainsPaginated(String title, int page, int size);
    PostClass findById(String id);
    void save(PostClass post);
    void saveAll(List<PostClass> posts);
    void saveAll(PostClass... posts);
    void deleteById(String id);
    void delete(PostClass post);
    Long count();

}
