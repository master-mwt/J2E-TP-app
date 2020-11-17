package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;

import java.util.List;

public interface ReplyBO {

    List<ReplyClass> findAll();
    ReplyClass findById(String id);
    List<ReplyClass> findByPost(PostClass post);
    Page<ReplyClass> findByPostOrderByCreatedAtDescPaginated(PostClass post, int page, int size);
    List<ReplyClass> findByChannelId(Long channelId);
    List<ReplyClass> findByUserId(Long userId);
    Page<ReplyClass> findByUserDownvotedPaginated(Long userId, int page, int size);
    Page<ReplyClass> findByUserUpvotedPaginated(Long userId, int page, int size);
    List<ReplyClass> findByUserDownvoted(Long userId);
    List<ReplyClass> findByUserUpvoted(Long userId);
    void save(ReplyClass reply);
    void saveAll(List<ReplyClass> replies);
    void saveAll(ReplyClass... replies);
    void deleteById(String id);
    void delete(ReplyClass reply);
    void deleteAll(List<ReplyClass> replies);
    void deleteAll(ReplyClass... replies);
    Long count();

    AjaxResponse upvote(String replyId, UserClass user) throws BusinessException;
    AjaxResponse downvote(String replyId, UserClass user) throws BusinessException;
    void createReplyInPost(ReplyClass reply, PostClass postContainer) throws BusinessException;
}
