package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.AjaxResponse;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.business.PostBO;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.ChannelRepository;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserChannelRoleRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.NotificationRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PostBOImpl implements PostBO {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserChannelRoleRepository userChannelRoleRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public List<PostClass> findAll() {
        return (List<PostClass>) postRepository.findAll();
    }

    @Override
    public List<PostClass> findAllOrderByCreatedAtDesc() {
        return (List<PostClass>) postRepository.findAll(Sort.by("created_at").descending());
    }

    @Override
    public Page<PostClass> findAllOrderByCreatedAtDescPaginated(int page, int size) {
        return new Page<>(postRepository.findAll(PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public List<PostClass> findByChannelId(Long channelId) {
        return postRepository.findByChannelId(channelId).orElse(new ArrayList<>());
    }

    @Override
    public Page<PostClass> findByChannelIdReportedOrderByCreatedAtDescPaginated(Long channelId, int page, int size) {
        return new Page<>(postRepository.findByChannelIdAndReported(channelId, true, PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public Page<PostClass> findByChannelIdOrderByCreatedAtDescPaginated(Long channelId, int page, int size) {
        return new Page<>(postRepository.findByChannelId(channelId, PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public List<PostClass> findByUserId(Long userId) {
        return postRepository.findByUserId(userId).orElse(new ArrayList<>());
    }

    @Override
    public Page<PostClass> findByUserIdOrderByCreatedAtDescPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUserId(userId, PageRequest.of(page, size, Sort.by("created_at").descending())));
    }

    @Override
    public List<PostClass> findByTagsContains(Set<TagClass> tags) {
        return postRepository.findByTagsContains(tags).orElse(new ArrayList<>());
    }

    @Override
    public Page<PostClass> findByTagsContainsPaginated(Set<TagClass> tags, int page, int size) {
        return new Page<>(postRepository.findByTagsContains(tags, PageRequest.of(page, size)));
    }

    @Override
    public List<PostClass> findByTitleContains(String title) {
        return postRepository.findByTitleContains(title).orElse(new ArrayList<>());
    }

    @Override
    public Page<PostClass> findByTitleContainsPaginated(String title, int page, int size) {
        return new Page<>(postRepository.findByTitleContains(title, PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserDownvotedPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersDownvotedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserUpvotedPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersUpvotedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserHiddenPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersHiddenContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserReportedPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersReportedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public Page<PostClass> findByUserSavedPaginated(Long userId, int page, int size) {
        return new Page<>(postRepository.findByUsersSavedContains(new HashSet<>(Arrays.asList(userId)), PageRequest.of(page, size)));
    }

    @Override
    public List<PostClass> findByUserDownvoted(Long userId) {
        return postRepository.findByUsersDownvotedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<PostClass> findByUserUpvoted(Long userId) {
        return postRepository.findByUsersUpvotedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<PostClass> findByUserHidden(Long userId) {
        return postRepository.findByUsersHiddenContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<PostClass> findByUserReported(Long userId) {
        return postRepository.findByUsersReportedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public List<PostClass> findByUserSaved(Long userId) {
        return postRepository.findByUsersSavedContains(new HashSet<>(Arrays.asList(userId))).orElse(new ArrayList<>());
    }

    @Override
    public PostClass findById(String id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void save(PostClass post) {
        postRepository.save(post);
    }

    @Override
    public void saveAll(List<PostClass> posts) {
        postRepository.saveAll(posts);
    }

    @Override
    public void saveAll(PostClass... posts) {
        postRepository.saveAll(Arrays.asList(posts));
    }

    @Override
    public void deleteById(String id) {
        postRepository.deleteById(id);
    }

    @Override
    public void delete(PostClass post) {
        postRepository.delete(post);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }

    @Override
    public AjaxResponse upvote(String postId, UserClass user) {
        PostClass post = postRepository.findById(postId).orElse(null);
        boolean upvotedAlready = false;
        boolean downvotedAlready = false;

        if(post.getUsersUpvoted() == null){
            post.setUsersUpvoted(new HashSet<>());
        }

        if(post.getUsersUpvoted().contains(user.getId())) {
            upvotedAlready = true;
        }

        if(post.getUsersDownvoted() != null && post.getUsersDownvoted().contains(user.getId())) {
            downvotedAlready = true;
        }

        if(upvotedAlready) {
            post.getUsersUpvoted().remove(user.getId());
            post.setUpvote(post.getUpvote() - 1);
            postRepository.save(post);
        } else if(downvotedAlready) {
            post.getUsersDownvoted().remove(user.getId());
            post.setDownvote(post.getDownvote() - 1);
            post.getUsersUpvoted().add(user.getId());
            post.setUpvote(post.getUpvote() + 1);
            postRepository.save(post);
        } else {
            post.getUsersUpvoted().add(user.getId());
            post.setUpvote(post.getUpvote() + 1);
            postRepository.save(post);
        }

        return new AjaxResponse(post.getUpvote() - post.getDownvote(), upvotedAlready, downvotedAlready);
    }

    @Override
    public AjaxResponse downvote(String postId, UserClass user) {
        PostClass post = postRepository.findById(postId).orElse(null);
        boolean upvotedAlready = false;
        boolean downvotedAlready = false;

        if(post.getUsersDownvoted() == null){
            post.setUsersDownvoted(new HashSet<>());
        }

        if(post.getUsersDownvoted().contains(user.getId())) {
            downvotedAlready = true;
        }

        if(post.getUsersUpvoted() != null && post.getUsersUpvoted().contains(user.getId())) {
            upvotedAlready = true;
        }

        if(downvotedAlready) {
            post.getUsersDownvoted().remove(user.getId());
            post.setDownvote(post.getDownvote() - 1);
            postRepository.save(post);
        } else if(upvotedAlready) {
            post.getUsersUpvoted().remove(user.getId());
            post.setUpvote(post.getUpvote() - 1);
            post.getUsersDownvoted().add(user.getId());
            post.setDownvote(post.getDownvote() + 1);
            postRepository.save(post);
        } else {
            post.getUsersDownvoted().add(user.getId());
            post.setDownvote(post.getDownvote() + 1);
            postRepository.save(post);
        }

        return new AjaxResponse(post.getUpvote() - post.getDownvote(), upvotedAlready, downvotedAlready);
    }

    @Override
    public void hideToggle(String postId, UserClass user) {
        PostClass post = postRepository.findById(postId).orElse(null);
        if(post.getUsersHidden() == null){
            post.setUsersHidden(new HashSet<>());
        }

        if(post.getUsersHidden().contains(user.getId())) {
            post.getUsersHidden().remove(user.getId());
        } else {
            post.getUsersHidden().add(user.getId());

        }
        postRepository.save(post);
    }

    @Override
    public void saveToggle(String postId, UserClass user) {
        PostClass post = postRepository.findById(postId).orElse(null);
        if(post.getUsersSaved() == null){
            post.setUsersSaved(new HashSet<>());
        }

        if(post.getUsersSaved().contains(user.getId())) {
            post.getUsersSaved().remove(user.getId());
        } else {
            post.getUsersSaved().add(user.getId());

        }
        postRepository.save(post);
    }

    @Override
    public void reportToggle(String postId, UserClass user) {
        PostClass post = postRepository.findById(postId).orElse(null);
        if(post.getUsersReported() == null){
            post.setUsersReported(new HashSet<>());
        }

        if(post.getUsersReported().contains(user.getId())) {
            post.getUsersReported().remove(user.getId());
        } else {
            post.getUsersReported().add(user.getId());
        }

        if(post.getUsersReported().isEmpty()) {
            post.setReported(false);
        } else {
            post.setReported(true);
        }

        postRepository.save(post);
    }

    @Override
    public void createPostInChannel(PostClass post) {
        postRepository.save(post);

        // notifications
        Long channelId = post.getChannelId();
        Long creatorId = post.getUserId();

        ChannelClass channel = channelRepository.findById(channelId).orElse(null);

        List<UserChannelRole> usersInChannel = userChannelRoleRepository.findByChannelId(channelId).orElse(null);

        if(usersInChannel != null && !(usersInChannel.isEmpty())) {
            // send notification to all members, creator excluded
            for(UserChannelRole member : usersInChannel) {
                if(!creatorId.equals(member.getUser().getId())) {
                    NotificationClass notification = new NotificationClass();
                    notification.setUserTargetId(member.getUser().getId());
                    notification.setUserCreatedById(creatorId);
                    notification.setChannelId(channelId);
                    notification.setChannelName(channel.getName());
                    notification.setPostId(post.getId());
                    notification.setPostTitle(post.getTitle());
                    notification.setScope(ChannelClass.class.getName());
                    notification.setContent("New post in channel " + notification.getChannelName());

                    notificationRepository.save(notification);
                }
            }
        }
    }
}
