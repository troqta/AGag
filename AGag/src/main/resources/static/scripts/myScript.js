$(document).ready(function () {
    var url = $(location).attr('href');
    if(url.includes("gag")){
        var parameter = url.substring(url.lastIndexOf('/') + 1);
        if(parameter.includes("#")){
            parameter = parameter.substring(0, parameter.length-1);
        }
        $('#'+parameter).addClass("active");
        console.log(parameter);
    }
    $("#sidenavBtn").on('click', function(){
        $('.sidenav').sidenav();
    });
    var $item;
    

    $('.myBanClass').on('click', banFunction);
    //TODO fix css and add handler with toast
    $('.myUnbanClass').on('click', unbanFunction );
    // $('.modal').modal();
    var button;
    function banFunction(){
       button = $(this);
        $item = $(this).closest("tr")
            .find(".username")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to ban " + itemText);
        $('#confirmButton').on('click', function(){
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/admin/api/user/ban/"+$item.text(), true);
            xhttp.onload = function(){
                console.log('RESPONSE TEXT ' + xhttp.responseText);
                M.toast({html: xhttp.responseText})
                if(xhttp.responseText.includes('Successfully')){
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
                console.log('RESPONSE TEXT ' + xhttp.responseText);
                M.toast({html: xhttp.responseText})
                if(xhttp.responseText.includes('Successfully')){
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
    function appendPostsHot(){
        if(counter != 0){
            var xhttp = new XMLHttpRequest();
            xhttp.open('GET', '/api/gag/hot/'+counter, true);
            xhttp.onload = function(){
                var textJson = xhttp.responseText;
                if(textJson === "end"){
                    counter = 0;
                    $('#loadBox').append(
                        '<div>'+
                            'NO MORE POSTS'+
                        '</div>');
                        $('#scroll-toHot').css('display', 'none');
                    return;
                }
                var arr = JSON.parse(textJson);
                for(var gag in arr){
                    var tagArr = arr[gag].tags;
                    var tags = "";
                    for(var i in tagArr){
                        tags += '<a href="/tag/'+tagArr[i].name+'" class="chip">'+tagArr[i].name+'</a>';
                    }
                    console.log(tags);
                    $('#loadBox').append(
                        '<div class="myGag">'+
                            '<div style="display : inline-block">'+
                                '<a class="gagLink" href="/gag/'+arr[gag].id+'">'+arr[gag].name+'</a>'+
                                '<div class="inline">'+
                                    '<img class="materialboxed" src="'+arr[gag].content+'">'+'</img>'+
                                '</div>'+
                                '<a class="grayLink" href="/gag/'+ arr[gag].id+'">'+arr[gag].upvotes+' points  '+arr[gag].comments.length+' comments</a>'+
                                '<div style="text-align:left;">'+
                
                                    tags+
                            
                                '</div>'+
                            '</div>'+
                        
                        '</div>');
                }
                $('.materialboxed').materialbox();
                console.log(arr);
                console.log(counter);

                counter++;
            }

            xhttp.send();
        }

    }
    function appendPostsFresh(){
        if(counter != 0){
            var xhttp = new XMLHttpRequest();
            xhttp.open('GET', '/api/gag/fresh/'+counter, true);
            xhttp.onload = function(){
                var textJson = xhttp.responseText;
                if(textJson === "end"){
                    counter = 0;
                    $('#loadBox').append(
                        '<div>'+
                            'NO MORE POSTS'+
                        '</div>');
                        $('#scroll-toFresh').css('display', 'none');
                    return;
                }
                var arr = JSON.parse(textJson);
                for(var gag in arr){
                    var tagArr = arr[gag].tags;
                    var tags = "";
                    for(var i in tagArr){
                        tags += '<a href="/tag/'+tagArr[i].name+'" class="chip">'+tagArr[i].name+'</a>';
                    }
                    console.log(tags);
                    $('#loadBox').append(
                        '<div class="myGag">'+
                            '<div style="display : inline-block">'+
                                '<a class="gagLink" href="/gag/'+arr[gag].id+'">'+arr[gag].name+'</a>'+
                                '<div class="inline">'+
                                    '<img class="materialboxed" src="'+arr[gag].content+'">'+'</img>'+
                                '</div>'+
                                '<a class="grayLink" href="/gag/'+ arr[gag].id+'">'+arr[gag].upvotes+' points  '+arr[gag].comments.length+' comments</a>'+
                                '<div style="text-align:left;">'+
                
                                    tags+
                            
                                '</div>'+
                            '</div>'+
                        
                        '</div>');
                }
                $('.materialboxed').materialbox();
                console.log(arr);
                console.log(counter);

                counter++;
            }

            xhttp.send();
        }

    }
    var flag = false;
    if($('#scroll-toHot').length){
            $(window).scroll(function() {
        var hT = $('#scroll-toHot').offset().top,
            hH = $('#scroll-toHot').outerHeight(),
            wH = $(window).height(),
            wS = $(this).scrollTop();
        if (wS > (hT+hH-wH)){
            if(flag === false){
                    appendPostsHot();
                    flag = true;
                    setTimeout(function(){
                        flag = false;
                    }, 2000);
            }
            
        }
        });
    }
    if($('#scroll-toFresh').length){
        $(window).scroll(function() {
    var hT = $('#scroll-toFresh').offset().top,
        hH = $('#scroll-toFresh').outerHeight(),
        wH = $(window).height(),
        wS = $(this).scrollTop();
    if (wS > (hT+hH-wH)){
        if(flag === false){
                appendPostsFresh();
                flag = true;
                setTimeout(function(){
                    flag = false;
                }, 2000);
        }
        
    }
    });
}
    $(document).ready(function(){
        $('.materialboxed').materialbox();
    });

    $('#newImg').change(function(){
        var input = this;
        if (input.files && input.files[0]) {
            var reader = new FileReader();
        
            reader.onload = function(e) {
                $('#preview').remove();
                $('#previewBox').append('<img id="preview" style="width:200px" src="'+e.target.result+'" />');
            }
        
            reader.readAsDataURL(input.files[0]);
          }
    });
    $('#editImg').change(function(){
        var input = this;
        if (input.files && input.files[0]) {
            var reader = new FileReader();
        
            reader.onload = function(e) {
                $('#oldImg').attr('src', e.target.result);
            }
        
            reader.readAsDataURL(input.files[0]);
          }
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