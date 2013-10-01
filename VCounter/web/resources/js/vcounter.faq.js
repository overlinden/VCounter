$(document).ready(function() {
    $(".accordion li strong").addClass("pointer");
    $(".accordion li p").hide();
    $(".accordion li strong").click(function() {
        $(this).parent().find("p").slideToggle();
    });
});