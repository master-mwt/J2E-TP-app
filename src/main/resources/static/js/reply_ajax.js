//
// REPLY AJAX FUNCTIONS
//

import {makeToast} from './notify.js';

let loginpath = window.location.protocol + "//" + window.location.host + '/j2etpapp' + "/login";

$(document).ready(function() {
    // upvote handler
    $(document).on('click', '.replyupvote', function(e){
        e.preventDefault();
        let href = $(this).attr('href');
        let hrefarray = href.split('/');
        let reply_id = hrefarray[hrefarray.length - 2];

        $.ajax({
            method: "POST",
            url: href,
            success: function(data, textStatus, XMLHTTPRequest){

                let sVotenumber = $('#reply-' + reply_id + '-votenumber');
                sVotenumber.text(data.vote);

                if(data.upvotedAlready){
                    $('#reply-' + reply_id + '-upvote').toggleClass('text-danger');
                    $('#reply-' + reply_id + '-votenumber').toggleClass('text-danger').toggleClass('text-dark');
                } else if(data.downvotedAlready){
                    $('#reply-' + reply_id + '-upvote').toggleClass('text-danger');
                    $('#reply-' + reply_id + '-downvote').toggleClass('text-danger');
                } else {
                    $('#reply-' + reply_id + '-upvote').toggleClass('text-danger');
                    $('#reply-' + reply_id + '-votenumber').toggleClass('text-danger').toggleClass('text-dark');
                }

            },
            error: function(XMLHTTPRequest, textStatus, errorThrown){
                makeToast("Error", errorThrown, 4000);
            },
        });
    });
    // downvote handler
    $(document).on('click', '.replydownvote', function(e){
        e.preventDefault();
        let href = $(this).attr('href');
        let hrefarray = href.split('/');
        let reply_id = hrefarray[hrefarray.length - 2];

        $.ajax({
            method: "POST",
            url: href,
            success: function(data, textStatus, XMLHTTPRequest){

                let sVotenumber = $('#reply-' + reply_id + '-votenumber');
                sVotenumber.text(data.vote);

                if(data.downvotedAlready){
                    $('#reply-' + reply_id + '-downvote').toggleClass('text-danger');
                    $('#reply-' + reply_id + '-votenumber').toggleClass('text-danger').toggleClass('text-dark');
                } else if(data.upvotedAlready){
                    $('#reply-' + reply_id + '-downvote').toggleClass('text-danger');
                    $('#reply-' + reply_id + '-upvote').toggleClass('text-danger');
                } else {
                    $('#reply-' + reply_id + '-downvote').toggleClass('text-danger');
                    $('#reply-' + reply_id + '-votenumber').toggleClass('text-danger').toggleClass('text-dark');
                }

            },
            error: function(XMLHTTPRequest, textStatus, errorThrown){
                makeToast("Error", errorThrown, 4000);
            },
        });
    });
    // delete handler
    $(document).on('click', '.replydelete', function(e){
        e.preventDefault();
        let href = $(this).attr('href');
        let hrefarray = href.split('/');
        let reply_id = hrefarray[hrefarray.length - 1];

        if(href.trim() === loginpath){
            window.location.href = loginpath;
            return;
        }

        $.ajax({
            method: "DELETE",
            url: href,
            success: function(data, textStatus, XMLHTTPRequest){

                let reply = $('#reply-' + reply_id);
                reply.remove();

            },
            error: function(XMLHTTPRequest, textStatus, errorThrown){
                makeToast("Error", errorThrown, 4000);
            },
        });
    });

});
