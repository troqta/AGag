$(document).ready(function () {
    var url = $(location).attr('href');
    if(url.includes("gag")){
    var parameter = url.substring(url.lastIndexOf('/') + 1);
    $('#'+parameter).addClass("active");
    console.log(parameter);
    }

});