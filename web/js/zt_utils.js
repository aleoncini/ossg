function calculateAdditionalStrokes(phcp, hhcp){
    if (phcp == 18){
        return 1;
    }

    if (phcp < 18){
        if (phcp >= hhcp){
            return 1;
        } else {
            return 0;
        }
    }

    if (phcp > 18){
        if ((phcp - 18) >= hhcp) {
            return 2;
        } else {
            return 1;
        }
    }
};

function calculateProjection(hole_number, points){
    return points + ((18 - hole_number) * 2);
};
function calculateStableford(strokes, par, additionalStrokes){
    var stb = ((par + additionalStrokes) - strokes) + 2;
    if (stb < 0){
        stb = 0;
    }
    return stb;
};

function formattedAdditionalStrokes(num) {
    var ret = '&nbsp';
    switch (num){
        case 2:
            ret = '<img src="img/add_two.png">';
            break;
        case 1:
            ret = '<img src="img/add_one.png">';
            break;
    }
    return ret;
};

function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
};

function getTotalStrokes(hole_number, scorecard){
    var total = 0;
    for (i = 1; i<=hole_number; i++){
        total += scorecard.scores[i].strokes;
    }
    return total;
};
function getTotalStableford(hole_number, scorecard){
    var total = 0;
    for (i = 1; i<=hole_number; i++){
        total += scorecard.scores[i].points;
    }
    return total;
};
function getMonthName(monthNumber){
    switch(monthNumber) {
        case 1:
            return 'Jan';
        case 2:
            return 'Feb';
        case 3:
            return 'Mar';
        case 4:
            return 'Apr';
        case 5:
            return 'May';
        case 6:
            return 'Jun';
        case 7:
            return 'Jul';
        case 8:
            return 'Aug';
        case 9:
            return 'Sep';
        case 10:
            return 'Oct';
        case 11:
            return 'Nov';
        default:
            return "Dic";
    }
};