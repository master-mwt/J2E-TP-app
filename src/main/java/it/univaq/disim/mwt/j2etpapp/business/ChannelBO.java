package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ChannelBO {

    List<ChannelClass> findAll();
    ChannelClass findById(Long id);
    ChannelClass findByName(String name);
    List<ChannelClass> findByNameContains(String name);
    Page<ChannelClass> findByNameContainsPaginated(String name, int page, int size);
    void save(ChannelClass channel);
    void saveAll(List<ChannelClass> channels);
    void saveAll(ChannelClass... channels);
    void deleteById(Long id);
    void delete(ChannelClass channel);
    Long count();

    Set<UserClass> getSoftBannedUsers(long channelId);
    void setSoftBannedUsers(long channelId, Set<UserClass> softBannedUsers);
    void appendSoftBannedUsers(long channelId, Set<UserClass> softBannedUsers);
    Set<UserClass> getReportedUsers(long channelId);
    void setReportedUsers(long channelId, Set<UserClass> reportedUsers);
    void appendReportedUsers(long channelId, Set<UserClass> reportedUsers);
    void joinChannel(long channelId, UserClass user);
    void leaveChannel(long channelId, UserClass user);
    void globalUnreportPost(long channelId, String postId) throws BusinessException;
    void reportUser(long channelId, long userId);
    void unReportUser(long channelId, long userId);
    void softBan(long channelId, long userId);
    void unSoftBan(long channelId, long userId);
    void upgradeMemberToModerator(long channelId, long userId);
    void upgradeModeratorToAdmin(long channelId, long userId);
    void downgradeModeratorToMember(long channelId, long userId);
    void upgradeAdminToCreator(long channelId, long userId) throws BusinessException;
    void downgradeAdminToModerator(long channelId, long userId);
    void downgradeCreatorToAdmin(long channelId, long userId);
    void removeImage(long channelId);
    void saveImage(long channelId, MultipartFile image) throws BusinessException;
    void createChannel(ChannelClass channel, UserClass creator);
}
