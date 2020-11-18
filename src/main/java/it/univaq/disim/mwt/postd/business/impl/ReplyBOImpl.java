package it.univaq.disim.mwt.postd.business.impl;

import it.univaq.disim.mwt.postd.business.AjaxResponse;
import it.univaq.disim.mwt.postd.business.BusinessException;
import it.univaq.disim.mwt.postd.business.Page;
import it.univaq.disim.mwt.postd.business.ReplyBO;
import it.univaq.disim.mwt.postd.domain.*;
import it.univaq.disim.mwt.postd.repository.jpa.ChannelRepository;
import it.univaq.disim.mwt.postd.repository.mongo.NotificationRepository;
import it.univaq.disim.mwt.postd.repository.mongo.PostRepository;
import it.univaq.disim.mwt.postd.repository.mongo.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = BusinessException.class)
public class ReplyBOImpl implements ReplyBO {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public List<ReplyClass> findAll() {
        return (List<ReplyClass>) replyRepository.findAll();
    }

    @Override
    public ReplyClass findById(String id) {
        return replyRepository.findById(id).orElse(null);
    }

    @Override
    public List<ReplyClass> findByPost(PostClass post) {
        return replyRepository.findByPost(post).orElse(new ArrayList<>());
    }

    @Override
    public Page<ReplyClass> findByPostOrderByCreatedAtDescPaginated(PostClass post, int page, int size) {
        return new Page<>(replyRepository.findByPost(post, PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public List<ReplyClass> findByChannelId(Long channelId) {
        return replyRepository.findByChannelId(channelId).orElse(new ArrayList<>());
    }

    @Override
    public List<ReplyClass> findByUserId(Long userId) {
        return replyRepository.findByUserId(userId).orElse(new ArrayList<>());
    }

    @Override
    public Page<ReplyClass> findByUserDownvotedPaginated(Long userId, int page, int size) {
        return new Page<>(replyRepository.findByUsersDownvotedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<ReplyClass> findByUserUpvotedPaginated(Long userId, int page, int size) {
        return new Page<>(replyRepository.findByUsersUpvotedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public List<ReplyClass> findByUserDownvoted(Long userId) {
        return replyRepository.findByUsersDownvotedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<ReplyClass> findByUserUpvoted(Long userId) {
        return replyRepository.findByUsersUpvotedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public void save(ReplyClass reply) {
        replyRepository.save(reply);
    }

    @Override
    public void saveAll(List<ReplyClass> replies) {
        replyRepository.saveAll(replies);
    }

    @Override
    public void saveAll(ReplyClass... replies) {
        replyRepository.saveAll(Arrays.asList(replies));
    }

    @Override
    public void deleteById(String id) {
        replyRepository.deleteById(id);
    }

    @Override
    public void delete(ReplyClass reply) {
        replyRepository.delete(reply);
    }

    @Override
    public void deleteAll(List<ReplyClass> replies) {
        replyRepository.deleteAll(replies);
    }

    @Override
    public void deleteAll(ReplyClass... replies) {
        replyRepository.deleteAll(Arrays.asList(replies));
    }

    @Override
    public Long count() {
        return replyRepository.count();
    }

    @Override
    public AjaxResponse upvote(String replyId, UserClass user) throws BusinessException {
        ReplyClass reply = replyRepository.findById(replyId).orElseThrow(BusinessException::new);
        boolean upvotedAlready = false;
        boolean downvotedAlready = false;

        if(reply.getUsersUpvoted() == null){
            reply.setUsersUpvoted(new HashSet<>());
        }

        if(reply.getUsersUpvoted().contains(user.getId())) {
            upvotedAlready = true;
        }

        if(reply.getUsersDownvoted() != null && reply.getUsersDownvoted().contains(user.getId())) {
            downvotedAlready = true;
        }

        if(upvotedAlready) {
            reply.getUsersUpvoted().remove(user.getId());
            reply.setUpvote(reply.getUpvote() - 1);
            replyRepository.save(reply);
        } else if(downvotedAlready) {
            reply.getUsersDownvoted().remove(user.getId());
            reply.setDownvote(reply.getDownvote() - 1);
            reply.getUsersUpvoted().add(user.getId());
            reply.setUpvote(reply.getUpvote() + 1);
            replyRepository.save(reply);
        } else {
            reply.getUsersUpvoted().add(user.getId());
            reply.setUpvote(reply.getUpvote() + 1);
            replyRepository.save(reply);
        }

        return new AjaxResponse(reply.getUpvote() - reply.getDownvote(), upvotedAlready, downvotedAlready);
    }

    @Override
    public AjaxResponse downvote(String replyId, UserClass user) throws BusinessException {
        ReplyClass reply = replyRepository.findById(replyId).orElseThrow(BusinessException::new);
        boolean upvotedAlready = false;
        boolean downvotedAlready = false;

        if(reply.getUsersDownvoted() == null){
            reply.setUsersDownvoted(new HashSet<>());
        }

        if(reply.getUsersDownvoted().contains(user.getId())) {
            downvotedAlready = true;
        }

        if(reply.getUsersUpvoted() != null && reply.getUsersUpvoted().contains(user.getId())) {
            upvotedAlready = true;
        }

        if(downvotedAlready) {
            reply.getUsersDownvoted().remove(user.getId());
            reply.setDownvote(reply.getDownvote() - 1);
            replyRepository.save(reply);
        } else if(upvotedAlready) {
            reply.getUsersUpvoted().remove(user.getId());
            reply.setUpvote(reply.getUpvote() - 1);
            reply.getUsersDownvoted().add(user.getId());
            reply.setDownvote(reply.getDownvote() + 1);
            replyRepository.save(reply);
        } else {
            reply.getUsersDownvoted().add(user.getId());
            reply.setDownvote(reply.getDownvote() + 1);
            replyRepository.save(reply);
        }

        return new AjaxResponse(reply.getUpvote() - reply.getDownvote(), upvotedAlready, downvotedAlready);
    }

    @Override
    public void createReplyInPost(ReplyClass reply, PostClass postContainer) throws BusinessException {
        Long replyCreator = reply.getUserId();

        reply.setPost(postContainer);

        Set<ReplyClass> replies = postContainer.getReplies();

        if(replies == null || replies.isEmpty()) {
            replies = new HashSet<>();
        }

        replies.add(reply);
        postContainer.setReplies(replies);

        replyRepository.save(reply);
        postRepository.save(postContainer);

        // notifications
        // send notification to all other replies creators and to post creator
        Long postCreator = postContainer.getUserId();
        ChannelClass channel = channelRepository.findById(reply.getChannelId()).orElseThrow(BusinessException::new);
        List<Long> alreadyNotified = new ArrayList<>();

        if(!replyCreator.equals(postCreator)) {
            NotificationClass notificationToPostCreator = new NotificationClass();
            notificationToPostCreator.setUserTargetId(postCreator);
            notificationToPostCreator.setUserCreatedById(replyCreator);
            notificationToPostCreator.setChannelId(postContainer.getChannelId());
            notificationToPostCreator.setChannelName(channel.getName());
            notificationToPostCreator.setPostId(postContainer.getId());
            notificationToPostCreator.setPostTitle(postContainer.getTitle());
            notificationToPostCreator.setScope(PostClass.class.getName());
            notificationToPostCreator.setContent("New reply in your post " + notificationToPostCreator.getPostTitle());

            notificationRepository.save(notificationToPostCreator);
            alreadyNotified.add(postCreator);
        }

        if(!replies.isEmpty()) {
            for(ReplyClass replyClass : replies) {
                if(replyClass == null) {
                    continue;
                }

                if(!replyCreator.equals(replyClass.getUserId()) && (!replyCreator.equals(postCreator)) && (!alreadyNotified.contains(replyClass.getUserId()))) {
                    NotificationClass notification = new NotificationClass();
                    notification.setUserTargetId(replyClass.getUserId());
                    notification.setUserCreatedById(replyCreator);
                    notification.setChannelId(reply.getChannelId());
                    notification.setChannelName(channel.getName());
                    notification.setPostId(postContainer.getId());
                    notification.setPostTitle(postContainer.getTitle());
                    notification.setScope(PostClass.class.getName());
                    notification.setContent("New reply in post " + notification.getPostTitle());

                    notificationRepository.save(notification);
                    alreadyNotified.add(replyClass.getUserId());
                }
            }
        }
    }
}
