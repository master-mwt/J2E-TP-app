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

    Set<UserClass> getSoftBannedUsers(Long channelId);
    void setSoftBannedUsers(Long channelId, Set<UserClass> softBannedUsers);
    void appendSoftBannedUsers(Long channelId, Set<UserClass> softBannedUsers);
    Set<UserClass> getReportedUsers(Long channelId);
    void setReportedUsers(Long channelId, Set<UserClass> reportedUsers);
    void appendReportedUsers(Long channelId, Set<UserClass> reportedUsers);
    void joinChannel(Long channelId, UserClass user);
    void leaveChannel(Long channelId, UserClass user);
    void globalUnreportPost(Long channelId, String postId) throws BusinessException;
    void reportUser(Long channelId, Long userId);
    void unReportUser(Long channelId, Long userId);
    void softBan(Long channelId, Long userId);
    void unSoftBan(Long channelId, Long userId);
    void upgradeMemberToModerator(Long channelId, Long userId);
    void upgradeModeratorToAdmin(Long channelId, Long userId);
    void downgradeModeratorToMember(Long channelId, Long userId);
    void upgradeAdminToCreator(Long channelId, Long userId) throws BusinessException;
    void downgradeAdminToModerator(Long channelId, Long userId);
    void downgradeCreatorToAdmin(Long channelId, Long userId);
    void removeImage(Long channelId);
    void saveImage(Long channelId, MultipartFile image) throws BusinessException;
    void createChannel(ChannelClass channel, UserClass creator);
    void updateChannel(Long channelId, ChannelClass newData);
}
