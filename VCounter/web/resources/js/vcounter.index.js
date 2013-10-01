var id;
var stat_link;

function updateCode() {
    var t = $('input[name=t]:checked').val();
    var s = $('input[name=s]:checked').val();
    var c_img = "http://vcounter.de/c?id=" + id + "&t=" + t + "&s=" + s;
    var prev_img = "http://vcounter.de/resources/images/counter/" + t + "_" + s + ".png";

    if (t === '6') {
        c_img = "http://vcounter.de/c?id=" + id + "&t=" + t + "&s=0";
        $('#vcounter_code').val("<img style=\"display:none; visibility:hidden;\" src=\"" + c_img + "\" alt=\"VCounter.de Besucherzähler\">");
        $('#size').slideUp('fast', function() {
            $('#prev').slideUp('fast', function() {
                $('#stat').slideDown('fast');
            });
        });

    } else {
        c_img = "http://vcounter.de/c?id=" + id + "&t=" + t + "&s=" + s;
        $('#vcounter_code').val("<a href=\"" + stat_link + "\"><img src=\"" + c_img + "\" alt=\"VCounter.de Besucherzähler\"></a>");
        $('#prev_img').attr('src', prev_img);
        $('#stat').slideUp('fast', function() {
            $('#size').slideDown('fast', function() {
                $('#prev').slideDown('fast');
            });
        });
    }
}

$(document).ready(function() {
    id = new Date().getTime();
    stat_link = "http://vcounter.de/stat.xhtml?id=" + id;
    $('#stat_link').val(stat_link);
    $('#stat').hide();
    updateCode();
    $('input[name=t], input[name=s]').change(updateCode);
});