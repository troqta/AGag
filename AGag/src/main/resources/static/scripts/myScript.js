$(document).ready(function (ev) {
    $('#loginButton').on('click', function (ev) {
    console.log("IM HERE NIGGER");
        ev.preventDefault();
        let username = $('#username').val();
        let password = $('#password').val();

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/login',
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify({
                "username": username,
                "password": password
            })
        }).fail(function (xhr, status, error) {
            $('#errors').append("<p>some error:"+ error +"<p>")

        });

    });
});