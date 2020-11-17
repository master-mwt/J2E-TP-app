package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ImageClass;
import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.TagClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface PostBO {

    List<PostClass> findAll();
    List<PostClass> findAllOrderByCreatedAtDesc();
    Page<PostClass> findAllOrderByCreatedAtDescPaginated(int page, int size);
    List<PostClass> findByChannelId(Long channelId);
    Page<PostClass> findByChannelIdReportedOrderByCreatedAtDescPaginated(Long channelId, int page, int size);
    Page<PostClass> findByChannelIdOrderByCreatedAtDescPaginated(Long channelId, int page, int size);
    List<PostClass> findByUserId(Long userId);
    Page<PostClass> findByUserIdOrderByCreatedAtDescPaginated(Long userId, int page, int size);
    List<PostClass> findByTagsContains(Set<TagClass> tags);
    Page<PostClass> findByTagsContainsPaginated(Set<TagClass> tags, int page, int size);
    List<PostClass> findByTitleContains(String title);
    Page<PostClass> findByTitleContainsPaginated(String title, int page, int size);
    Page<PostClass> findByUserDownvotedPaginated(Long userId, int page, int size);
    Page<PostClass> findByUserUpvotedPaginated(Long userId, int page, int size);
    Page<PostClass> findByUserHiddenPaginated(Long userId, int page, int size);
    Page<PostClass> findByUserReportedPaginated(Long userId, int page, int size);
    Page<PostClass> findByUserSavedPaginated(Long userId, int page, int size);
    List<PostClass> findByUserDownvoted(Long userId);
    List<PostClass> findByUserUpvoted(Long userId);
    List<PostClass> findByUserHidden(Long userId);
    List<PostClass> findByUserReported(Long userId);
    List<PostClass> findByUserSaved(Long userId);
    PostClass findById(String id) throws BusinessException;
    void save(PostClass post);
    void saveAll(List<PostClass> posts);
    void saveAll(PostClass... posts);
    void deleteById(String id) throws BusinessException;
    void delete(PostClass post);
    Long count();

    AjaxResponse upvote(String postId, UserClass user) throws BusinessException;
    AjaxResponse downvote(String postId, UserClass user) throws BusinessException;
    void hideToggle(String postId, UserClass user) throws BusinessException;
    void saveToggle(String postId, UserClass user) throws BusinessException;
    void reportToggle(String postId, UserClass user) throws BusinessException;
    void createPostInChannel(PostClass post, String tagListString) throws BusinessException;
    void createPostInChannel(PostClass post, String tagListString, MultipartFile[] images) throws BusinessException;
    List<ImageClass> getPostImages(String postId) throws BusinessException;
}
