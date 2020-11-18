package it.univaq.disim.mwt.postd.business;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AjaxResponse {
    public long vote;
    public boolean upvotedAlready;
    public boolean downvotedAlready;

    public AjaxResponse(long vote, boolean upvotedAlready, boolean downvotedAlready) {
        this.vote = vote;
        this.upvotedAlready = upvotedAlready;
        this.downvotedAlready = downvotedAlready;
    }
}
