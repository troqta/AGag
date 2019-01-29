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
    

    $('.myBanClass').on('click', function(){
        $item = $(this).closest("tr")
        .find(".username")
        
        itemText = $item.text();
        console.log($item); 
        $('#userPlaceholder').html("Are you sure you wish to ban " + itemText);
        $('#confirmButton').addClass("banBtnClass");
        $('.modal').modal();
    });
    $('.banBtnClass').on('click', function(){
        console.log('I AM IN THE BUTTON');
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/admin/user/ban/"+$item.text(), true);
        xhttp.send();
        $item.parent().css('background-color', 'argb(255, 0, 0, 40)');
        $('#confirmButton').removeClass("banBtnClass");
    });

    $('.myUnbanClass').on('click', function(){
        $item = $(this).closest("tr")
        .find(".username")
        itemText = $item.text();
        console.log($item); 
        $('#userPlaceholder').html("Are you sure you wish to unban " + itemText);
        $('#confirmButton').addClass("unBanBtnClass");

        $('.modal').modal();
    });
    $('.unBanBtnClass').on('click', function(){
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/admin/user/unban/"+$item.text(), true);
        xhttp.send();
    
        $('#confirmButton').removeClass("unBanBtnClass");
    });
    // $('.modal').modal();

});