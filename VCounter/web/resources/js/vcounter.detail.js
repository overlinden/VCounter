$(document).ready(function() {
    $.tablesorter.addParser({
        id: 'germandate',
        is: function(s) {
            return false;
        },
        format: function(s) {
            var a = s.split('.');
            a[1] = a[1].replace(/^[0]+/g, "");
            return new Date(a.reverse().join("/")).getTime();
        },
        type: 'numeric'
    });
    initTableSort();
});


function reSort(e) {
    if (e.status == 'success') {
        initTableSort();
    }
}

function initTableSort() {
    $("#last30").tablesorter({
        headers: {0: {sorter: 'germandate'}},
        sortList: [[0, 1]]
    });
    $("#tophits").tablesorter({
        headers: {0: {sorter: 'germandate'}},
        sortList: [[2, 1]]
    });
    $("#topvisits").tablesorter({
        headers: {0: {sorter: 'germandate'}},
        sortList: [[1, 1]]
    });
    $("#topips").tablesorter({
        sortList: [[1, 1]]
    });
    $("#topbrowser").tablesorter({
        sortList: [[1, 1]]
    });
    $("#toppages").tablesorter({
        sortList: [[1, 1]]
    });
}