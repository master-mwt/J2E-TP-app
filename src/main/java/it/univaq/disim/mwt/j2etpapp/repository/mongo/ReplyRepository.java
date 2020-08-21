package it.univaq.disim.mwt.j2etpapp.repository.mongo;

import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends PagingAndSortingRepository<ReplyClass, String> {
    Optional<List<ReplyClass>> findByPost(PostClass post);
    Optional<List<ReplyClass>> findByChannelId(Long channelId);
    Optional<List<ReplyClass>> findByUserId(Long userId);
}
