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
            console.log('I AM IN THE BUTTON');
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/admin/api/user/ban/"+$item.text(), true);
            xhttp.onload = function(){
                console.log('RESPONSE TEXT ' + xhttp.responseText);
                M.toast({html: xhttp.responseText})
            };
            var neshto = xhttp.send();

            // .css('background-color', 'rgba(255, 0, 0, 40)')
            $item.parent().css('background-color', 'rgba(255, 0, 0, 0.5)');
            $('#confirmButton').prop("onclick", null).off("click");
            button.prop("onclick", null).off("click");
            button.html('UNBAN');
            button.on('click', unbanFunction);
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
            xhttp.onload = function () {
                console.log('RESPONSE TEXT ' + xhttp.responseText);
                M.toast({html: xhttp.responseText})
            };
            xhttp.send();

            $item.parent().css('background-color', 'rgba(0, 255, 0, 0.5)');
            $('#confirmButton').prop("onclick", null).off("click");
            button.prop("onclick", null).off("click");
            button.html('BAN');
            button.on('click', banFunction);
        });

        $('.modal').modal();
    };

});