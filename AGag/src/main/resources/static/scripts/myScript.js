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

});