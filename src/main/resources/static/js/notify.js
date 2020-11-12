// UTILS: NOTIFY

/**
 * A stacking toast generation
 *
 * @param {string} title The title of the toast
 * @param {string} body The body of the toast
 * @param {number} delay The delay of the toast (in milliseconds)
 */
let toastId = 0;
function makeToast(title, body, delay){
    let now = new Date();
    let time = now.getHours() + ':' + now.getMinutes();

    let toast = `<div id="toast${toastId}" class="toast ml-auto bg-primary m-4" data-delay="${delay}" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="toast-header">
                        <strong class="mr-auto">${title}</strong>
                        <small class="text-muted">${time}</small>
                        <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="toast-body">${body}</div>
                 </div>`;

    $('#toast-container').append(toast);
    // TODO: Toast not showing
    console.log('toast appended');
    $(document).find('#toast' + toastId).toast('show');
    console.log('toast fired');
    toastId++;
}


/**
 * Notifications handling
 */
let notifications = [];
let baseUrl = window.location.protocol + "//" + window.location.host + '/j2etpapp';
let postpath = window.location.protocol + "//" + window.location.host + '/j2etpapp' + "/discover/post/";

// TODO: Notifications

$(document).ready(function() {
    if(window.userIsLogged){
        notification();
        setInterval(notification, 10000);
    }
});

function notification(){
    $.get(baseUrl + '/notifications', function (data) {
        console.log(data);
        addNotifications(data);
    });
}

function addNotifications(newNotifications) {

    if(newNotifications.length === notifications.length){
        return;
    }

    notifications = newNotifications;
    $('#notification-area').empty();
    $('#notification-count').text(notifications.length + ' Notifications');

    if(notifications.length){
        $('#notification-button').addClass('text-danger');
    } else {
        $('#notification-button').removeClass('text-danger');
    }

    notifications.forEach(function(entry) {
        getUser(entry.userCreatedById, entry);
    });
}

function getUser(user_id, entry){
    $.ajax({
        method: "GET",
        url: baseUrl + "/user/" + user_id,
        success: function(data, textStatus, XMLHTTPRequest){
            notify(data, entry);
        },
    });
}

function notify(user, entry){

    let notification = `<div class="media">
                                <img src="${baseUrl + '/' + user.userImage}" alt="User Avatar" class="img-size-50 mr-3 img-circle">
                                <div class="media-body">
                                    <h3 class="dropdown-item-title">
                                    ${user.username}
                                    </h3>
                                    <a id="${entry.id}" href="${postpath + entry.postId}" class="text-sm notification-entry">${entry.content}</a>
                                    <p class="text-sm text-muted"><i class="far fa-clock mr-1"></i> ${new Date(entry.created_at).toGMTString()}</p>
                                </div>
                            </div>`;

    $('#notification-area').append(notification);

    // TODO: makeToast
    makeToast('Notification', 'You have new notifications', 4000);
}

$(document).ready(function() {
    // delete notification
    $(document).on('click', '.notification-entry', function(e){
        let notificationId = $(this).attr('id');

        $.ajax({
            method: "POST",
            url: baseUrl + `/notifications/${notificationId}/delete`,
            success: function(data, textStatus, XMLHTTPRequest){
            },
            error: function(XMLHTTPRequest, textStatus, errorThrown){
            },
        });
    });
    // delete all notifications
    $(document).on('click', '#notifications-delete-all', function(e){
        $.ajax({
            method: "POST",
            url: baseUrl + `/notifications/delete_all`,
            success: function(data, textStatus, XMLHTTPRequest){
                notifications = [];
                $('#notification-area').empty();
                $('#notification-count').text('no notifications');

                if(notifications.length){
                    $('#notification-button').addClass('text-danger');
                } else {
                    $('#notification-button').removeClass('text-danger');
                }
            },
            error: function(XMLHTTPRequest, textStatus, errorThrown){
            },
        });
    });
});

export {
    makeToast,
};
