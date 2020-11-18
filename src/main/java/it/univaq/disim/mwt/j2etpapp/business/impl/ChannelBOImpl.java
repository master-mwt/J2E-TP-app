package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.FileTypeException;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.*;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.ReplyRepository;
import it.univaq.disim.mwt.j2etpapp.utils.FileDealer;
import it.univaq.disim.mwt.j2etpapp.utils.UtilsClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Transactional(rollbackFor = BusinessException.class)
@Slf4j
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
    private UtilsClass utilsClass;

    @Override
    public List<ChannelClass> findAll() {
        return (List<ChannelClass>) channelRepository.findAll();
    }

    @Override
    public ChannelClass findById(Long id) throws BusinessException {
        ChannelClass channel = channelRepository.findById(id).orElse(null);

        if(channel == null) {
            log.info("findById: Error finding channel with id " + id);
            throw new BusinessException("Channel with id " + id + " not found");
        }

        return channel;
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
    public void deleteById(Long id) throws BusinessException {
        ChannelClass channel = channelRepository.findById(id).orElseThrow(BusinessException::new);

        String imageLocation = getChannelImageLocation(channel);

        if(imageLocation != null) {
            fileDealer.removeFile(imageLocation);
        }

        deleteChannelPostsAndReplies(id);

        channelRepository.deleteById(id);

        log.info("Deleted channel with id " + id);
    }

    @Override
    public void delete(ChannelClass channel) {
        Long channelId = channel.getId();

        String imageLocation = getChannelImageLocation(channel);

        if(imageLocation != null) {
            fileDealer.removeFile(imageLocation);
        }

        deleteChannelPostsAndReplies(channelId);

        channelRepository.delete(channel);

        log.info("Deleted channel with id " + channelId);
    }

    @Override
    public Long count() {
        return channelRepository.count();
    }

    @Override
    public Set<UserClass> getSoftBannedUsers(Long channelId) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        return channel.getSoftBannedUsers();
    }

    @Override
    public void setSoftBannedUsers(Long channelId, Set<UserClass> softBannedUsers) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        channel.setSoftBannedUsers(softBannedUsers);
        channelRepository.save(channel);

        for(UserClass softBannedUser : softBannedUsers) {
            UserChannelRole userToBeSoftBanned = userChannelRoleRepository.findByChannelIdAndUserId(channelId, softBannedUser.getId()).orElse(null);
            if(userToBeSoftBanned != null) {
                userChannelRoleRepository.delete(userToBeSoftBanned);
            }
        }
    }

    @Override
    public void appendSoftBannedUsers(Long channelId, Set<UserClass> softBannedUsers) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        Set<UserClass> alreadySoftBanned = channel.getSoftBannedUsers();
        if (alreadySoftBanned == null) {
            alreadySoftBanned = new HashSet<>();
        }
        alreadySoftBanned.addAll(softBannedUsers);
        channel.setSoftBannedUsers(alreadySoftBanned);
        channelRepository.save(channel);

        for(UserClass softBannedUser : softBannedUsers) {
            UserChannelRole userToBeSoftBanned = userChannelRoleRepository.findByChannelIdAndUserId(channelId, softBannedUser.getId()).orElse(null);
            if(userToBeSoftBanned != null) {
                userChannelRoleRepository.delete(userToBeSoftBanned);
            }
        }
    }

    @Override
    public Set<UserClass> getReportedUsers(Long channelId) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        return channel.getReportedUsers();
    }

    @Override
    public void setReportedUsers(Long channelId, Set<UserClass> reportedUsers) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        channel.setReportedUsers(reportedUsers);
        channelRepository.save(channel);
    }

    @Override
    public void appendReportedUsers(Long channelId, Set<UserClass> reportedUsers) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        Set<UserClass> alreadyReported = channel.getReportedUsers();
        if (alreadyReported == null) {
            alreadyReported = new HashSet<>();
        }
        alreadyReported.addAll(reportedUsers);
        channel.setReportedUsers(alreadyReported);
        channelRepository.save(channel);
    }

    @Override
    public void joinChannel(Long channelId, UserClass user) throws BusinessException {
        RoleClass member = roleRepository.findByName("member").orElseThrow(BusinessException::new);

        UserChannelRole joinedMember = new UserChannelRole();
        UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
        userChannelRoleFKs.setUserId(user.getId());
        userChannelRoleFKs.setChannelId(channelId);
        userChannelRoleFKs.setRoleId(member.getId());

        joinedMember.setUserChannelRoleFKs(userChannelRoleFKs);

        userChannelRoleRepository.save(joinedMember);
    }

    @Override
    public void leaveChannel(Long channelId, UserClass user) throws BusinessException {
        RoleClass creator = roleRepository.findByName("creator").orElseThrow(BusinessException::new);

        UserChannelRole memberToDelete = userChannelRoleRepository.findByChannelIdAndUserId(channelId, user.getId()).orElseThrow(BusinessException::new);

        if(creator.equals(memberToDelete.getRole())) {
            log.info("leaveChannel: Error leaving channel user with id " + user.getId() + " in channel with id " + channelId);
            throw new BusinessException("You cannot leave the channel, you are the creator");
        }

        userChannelRoleRepository.delete(memberToDelete);
    }

    @Override
    public void globalUnreportPost(Long channelId, String postId) throws BusinessException {
        PostClass post = postRepository.findById(postId).orElseThrow(BusinessException::new);
        if(!post.getChannelId().equals(channelId)){
            log.info("globalUnreportPost: Error global unreport post with id " + postId + " in channel with id " + channelId);
            throw new BusinessException("Global unreport post error");
        }
        post.getUsersReported().clear();
        post.setReported(false);
        postRepository.save(post);
    }

    @Override
    public void reportUser(Long channelId, Long userId, UserClass principal) throws BusinessException {
        if(userId.equals(principal.getId())) {
            log.info("reportUser: Error in reporting user with id " + userId + " in channel with id " + channelId);
            throw new BusinessException("You cannot report yourself");
        }

        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        if(channel.getReportedUsers() == null){
            channel.setReportedUsers(new HashSet<>());
        }
        channel.getReportedUsers().add(userRepository.findById(userId).orElseThrow(BusinessException::new));
        channelRepository.save(channel);
    }

    @Override
    public void unReportUser(Long channelId, Long userId, UserClass principal) throws BusinessException {
        if(userId.equals(principal.getId())) {
            log.info("unReportUser: Error in unreporting user with id " + userId + " in channel with id " + channelId);
            throw new BusinessException("You cannot unreport yourself");
        }

        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        if(channel.getReportedUsers() == null){
            channel.setReportedUsers(new HashSet<>());
        }
        channel.getReportedUsers().remove(userRepository.findById(userId).orElseThrow(BusinessException::new));
        channelRepository.save(channel);
    }

    @Override
    public void softBan(Long channelId, Long userId, UserClass principal) throws BusinessException {
        if(userId.equals(principal.getId())) {
            log.info("softBan: Error in softbanning user with id " + userId + " in channel with id " + channelId);
            throw new BusinessException("You cannot softban yourself");
        }

        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        if(channel.getSoftBannedUsers() == null){
            channel.setSoftBannedUsers(new HashSet<>());
        }
        channel.getSoftBannedUsers().add(userRepository.findById(userId).orElseThrow(BusinessException::new));
        channelRepository.save(channel);

        UserChannelRole userToBeDeleted = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElseThrow(BusinessException::new);
        userChannelRoleRepository.delete(userToBeDeleted);
    }

    @Override
    public void unSoftBan(Long channelId, Long userId, UserClass principal) throws BusinessException {
        if(userId.equals(principal.getId())) {
            log.info("unSoftBan: Error in unsoftbanning user with id " + userId + " in channel with id " + channelId);
            throw new BusinessException("You cannot unsoftban yourself");
        }

        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        if(channel.getSoftBannedUsers() == null){
            channel.setSoftBannedUsers(new HashSet<>());
        }
        channel.getSoftBannedUsers().remove(userRepository.findById(userId).orElseThrow(BusinessException::new));
        channelRepository.save(channel);
    }

    @Override
    public void upgradeMemberToModerator(Long channelId, Long userId) throws BusinessException {
        RoleClass member = roleRepository.findByName("member").orElseThrow(BusinessException::new);
        RoleClass moderator = roleRepository.findByName("moderator").orElseThrow(BusinessException::new);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElseThrow(BusinessException::new);
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
    public void upgradeModeratorToAdmin(Long channelId, Long userId) throws BusinessException {
        RoleClass moderator = roleRepository.findByName("moderator").orElseThrow(BusinessException::new);
        RoleClass admin = roleRepository.findByName("admin").orElseThrow(BusinessException::new);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElseThrow(BusinessException::new);
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
    public void downgradeModeratorToMember(Long channelId, Long userId) throws BusinessException {
        RoleClass member = roleRepository.findByName("member").orElseThrow(BusinessException::new);
        RoleClass moderator = roleRepository.findByName("moderator").orElseThrow(BusinessException::new);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElseThrow(BusinessException::new);
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
    public void upgradeAdminToCreator(Long channelId, Long userId) throws BusinessException {
        RoleClass admin = roleRepository.findByName("admin").orElseThrow(BusinessException::new);
        RoleClass creator = roleRepository.findByName("creator").orElseThrow(BusinessException::new);
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);

        for(UserChannelRole userChannelRole : userChannelRoleRepository.findByChannelId(channelId).orElseThrow(BusinessException::new)) {
            if(creator.equals(userChannelRole.getRole())) {
                log.info("upgradeAdminToCreator: Upgrade admin with id " + userId +  " to creator rejected, creator already present in channel with id " + channelId);
                throw new BusinessException("A creator is already present");
            }
        }

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElseThrow(BusinessException::new);
        if(admin.equals(currentMember.getRole())){
            UserChannelRole newRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setUserId(userId);
            userChannelRoleFKs.setRoleId(creator.getId());
            newRole.setUserChannelRoleFKs(userChannelRoleFKs);
            userChannelRoleRepository.delete(currentMember);
            userChannelRoleRepository.save(newRole);

            channel.setCreator(userRepository.findById(userId).orElseThrow(BusinessException::new));
            channelRepository.save(channel);
            log.info("Upgraded admin to creator user with id " + userId + " in channel with id " + channelId);
        }
    }

    @Override
    public void downgradeAdminToModerator(Long channelId, Long userId) throws BusinessException {
        RoleClass moderator = roleRepository.findByName("moderator").orElseThrow(BusinessException::new);
        RoleClass admin = roleRepository.findByName("admin").orElseThrow(BusinessException::new);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElseThrow(BusinessException::new);
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
    public void downgradeCreatorToAdmin(Long channelId, Long userId) throws BusinessException {
        RoleClass admin = roleRepository.findByName("admin").orElseThrow(BusinessException::new);
        RoleClass creator = roleRepository.findByName("creator").orElseThrow(BusinessException::new);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElseThrow(BusinessException::new);
        if(creator.equals(currentMember.getRole())){
            UserChannelRole newRole = new UserChannelRole();
            UserChannelRoleFKs userChannelRoleFKs = new UserChannelRoleFKs();
            userChannelRoleFKs.setChannelId(channelId);
            userChannelRoleFKs.setUserId(userId);
            userChannelRoleFKs.setRoleId(admin.getId());
            newRole.setUserChannelRoleFKs(userChannelRoleFKs);
            userChannelRoleRepository.delete(currentMember);
            userChannelRoleRepository.save(newRole);
            log.info("Downgraded creator to admin user with id " + userId + " in channel with id " + channelId);
        }
    }

    @Override
    public void removeImage(Long channelId) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        if(channel.getImage() != null) {
            imageRepository.delete(channel.getImage());
            fileDealer.removeFile(channel.getImage().getLocation());
        }
        channel.setImage(null);

        channelRepository.save(channel);
    }

    @Override
    public String saveImage(Long channelId, MultipartFile image) throws BusinessException {
        ChannelClass channel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        String path = null;

        try {
            if(utilsClass.checkContentTypeValidity(image.getContentType())) {
                path = fileDealer.uploadFile(image);
                ImageClass imageClass = utilsClass.fillImageData(path, image.getContentType());

                imageRepository.save(imageClass);
                channel.setImage(imageClass);
                channelRepository.save(channel);
            } else {
                throw new FileTypeException("The uploaded file is not an image");
            }
        } catch (IOException | FileTypeException e) {
            log.info("saveImage: Error in saving image in channel id " + channelId);
            throw new BusinessException("Error in saving image", e);
        }

        return path;
    }

    @Override
    public void createChannel(ChannelClass channel, UserClass creator) throws BusinessException {
        RoleClass creatorRole = roleRepository.findByName("creator").orElseThrow(BusinessException::new);

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

    @Override
    public void updateChannel(Long channelId, ChannelClass newData) throws BusinessException {
        ChannelClass savedChannel = channelRepository.findById(channelId).orElseThrow(BusinessException::new);
        savedChannel.setTitle(newData.getTitle());
        savedChannel.setDescription(newData.getDescription());
        savedChannel.setRules(newData.getRules());

        channelRepository.save(savedChannel);
    }


    private String getChannelImageLocation(ChannelClass channel) {
        ImageClass channelImage = channel.getImage();
        String imageLocation = null;

        if(channelImage != null) {
            imageLocation = channelImage.getLocation();
        }
        return imageLocation;
    }

    private void deleteChannelPostsAndReplies(Long channelId) {
        List<PostClass> posts = postRepository.findByChannelId(channelId).orElse(null);
        List<ReplyClass> replies = replyRepository.findByChannelId(channelId).orElse(null);

        if(posts != null && !posts.isEmpty()) {
            for(PostClass post : posts) {
                Set<Long> imagesId = post.getImages();
                if(imagesId != null && !imagesId.isEmpty()) {
                    for(Long imageId : imagesId) {
                        ImageClass image = imageRepository.findById(imageId).orElse(null);
                        if(image != null) {
                            fileDealer.removeFile(image.getLocation());
                            imageRepository.delete(image);
                        }
                    }
                }
            }
            postRepository.deleteAll(posts);
        }

        if(replies != null && !replies.isEmpty()) {
            replyRepository.deleteAll(replies);
        }
    }
}
