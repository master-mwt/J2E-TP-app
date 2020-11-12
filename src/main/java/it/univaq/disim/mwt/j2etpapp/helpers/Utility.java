package it.univaq.disim.mwt.j2etpapp.helpers;

import it.univaq.disim.mwt.j2etpapp.business.*;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

// TODO: will this class be inserted in all templates ? (decision)
@Component
public class Utility {

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
        return channelBO.findById(channelId);
    }

    public UserClass findUserById(Long userId) {
        return userBO.findById(userId);
    }

    public boolean isUserChannelMember(Long channelId, Long userId) {
        return (userChannelRoleBO.findByChannelIdAndUserId(channelId, userId) != null);
    }

    public String findUserChannelRoleName(Long channelId, Long userId) {
        return roleBO.findById(userChannelRoleBO.findByChannelIdAndUserId(channelId, userId).getRole().getId()).getName();
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
        return postBO.getPostImages(postId);
    }
}
