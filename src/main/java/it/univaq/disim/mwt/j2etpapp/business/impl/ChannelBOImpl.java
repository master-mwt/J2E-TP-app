package it.univaq.disim.mwt.j2etpapp.business.impl;

import it.univaq.disim.mwt.j2etpapp.business.BusinessException;
import it.univaq.disim.mwt.j2etpapp.business.ChannelBO;
import it.univaq.disim.mwt.j2etpapp.business.Page;
import it.univaq.disim.mwt.j2etpapp.domain.*;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.ChannelRepository;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.RoleRepository;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserChannelRoleRepository;
import it.univaq.disim.mwt.j2etpapp.repository.jpa.UserRepository;
import it.univaq.disim.mwt.j2etpapp.repository.mongo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserRepository userRepository;

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
    }

    @Override
    public void delete(ChannelClass channel) {
        channelRepository.delete(channel);
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
    public Set<UserClass> getReportedUsers(long channelId) {
        ChannelClass channel = channelRepository.findById(channelId).orElse(null);
        return channel.getReportedUsers();
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
            currentMember.setRole(moderator);
            userChannelRoleRepository.save(currentMember);
        }
    }

    @Override
    public void upgradeModeratorToAdmin(long channelId, long userId) {
        RoleClass moderator = roleRepository.findByName("moderator").orElse(null);
        RoleClass admin = roleRepository.findByName("admin").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(moderator.equals(currentMember.getRole())){
            currentMember.setRole(admin);
            userChannelRoleRepository.save(currentMember);
        }
    }

    @Override
    public void downgradeModeratorToMember(long channelId, long userId) {
        RoleClass member = roleRepository.findByName("member").orElse(null);
        RoleClass moderator = roleRepository.findByName("moderator").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(moderator.equals(currentMember.getRole())){
            currentMember.setRole(member);
            userChannelRoleRepository.save(currentMember);
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
            currentMember.setRole(creator);
            channel.setCreator(userRepository.findById(userId).orElse(null));
            userChannelRoleRepository.save(currentMember);
            channelRepository.save(channel);
        }
    }

    @Override
    public void downgradeAdminToModerator(long channelId, long userId) {
        RoleClass moderator = roleRepository.findByName("moderator").orElse(null);
        RoleClass admin = roleRepository.findByName("admin").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(admin.equals(currentMember.getRole())){
            currentMember.setRole(moderator);
            userChannelRoleRepository.save(currentMember);
        }
    }

    @Override
    public void downgradeCreatorToAdmin(long channelId, long userId) {
        RoleClass admin = roleRepository.findByName("admin").orElse(null);
        RoleClass creator = roleRepository.findByName("creator").orElse(null);

        UserChannelRole currentMember = userChannelRoleRepository.findByChannelIdAndUserId(channelId, userId).orElse(null);
        if(creator.equals(currentMember.getRole())){
            currentMember.setRole(admin);
            userChannelRoleRepository.save(currentMember);
        }
    }
}
