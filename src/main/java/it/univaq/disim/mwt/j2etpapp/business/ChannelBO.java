package it.univaq.disim.mwt.j2etpapp.business;

import it.univaq.disim.mwt.j2etpapp.domain.ChannelClass;
import it.univaq.disim.mwt.j2etpapp.domain.UserClass;

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
    Set<UserClass> getReportedUsers(long channelId);
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
}
