$(document).ready(function () {
    $('.dropdown-trigger').dropdown();
    $('.dropify').dropify();
    $('.tooltipped').tooltip();
    $('#tabs').tabs();
    var url = $(location).attr('href');
    if (url.includes("gag")) {
        var parameter = url.substring(url.lastIndexOf('/') + 1);
        if (parameter.includes("#")) {
            parameter = parameter.substring(0, parameter.length - 1);
        }
        $('#' + parameter).addClass("active");
    }
    $("#sidenavBtn").on('click', function () {
        $('.sidenav').sidenav();
    });
    var $item;


    $('.myBanClass').on('click', banFunction);
    //TODO fix css and add handler with toast
    $('.myUnbanClass').on('click', unbanFunction);
    // $('.modal').modal();
    var button;

    function banFunction() {
        button = $(this);
        $item = $(this).closest("tr")
            .find(".username")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to ban " + itemText);
        $('#confirmButton').on('click', function () {
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/admin/api/user/ban/" + $item.text(), true);
            xhttp.onload = function () {
                M.toast({html: xhttp.responseText})
                if (xhttp.responseText.includes('Successfully')) {
                    $item.parent().css('background-color', 'rgba(255, 0, 0, 0.5)');

                    button.prop("onclick", null).off("click");
                    button.html('UNBAN');
                    button.on('click', unbanFunction);
                }
            }
            $('#confirmButton').prop("onclick", null).off("click");
            xhttp.send();
        });
        $('.modal').modal();
    };

    function unbanFunction() {
        button = $(this);
        $item = $(this).closest("tr")
            .find(".username")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to unban " + itemText);
        $('#confirmButton').on('click', function () {
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/admin/api/user/unban/" + $item.text(), true);
            var flag = false;
            xhttp.onload = function () {
                M.toast({html: xhttp.responseText})
                if (xhttp.responseText.includes('Successfully')) {
                    $item.parent().css('background-color', 'rgba(0, 255, 0, 0.5)');
                    button.prop("onclick", null).off("click");
                    button.html('BAN');
                    button.on('click', banFunction);
                }
            }
            $('#confirmButton').prop("onclick", null).off("click");
            xhttp.send();
        });

        $('.modal').modal();
    };
    var counter = 1;

    function appendPostsHot() {
        if (counter != 0) {
            var xhttp = new XMLHttpRequest();
            xhttp.open('GET', '/api/gag/hot/' + counter, true);
            xhttp.onload = function () {
                var textJson = xhttp.responseText;
                if (textJson === "end") {
                    counter = 0;
                    $('#loadBox').append(
                        '<div class="btn disabled bot-margin">' +
                        'NO MORE POSTS' +
                        '</div>');
                    $('#scroll-toHot').css('display', 'none');
                    return;
                }
                var arr = JSON.parse(textJson);
                for (var gag in arr) {
                    var tagArr = arr[gag].tags;
                    var tags = "";
                    for (var i in tagArr) {
                        tags += '<a href="/tag/' + tagArr[i].name + '" class="chip">' + tagArr[i].name + '</a>';
                    }
                    $('#loadBox').append(
                        '<div class="myGag">' +
                        '<div style="display : inline-block">' +
                        '<a class="gagLink" href="/gag/' + arr[gag].id + '">' + arr[gag].name + '</a>' +
                        '<div class="inline">' +
                        '<img class="materialboxed" src="' + arr[gag].content + '">' + '</img>' +
                        '</div>' +
                        '<a class="grayLink" href="/gag/' + arr[gag].id + '">' + arr[gag].upvotes + ' points  ' + arr[gag].comments.length + ' comments</a>' +
                        '<div style="text-align:left;">' +

                        tags +

                        '</div>' +
                        '</div>' +

                        '</div>');
                }
                $('.materialboxed').materialbox();

                counter++;
            }

            xhttp.send();
        }

    }

    function appendPostsFresh() {
        if (counter != 0) {
            var xhttp = new XMLHttpRequest();
            xhttp.open('GET', '/api/gag/fresh/' + counter, true);
            xhttp.onload = function () {
                var textJson = xhttp.responseText;
                if (textJson === "end") {
                    counter = 0;
                    $('#loadBox').append(
                        '<div class="btn disabled bot-margin">' +
                        'NO MORE POSTS' +
                        '</div>');
                    $('#scroll-toFresh').css('display', 'none');
                    return;
                }
                var arr = JSON.parse(textJson);
                for (var gag in arr) {
                    var tagArr = arr[gag].tags;
                    var tags = "";
                    for (var i in tagArr) {
                        tags += '<a href="/tag/' + tagArr[i].name + '" class="chip">' + tagArr[i].name + '</a>';
                    }
                    $('#loadBox').append(
                        '<div class="myGag">' +
                        '<div style="display : inline-block">' +
                        '<a class="gagLink" href="/gag/' + arr[gag].id + '">' + arr[gag].name + '</a>' +
                        '<div class="inline">' +
                        '<img class="materialboxed" src="' + arr[gag].content + '">' + '</img>' +
                        '</div>' +
                        '<a class="grayLink" href="/gag/' + arr[gag].id + '">' + arr[gag].upvotes + ' points  ' + arr[gag].comments.length + ' comments</a>' +
                        '<div style="text-align:left;">' +

                        tags +

                        '</div>' +
                        '</div>' +

                        '</div>');
                }
                $('.materialboxed').materialbox();


                counter++;
            }

            xhttp.send();
        }

    }

    var flag = false;
    if ($('#scroll-toHot').length) {
        $(window).scroll(function () {
            var hT = $('#scroll-toHot').offset().top,
                hH = $('#scroll-toHot').outerHeight(),
                wH = $(window).height(),
                wS = $(this).scrollTop();
            if (wS > (hT + hH - wH)) {
                if (flag === false) {
                    appendPostsHot();
                    flag = true;
                    setTimeout(function () {
                        flag = false;
                    }, 2000);
                }

            }
        });
    }
    if ($('#scroll-toFresh').length) {
        $(window).scroll(function () {
            var hT = $('#scroll-toFresh').offset().top,
                hH = $('#scroll-toFresh').outerHeight(),
                wH = $(window).height(),
                wS = $(this).scrollTop();
            if (wS > (hT + hH - wH)) {
                if (flag === false) {
                    appendPostsFresh();
                    flag = true;
                    setTimeout(function () {
                        flag = false;
                    }, 2000);
                }

            }
        });
    }
    $(document).ready(function () {
        $('.materialboxed').materialbox();
    });

    $('#newImg').change(function () {
        var input = this;
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#preview').remove();
                $('#previewBox').append('<img id="preview" style="width:200px" src="' + e.target.result + '" />');
            }

            reader.readAsDataURL(input.files[0]);
        }
    });
    $('#editImg').change(function () {
        var input = this;
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#oldImg').attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    });


    $('#likeBtn').on('click', function () {
        var xhttp = new XMLHttpRequest();
        var id = parseInt(url.substring(url.lastIndexOf('/') + 1), 10);
        xhttp.open('POST', '/api/gag/like/' + id, true);
        xhttp.onload = function () {
            if (xhttp.responseText.includes('successful')) {
                $('#likeBtn').addClass('disabled');
                var text = $('#pointsComments').html();
                var textarr = text.split(' ');
                var number = parseInt(textarr[0], 10) + 1;
                textarr[0] = number;
                $('#pointsComments').html(textarr.join(' '));
            }
            M.toast({html: xhttp.responseText});
        }
        xhttp.send();
    });
    $('#submitComment').on('click', function () {
        if ($('.commentContent').val() === "") {
            $('.commentContent').addClass('invalid');
            M.toast({html: 'Please enter comment content!'});
        }
        else {
            $('#commentForm').submit();
        }
    });
    var userNameValid = false;
    $('.userNameRegister').on('change', function () {
        var xhttp = new XMLHttpRequest();
        var $field = $('.userNameRegister');
        var parameter = $field.val();
        if (parameter === "") {
            $field.addClass('invalid');
            userNameValid = false;
            return;
        }
        var url = '/api/user/exists/' + parameter;
        xhttp.open('GET', url, true);
        xhttp.onload = function () {
            var response = xhttp.responseText;
            if (!response.includes("ok")) {
                $field.removeClass('valid');
                $field.addClass('invalid');
                M.toast({html: response});
                userNameValid = false;
            }
            else {
                $field.removeClass('invalid');
                $field.addClass('valid');
                userNameValid = true;
            }
        }
        xhttp.send();
    });

    function registerPasswordsCheck() {
        var $pass1 = $('#password');
        var $pass2 = $('#confirmPassword');
        if($pass1.val().length < 5 || $pass1.val().length > 20){
            $pass1.removeClass('valid');
            $pass1.addClass('invalid');
            M.toast({html: 'Password should be between 5 and 20 symbols long!'});
            return false;

        }
        if($pass2.val().length < 5 || $pass2.val().length > 20){
            $pass2.removeClass('valid');
            $pass2.addClass('invalid');
            M.toast({html: 'Password should be between 5 and 20 symbols long!'});
            return false;

        }
        if ($pass1.val() !== $pass2.val()) {
            $pass1.removeClass('valid');
            $pass1.addClass('invalid');
            $pass2.removeClass('valid');
            $pass2.addClass('invalid');
            M.toast({html: 'Passwords dont match!'});
            return false;
        }
        return true;

    }
    $('#registerBtn').on('click', function () {
        if (!userNameValid) {
            M.toast({html: 'Invalid Username!'});
            return;
        }
        if(registerPasswordsCheck()){
            $('#registerForm').submit();
        }
    });
    $('#gagBtn').on('click', function () {
        var $title = $('#name');
        var $file = $('.dropify');
        if ($title.val() === ""){
            M.toast({html: 'Please enter a title!'});
            return;
        }
        else if ($title.val().length < 5){
            M.toast({html: 'Title should be atleast 5 symbols long!'});
            return;
        } 
        if (!$file.val()) {
            M.toast({html: 'Please upload a file!'});
            return;
        }
        var xhttp = new XMLHttpRequest()
        xhttp.open('GET', '/api/gag/exists/' + $title.val(), true);
        xhttp.onload = function () {
            if (!xhttp.responseText.includes("Valid")){
                M.toast({html: 'Gag with this title already exists!!'});
                $title.addClass("invalid");

            }
            else{
                $('#gagForm').submit();
            }
        };
        xhttp.send();


        
    });
    $('#editUserBtn').on('click', function () {
        var $pass1 = $('#password');
        var $pass2 = $('#confirmPassword');
        var $oldPass = $('#oldPassword');

        if($pass1.val().length > 0 || $pass2.val().length > 0 ){
            if($oldPass.val().length === 0){
                M.toast({html: 'If you want to change password please enter your old password!'});
                $oldPass.addClass('invalid');
                return;
            }
            if($pass1.val() !== $pass2.val()){
                M.toast({html: 'Passwords don\'t match!'});
                $pass1.addClass('invalid');
                $pass2.addClass('invalid');
                return;
            }
            if($pass1.val().length < 5 || $pass1.val().length > 20 || $pass2.val().length < 5 || $pass2.val().length > 20){
                M.toast({html: 'Password should be between 5 and 20 symbols long!'});
                $pass1.addClass('invalid');
                $pass2.addClass('invalid');
                return;
            }
        }


        $('#editUserForm').submit();
    });
});
let scrollPos = 0;
const nav = document.querySelector('.site-nav');

function checkPosition() {
    let windowY = window.scrollY;
    if (windowY < scrollPos) {
        // Scrolling UP
        nav.classList.add('is-visible');
        nav.classList.remove('is-hidden');
    } else {
        // Scrolling DOWN
        nav.classList.add('is-hidden');
        nav.classList.remove('is-visible');
    }
    scrollPos = windowY;
}

window.addEventListener('scroll', checkPosition);