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
        $('#userPlaceholder').html("Are you sure you wish to ban " + itemText);
        $('#confirmButton').on('click', function(){
            console.log('I AM IN THE BUTTON');
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/admin/user/ban/"+$item.text(), true);
           var neshto = xhttp.send();
           console.log(neshto);
            $item.parent().css('background-color', 'argb(255, 0, 0, 40)');
            $('#confirmButton').off('click', '#confirmButton');
        });
        $('.modal').modal();
    });
    //TODO fix css and add handler with toast
    $('.myUnbanClass').on('click', function(){
        $item = $(this).closest("tr")
        .find(".username")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to unban " + itemText);
        $('#confirmButton').on('click', function(){
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/admin/user/unban/"+$item.text(), true);
            xhttp.send();

            $('#confirmButton').off('click', '#confirmButton');
        });

        $('.modal').modal();
    });
    $('.unBanBtnClass')
    // $('.modal').modal();

});