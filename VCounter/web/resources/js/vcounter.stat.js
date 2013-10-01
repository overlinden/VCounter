$(document).ready(function() {
    $.tablesorter.addParser({
        id: 'germandatetime',
        is: function(s) {
            return false;
        },
        format: function(s) {
            var a = s.split('.');
            a[1] = a[1].replace(/^[0]+/g, "");
            tmp = a[2].split(' ');
            a[2] = tmp[0];
            t = tmp[1].split(':');
            return new Date(a[2], a[1] - 1, a[0], t[0], t[1], t[2]).getTime();
        },
        type: 'numeric'
    });
    
    $("#last5").tablesorter({
        headers: {0: {sorter: 'germandatetime'}},
        sortList: [[0, 1]]
    });
});