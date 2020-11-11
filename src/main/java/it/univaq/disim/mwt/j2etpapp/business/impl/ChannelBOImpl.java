package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.configuration.ApplicationProperties;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.*;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.ReplyRepository;
import it.univaq.disim.mwt.j2etpapp.utils.FileDealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ChannelBOImpl implements ChannelBO {

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserChannelRoleRepository userChannelRoleRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private FileDealer fileDealer;
    @Autowired
    private ApplicationProperties properties;

    @Override
    public List<ChannelClass> findAll() {
        return (List<ChannelClass>) channelRepository.findAll();
    }

    @Override
    public ChannelClass findById(Long id) {
        return channelRepository.findById(id).orElse(null);
    }

    @Override
    public ChannelClass findByName(String name) {
        return channelRepository.findByName(name).orElse(null);
    }

    @Override
    public List<ChannelClass> findByNameContains(String name) {
        return channelRepository.findByNameContains(name).orElse(new ArrayList<>());
    }

    @Override
    public Page<ChannelClass> findByNameContainsPaginated(String name, int page, int size) {
        return new Page<>(channelRepository.findByNameContains(name, PageRequest.of(page, size)));
    }

    @Override
    public void save(ChannelClass channel) {
        channelRepository.save(channel);
    }

    @Override
    public void saveAll(List<ChannelClass> channels) {
        channelRepository.saveAll(channels);
    }

    @Override
    public void saveAll(ChannelClass... channels) {
        channelRepository.saveAll(Arrays.asList(channels));
    }

    @Override
    public void deleteById(Long id) {
        channelRepository.deleteById(id);

        List<PostClass> posts = postRepository.findByChannelId(id).orElse(null);
        List<ReplyClass> replies = replyRepository.findByChannelId(id).orElse(null);

        if(posts != null && !posts.isEmpty()) {
            postRepository.deleteAll(posts);
        }

        if(replies != null && !replies.isEmpty()) {
            replyRepository.deleteAll(replies);
        }
    }

    @Override
    public void delete(ChannelClass channel) {
        channelRepository.delete(channel);

        List<PostClass> posts = postRepository.findByChannelId(channel.getId()).orElse(null);
        List<ReplyClass> replies = replyRepository.findByChannelId(channel.getId()).orElse(null);

        if(posts != null && !posts.isEmpty()) {
            postRepository.deleteAll(posts);
        }

        if(replies != null && !replies.isEmpty()) {
            replyRepository.deleteAll(replies);
        }
    }

    @Override
    public Long count() {
        return channelRepository.count();
    }

    @Override
    public Set<UserClass> getSoftBannedUsers(long channelId) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        return channel.getSoftBannedUsers();
    }

    @Override
    public void setSoftBannedUsers(long channelId, Set<UserClass> softBannedUsers) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        channel.setSoftBannedUsers(softBannedUsers);
        channelRepository.save(channel);

        for(UserClass softBannedUser : softBannedUsers) {
            UserChannelRole userToBeSoftbanned = userChannelRoleRepository.findByChannelIdAndUserId(channelId, softBannedUser.getId()).orElse(null);
            userChannelRoleRepository.delete(userToBeSoftbanned);
        }
    }

    @Override
    public void appendSoftBannedUsers(long channelId, Set<UserClass> softBannedUsers) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        Set<UserClass> alreadySoftBanned = channel.getSoftBannedUsers();
        if (alreadySoftBanned == null) {
            alreadySoftBanned = new HashSet<>();
        }
        alreadySoftBanned.addAll(softBannedUsers);
        channel.setSoftBannedUsers(alreadySoftBanned);
        channelRepository.save(channel);

        for(UserClass softBannedUser : softBannedUsers) {
            UserChannelRole userToBeSoftbanned = userChannelRoleRepository.findByChannelIdAndUserId(channelId, softBannedUser.getId()).orElse(null);
            userChannelRoleRepository.delete(userToBeSoftbanned);
        }
    }

    @Override
    public Set<UserClass> getReportedUsers(long channelId) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        return channel.getReportedUsers();
    }

    @Override
    public void setReportedUsers(long channelId, Set<UserClass> reportedUsers) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        channel.setReportedUsers(reportedUsers);
        channelRepository.save(channel);
    }

    @Override
    public void appendReportedUsers(long channelId, Set<UserClass> reportedUsers) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        Set<UserClass> alreadyReported = channel.getReportedUsers();
        if (alreadyReported == null) {
            alreadyReported = new HashSet<>();
        }
        alreadyReported.addAll(reportedUsers);
        channel.setReportedUsers(alreadyReported);
        channelRepository.save(channel);
    }

    @Override
    public void joinChannel(long channelId, UserClass user) {
        RoleClass member = roleRepository.findByName("member").orElse(null);

        UserChannelRole joinedMember = new UserChannelRole();
        UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
        userChannelRoleFKs.setUserId(user.getId());
        userChannelRoleFKs.setChannelId(channelId);
        userChannelRoleFKs.setRoleId(member.getId());

        joinedMember.setUserChannelRoleFKs(userChannelRoleFKs);

        userChannelRoleRepository.save(joinedMember);
    }

    @Override
    public void leaveChannel(long channelId, UserClass user) {
        UserChannelRole memberToDelete = userChannelRoleRepository.findByChannelIdAndUserId(channelId, user.getId()).orElse(null);
        userChannelRoleRepository.delete(memberToDelete);
    }

    @Override
    public void globalUnreportPost(long channelId, String postId) throws BusinessException {
        PostClass post = postRepository.findById(postId).orElse(null);
        if(!post.getChannelId().equals(channelId)){
            throw new BusinessException();
        }
        post.getUsersReported().clear();
        post.setReported(false);
        postRepository.save(post);
    }

    @Override
    public void reportUser(long channelId, long userId) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        if(channel.getReportedUsers() == null){
            channel.setReportedUsers(new HashSet<>());
        }
        channel.getReportedUsers().add(userRepository.findById(userId).orElse(null));
        channelRepository.save(channel);
    }

    @Override
    public void unReportUser(long channelId, long userId) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        if(channel.getReportedUsers() == null){
            channel.setReportedUsers(new HashSet<>());
        }
        channel.getReportedUsers().remove(userRepository.findById(userId).orElse(null));
        channelRepository.save(channel);
    }

    @Override
    public void softBan(long channelId, long userId) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        if(channel.getSoftBannedUsers() == null){
            channel.setSoftBannedUsers(new HashSet<>());
        }
        channel.getSoftBannedUsers().add(userRepository.findById(userId).orElse(null));
        channelRepository.save(channel);

        UserChannelRole userToBeDeleted = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        userChannelRoleRepository.delete(userToBeDeleted);
    }

    @Override
    public void unSoftBan(long channelId, long userId) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        if(channel.getSoftBannedUsers() == null){
            channel.setSoftBannedUsers(new HashSet<>());
        }
        channel.getSoftBannedUsers().remove(userRepository.findById(userId).orElse(null));
        channelRepository.save(channel);
    }

    @Override
    public void upgradeMemberToModerator(long channelId, long userId) {
        RoleClass member = roleRepository.findByName("member").orElse(null);
        RoleClass moderator = roleRepository.findByName("moderator").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(member.equals(currentMember.getRole())){
            UserChannelRole newRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setUserId(userId);
            userChannelRoleFKs.setRoleId(moderator.getId());
            newRole.setUserChannelRoleFKs(userChannelRoleFKs);

            userChannelRoleRepository.delete(currentMember);
            userChannelRoleRepository.save(newRole);
        }
    }

    @Override
    public void upgradeModeratorToAdmin(long channelId, long userId) {
        RoleClass moderator = roleRepository.findByName("moderator").orElse(null);
        RoleClass admin = roleRepository.findByName("admin").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(moderator.equals(currentMember.getRole())){
            UserChannelRole newRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setUserId(userId);
            userChannelRoleFKs.setRoleId(admin.getId());
            newRole.setUserChannelRoleFKs(userChannelRoleFKs);

            userChannelRoleRepository.delete(currentMember);
            userChannelRoleRepository.save(newRole);
        }
    }

    @Override
    public void downgradeModeratorToMember(long channelId, long userId) {
        RoleClass member = roleRepository.findByName("member").orElse(null);
        RoleClass moderator = roleRepository.findByName("moderator").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(moderator.equals(currentMember.getRole())){
            UserChannelRole newRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setUserId(userId);
            userChannelRoleFKs.setRoleId(member.getId());
            newRole.setUserChannelRoleFKs(userChannelRoleFKs);

            userChannelRoleRepository.delete(currentMember);
            userChannelRoleRepository.save(newRole);
        }
    }

    @Override
    public void upgradeAdminToCreator(long channelId, long userId) throws BusinessException {
        RoleClass admin = roleRepository.findByName("admin").orElse(null);
        RoleClass creator = roleRepository.findByName("creator").orElse(null);
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);

        for(UserChannelRole userChannelRole : userChannelRoleRepository.findByChannelId(channelId).orElse(null)) {
            if(creator.equals(userChannelRole.getRole())) {
                throw new BusinessException();
            }
        }

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(admin.equals(currentMember.getRole())){
            UserChannelRole newRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setUserId(userId);
            userChannelRoleFKs.setRoleId(creator.getId());
            newRole.setUserChannelRoleFKs(userChannelRoleFKs);
            userChannelRoleRepository.delete(currentMember);
            userChannelRoleRepository.save(newRole);

            channel.setCreator(userRepository.findById(userId).orElse(null));
            channelRepository.save(channel);
        }
    }

    @Override
    public void downgradeAdminToModerator(long channelId, long userId) {
        RoleClass moderator = roleRepository.findByName("moderator").orElse(null);
        RoleClass admin = roleRepository.findByName("admin").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(admin.equals(currentMember.getRole())){
            UserChannelRole newRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setUserId(userId);
            userChannelRoleFKs.setRoleId(moderator.getId());
            newRole.setUserChannelRoleFKs(userChannelRoleFKs);
            userChannelRoleRepository.delete(currentMember);
            userChannelRoleRepository.save(newRole);
        }
    }

    @Override
    public void downgradeCreatorToAdmin(long channelId, long userId) {
        RoleClass admin = roleRepository.findByName("admin").orElse(null);
        RoleClass creator = roleRepository.findByName("creator").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(creator.equals(currentMember.getRole())){
            UserChannelRole newRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setUserId(userId);
            userChannelRoleFKs.setRoleId(admin.getId());
            newRole.setUserChannelRoleFKs(userChannelRoleFKs);
            userChannelRoleRepository.delete(currentMember);
            userChannelRoleRepository.save(newRole);
        }
    }

    @Override
    public void removeImage(long channelId) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        channel.setImage(null);

        channelRepository.save(channel);
    }

    @Override
    public void saveImage(long channelId, MultipartFile image) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        try {
            String filename = fileDealer.uploadFile(image);
            ImageClass imageClass = new ImageClass();
            imageClass.setLocation(properties.getImagesStoragePathRelative() + filename);
            imageClass.setType(image.getContentType());

            BufferedImage bimg = ImageIO.read(new File(properties.getImagesStoragePathAbsolute() + filename));
            imageClass.setSize(bimg.getWidth() + "x" + bimg.getHeight());

            imageRepository.save(imageClass);
            channel.setImage(imageClass);
            channelRepository.save(channel);

        } catch (IOException e) {
            throw new BusinessException("saveImage", e);
        }
    }

    @Override
    public void createChannel(ChannelClass channel, UserClass creator) {
        RoleClass creatorRole = roleRepository.findByName("creator").orElse(null);

        channel.setCreator(creator);
        channelRepository.save(channel);

        UserChannelRole userChannelRole = new UserChannelRole();
        UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();

        userChannelRoleFKs.setChannelId(channel.getId());
        userChannelRoleFKs.setUserId(creator.getId());
        userChannelRoleFKs.setRoleId(creatorRole.getId());

        userChannelRole.setUserChannelRoleFKs(userChannelRoleFKs);

        userChannelRoleRepository.save(userChannelRole);
    }
}
