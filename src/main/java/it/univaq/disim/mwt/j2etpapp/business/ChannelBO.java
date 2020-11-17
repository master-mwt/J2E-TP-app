package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ChannelBO {

    List<ChannelClass> findAll();
    ChannelClass findById(Long id) throws BusinessException;
    ChannelClass findByName(String name);
    List<ChannelClass> findByNameContains(String name);
    Page<ChannelClass> findByNameContainsPaginated(String name, int page, int size);
    void save(ChannelClass channel);
    void saveAll(List<ChannelClass> channels);
    void saveAll(ChannelClass... channels);
    void deleteById(Long id) throws BusinessException;
    void delete(ChannelClass channel);
    Long count();

    Set<UserClass> getSoftBannedUsers(Long channelId) throws BusinessException;
    void setSoftBannedUsers(Long channelId, Set<UserClass> softBannedUsers) throws BusinessException;
    void appendSoftBannedUsers(Long channelId, Set<UserClass> softBannedUsers) throws BusinessException;
    Set<UserClass> getReportedUsers(Long channelId) throws BusinessException;
    void setReportedUsers(Long channelId, Set<UserClass> reportedUsers) throws BusinessException;
    void appendReportedUsers(Long channelId, Set<UserClass> reportedUsers) throws BusinessException;
    void joinChannel(Long channelId, UserClass user) throws BusinessException;
    void leaveChannel(Long channelId, UserClass user) throws BusinessException;
    void globalUnreportPost(Long channelId, String postId) throws BusinessException;
    void reportUser(Long channelId, Long userId, UserClass principal) throws BusinessException;
    void unReportUser(Long channelId, Long userId, UserClass principal) throws BusinessException;
    void softBan(Long channelId, Long userId, UserClass principal) throws BusinessException;
    void unSoftBan(Long channelId, Long userId, UserClass principal) throws BusinessException;
    void upgradeMemberToModerator(Long channelId, Long userId) throws BusinessException;
    void upgradeModeratorToAdmin(Long channelId, Long userId) throws BusinessException;
    void downgradeModeratorToMember(Long channelId, Long userId) throws BusinessException;
    void upgradeAdminToCreator(Long channelId, Long userId) throws BusinessException;
    void downgradeAdminToModerator(Long channelId, Long userId) throws BusinessException;
    void downgradeCreatorToAdmin(Long channelId, Long userId) throws BusinessException;
    void removeImage(Long channelId) throws BusinessException;
    String saveImage(Long channelId, MultipartFile image) throws BusinessException;
    void createChannel(ChannelClass channel, UserClass creator) throws BusinessException;
    void updateChannel(Long channelId, ChannelClass newData) throws BusinessException;
}
