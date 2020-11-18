package it.univaq.disim.mwt.postd.helpers;

import it.univaq.disim.mwt.postd.business.*;
import it.univaq.disim.mwt.postd.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TemplateHelper {

    @Autowired
    private PostBO postBO;
    @Autowired
    private ChannelBO channelBO;
    @Autowired
    private UserBO userBO;
    @Autowired
    private UserChannelRoleBO userChannelRoleBO;
    @Autowired
    private RoleBO roleBO;
    @Autowired
    private ReplyBO replyBO;
    @Autowired
    private ImageBO imageBO;


    public boolean isPostUpvotedByUser(Long userId, PostClass post) {
        return postBO.findByUserUpvoted(userId).contains(post);
    }

    public boolean isPostDownvotedByUser(Long userId, PostClass post) {
        return postBO.findByUserDownvoted(userId).contains(post);
    }

    public boolean isReplyUpvotedByUser(Long userId, ReplyClass reply) {
        return replyBO.findByUserUpvoted(userId).contains(reply);
    }

    public boolean isReplyDownvotedByUser(Long userId, ReplyClass reply) {
        return replyBO.findByUserDownvoted(userId).contains(reply);
    }

    public boolean isPostSavedByUser(Long userId, PostClass post) {
        return postBO.findByUserSaved(userId).contains(post);
    }

    public boolean isPostHiddenByUser(Long userId, PostClass post) {
        return postBO.findByUserHidden(userId).contains(post);
    }

    public boolean isPostReportedByUser(Long userId, PostClass post) {
        return postBO.findByUserReported(userId).contains(post);
    }

    public ChannelClass findChannelById(Long channelId) {
        ChannelClass channel = null;
        try {
            channel = channelBO.findById(channelId);
        } catch (BusinessException e) {
            log.info("findChannelById: Error in finding channel with id " + channelId);
        }
        return channel;
    }

    public UserClass findUserById(Long userId) {
        UserClass user = null;
        try {
            user = userBO.findById(userId);
        } catch (BusinessException e) {
            log.info("findUserById: Error in finding user with id " + userId);
        }
        return user;
    }

    public boolean isUserChannelMember(Long channelId, Long userId) {
        return (userChannelRoleBO.findByChannelIdAndUserId(channelId, userId) != null);
    }

    public String findUserChannelRoleName(Long channelId, Long userId) {
        UserChannelRole userChannelRole = userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);
        if(userChannelRole == null) {
            return null;
        }
        return roleBO.findById(userChannelRole.getRole().getId()).getName();
    }

    public UserChannelRole findMemberUserChannelRole(Long channelId, Long userId) {
        return userChannelRoleBO.findByChannelIdAndUserId(channelId, userId);
    }

    public boolean hasUserProfileImage(Long userId) {
        return (imageBO.findUserProfileImage(userId) == null);
    }

    public ImageClass findUserProfileImage(Long userId) {
        return imageBO.findUserProfileImage(userId);
    }

    public boolean hasChannelImage(Long channelId) {
        return (imageBO.findChannelImage(channelId) == null);
    }

    public ImageClass findChannelImage(Long channelId) {
        return imageBO.findChannelImage(channelId);
    }

    public List<ImageClass> findPostImages(String postId) {
        List<ImageClass> images = null;
        try {
            images = postBO.getPostImages(postId);
        } catch (BusinessException e) {
            log.info("findPostImages: Error in finding images of post " + postId);
        }
        return images;
    }

    public RoleClass findRoleById(Long roleId) {
        return roleBO.findById(roleId);
    }
}
