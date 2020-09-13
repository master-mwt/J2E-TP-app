package it.univaq.disim.mwt.j2etpapp.repository.mongo;

import it.univaq.disim.mwt.j2etpapp.domain.PostClass;
import it.univaq.disim.mwt.j2etpapp.domain.ReplyClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ReplyRepository extends PagingAndSortingRepository<ReplyClass, String> {
    Optional<List<ReplyClass>> findByPost(PostClass post);
    Page<ReplyClass> findByPost(PostClass post, Pageable pageable);
    Optional<List<ReplyClass>> findByChannelId(Long channelId);
    Optional<List<ReplyClass>> findByUserId(Long userId);
    Page<ReplyClass> findByUsersDownvotedContains(Set<Long> usersDownvoted, Pageable pageable);
    Page<ReplyClass> findByUsersUpvotedContains(Set<Long> usersUpvoted, Pageable pageable);
}
