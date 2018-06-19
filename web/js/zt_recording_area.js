/**
 * Created by andrea on 29/10/2017.
 */

window.roundData = {};
window.results = {};

/**
 *
 *  Initial setup
 *  Checks for browser compatibility
 *  and user registration
 *
 */

function checkPlayer() {
    $.get("/rs/players/user", function(data) {
        var data = jQuery.parseJSON(response.responseText);
        if (data.id === "none"){
            $("#unregistered_user_alert").fadeIn();
        } else {
            localStorage.setItem("playerid", data.id);
            localStorage.setItem("playername", data.name);
        }
    });
};

function doubleCheck() {
    startWithBrowserCheck();
};

function startWithBrowserCheck() {
    if (typeof(Storage) !== "undefined") {
        //gotoUserRegistrationCheck();
        gotoInitializationCheck();
    } else {
        console.log("=====================> browser UNSUPPORTED")
        // Sorry! No Web Storage support..
        // display alert and disable buttons
        $("#invalid_browser_alert").fadeIn();
    }
};
function gotoInitializationCheck() {
    var b_check = (localStorage.getItem("browserChecked") == 'true');
    if ( ! b_check){
        localStorage.setItem("browserChecked", true);
        $("#browser_supported").fadeIn();
    }
    gotoUserRegistrationCheck();
};
function gotoUserRegistrationCheck() {
    var pid = localStorage.getItem("playerid");
    if (pid === null) {
        $("#unregistered_user_alert").fadeIn();
    }
};

function checkUserEmail(email) {
    var theUrl = '/rs/players/check/email?email=' + email;
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            if (data.id === "none"){
                askForUserName(email);
            } else {
                localStorage.setItem("playerid", data.id);
                localStorage.setItem("playername", data.name);
                $("#email_ok_alert").fadeIn();
            }
        }
    });
};
function askForUserName(email) {
    $("#hidden_email").val(email);
    $("#label_p").html("Now enter your first and second name");
    $("#reg_input").text("");
    $("#reg_input").val("");
    $("#reg_input").attr("placeholder", "Tiger Woods");
};
function completeUserRegistration(email, name) {
    var theUrl = '/ossg/rs/players/add';
    $.ajax({
        url: theUrl,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            "email": email,
            "name": name
        }),
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            if (data.status == 'success'){
                checkUserEmail(email);
            }
        }
    });
};
/* --- END Initial Setup ---------------------- */

/**
 *
 *  New Tournament page --------------------------
 *
 */
function createTournament() {
    var theUrl = '/rs/tournaments/ads';
    var theData = buildTournamentString();
    console.log("==> " + theData);
    $.ajax({
        url: theUrl,
        type: 'POST',
        contentType: 'application/json',
        data: theData,
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            if (data.status == 'success'){
                console.log("==========> new tournament with ID: " + data.id);
            }
        }
    });
};
function buildTournamentString() {
    var doe = {};
    doe.day = Number( $('#sel_tournament_day option:selected').text() );
    doe.month = Number( $('#sel_tournament_month option:selected').val() );
    doe.year = Number( $('#sel_tournament_year option:selected').text() );

    var data = {};
    data.ownerId = getPlayerId();
    data.title = $('#inp_tournament_title').val();
    data.dayOfEvent = doe;
    data.isOpen = true;
    data.type = Number( $('#radio_tour_type input:radio:checked').val() );
    return JSON.stringify(data);
}
/* --- END New Tournament page ---------------------- */

/**
 *
 *  All functions required to play a round
 *
 */
function initRoundPage() {
    getPlayerName(null,formatReviewRoundHelloPar);
    sessionStorage.setItem("courseid", "");
    sessionStorage.setItem("coursename", "");
    $('#courseid_hidden_value').val('');
    $('#tournamentid_hidden_value').val('');
};
/**
 *  setEventDay()
 *  called by the modal window to select the day of round
 *  when the user clicks on OK button.
 */
function setEventDay() {
    $("#day_of_event_hidden_value").val($("#sel_day option:selected").text());
    $("#month_of_event_hidden_value").val($("#sel_month option:selected").val());
    $("#year_of_event_hidden_value").val($("#sel_year option:selected").text());
    var td = $("#day_of_event_hidden_value").val();
    td += ' ' + $("#sel_month option:selected").text();
    td += ' ' + $("#year_of_event_hidden_value").val();
    $("#theDay").html(td);
};
/**
 *  getPlayerName()
 *  Starting from persistent playerid get player name
 *  from server and put it in a session local storage.
 *  Also put the name in the salutation paragraph.
 */
function getPlayerName(pid, callback) {
    if ((pid == null) || (pid.length == 0)){
        pid = localStorage.getItem("playerid");
    }
    var theUrl = '/rs/players/player/' + pid;
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var playerData = jQuery.parseJSON(response.responseText);
            sessionStorage.setItem("playerName", playerData.name);
            callback(playerData.name);
        }
    });
};
/**
 *  formatModalWindowToSelectCourse(courseName)
 *  Get the course list using the courseName keyword
 *  the result is passed to a formatting method.
 */
function formatModalWindowToSelectCourse(courseName) {
    $('#sel_course_list').hide();
    $('#sel_course_list').empty();
    var base_url = "/rs/courses/search?name=" + courseName;
    $.get(base_url, function(data) {
        addCoursesToSelection(data.courses);
    });
};
/**
 *  addCoursesToSelection(course_list)
 *  using the courses collection format the selection
 *  component in the modal window.
 *  At the end shows the modal window.
 */
function addCoursesToSelection(courses) {
    var sel_content = '<option value="0">select a course from list</option>';
    $('#sel_course_list').append(sel_content);
    $.each(courses, function (index, course) {
        sel_content = '<option value="' + course.id + '">' + course.name + '</option>';
        $('#sel_course_list').append(sel_content);
    });
    $('#sel_course_list').show(1000);
};

function formatModalWindowToSelectTournament(){
    var theUrl = '/rs/tournaments/search?';
    if (Number($("#day_of_event_hidden_value").val()) == 0){
        // user is playing today
        var d = new Date();
        var dd = d.getDay();
        var mm = Number(d.getMonth()) + 1;
        var yy = d.getFullYear();
        theUrl += '&day=' + dd;
        theUrl += '&month=' + mm;
        theUrl += '&year=' + yy;
    } else {
        theUrl += '&day=' + $("#day_of_event_hidden_value").val();
        theUrl += '&month=' + $("#month_of_event_hidden_value").val();
        theUrl += '&year=' + $("#year_of_event_hidden_value").val();
    }
    $.get(theUrl, function(data) {
        addTournamentsToSelectionWindow(data.tournaments);
    });
};
function addTournamentsToSelectionWindow(tournaments) {
    $('#sel_tournament_list').empty();
    var sel_content = '<option value="0">select a Tournament from the list</option>';
    $.each(tournaments, function (index, tournament) {
        sel_content += '<option value="' + tournament.id + '">' + tournament.title + '</option>';
    });
    $('#sel_tournament_list').append(sel_content);
    $('#sel_tournament_list').show();
};
/**
 *  checkDataToPlay()
 *  before initializing a new round this function checks
 *  if the user has selected the course.
 */
function checkDataToPlay() {
    if($('#courseid_hidden_value').val('').length > 0){
        initRound();
    }
};
/**
 *  initRound()
 *  Initialize server side a new round.
 *  The round ID is returned by the ReST service
 *  and stored in the persistent storage. This means that until
 *  the user creates a new round this will be the current round.
 */
function initRound() {
    var theUrl = '/rs/rounds/init?';
    theUrl += 'playerid=' + getPlayerId();
    theUrl += '&courseid=' + sessionStorage.getItem("courseid");
    theUrl += '&phcp=' + sessionStorage.getItem("phcp");
    if (Number($("#day_of_event_hidden_value").val()) > 0){
        theUrl += '&day=' + $("#day_of_event_hidden_value").val();
        theUrl += '&month=' + $("#month_of_event_hidden_value").val();
        theUrl += '&year=' + $("#year_of_event_hidden_value").val();
    }
    if ($("#tournamentid_hidden_value").val().length > 0){
        theUrl += '&tournamentid=' + $("#tournamentid_hidden_value").val();
    }
    $.ajax({
        url: theUrl,
        type: 'POST',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            localStorage.setItem("roundid", data.id);
            window.location.href = "hole.html";
        }
    });
};
/* --- END Play Round ---------------------- */

/**
 *
 *  All the functions used in the review tournament page
 *
 */

/**
 *  initializeTournamentReviewPage()
 *  When starting the page the app loads from server data related to the
 *  Tournament that must be displayed
 */
function initializeTournamentReviewPage() {
    var theUrl = '/rs/tournaments/tournament/' + getTournamentId();
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            $('#theTitle').html(data.title);
            var theDate = data.dayOfEvent.day + " ";
            theDate += getMonthName(Number(data.dayOfEvent.month));
            theDate += " " + data.dayOfEvent.year;
            $('#theDate').html(theDate);
            loadRoundsForLeaderboard();
        }
    });
};
/**
 *  loadRoundsForLeaderboard()
 *  When loading the page all rounds of the tournament must be loaded
 */
function loadRoundsForLeaderboard() {
    var theUrl = '/rs/rounds/leaderboard/' + getTournamentId();
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            createLeaderBoard(data.results);
        }
    });
};
function createLeaderBoard(results) {
    var tableContent = '<thead>';
    tableContent += '<tr>';
    tableContent += '<td class="t_tbl_th_center">#</td>';
    tableContent += '<td class="t_tbl_th_left">player</td>';
    tableContent += '<td class="t_tbl_th_left">course</td>';
    tableContent += '<td class="t_tbl_th_center">hcp</td>';
    tableContent += '<td  class="t_tbl_th_center">stb</td>';
    tableContent += '</tr>';
    tableContent += '</thead>';

    tableContent += '<tbody>';
    $.each(results, function (index, r) {
        tableContent += '<tr class="goto_view_round" data-id="' + r.roundid + '">';
        tableContent += '<td>' + (index + 1) + '</td>';
        tableContent += '<td class="t_tbl_td_left">' + r.playerName + '</td>';
        tableContent += '<td class="t_tbl_td_left">' + r.course + '</td>';
        tableContent += '<td>' + r.phcp + '</td>';
        tableContent += '<td>' + r.stb + '</td>';
        tableContent += '</tr>';
    });
    tableContent += '</tbody>';

    $("#tbl_tour").html(tableContent);
};
/* --- END Tournament Review ---------------------- */

/**
 *
 *  All the functions used in the hole by hole page
 *
 */

/**
 *  loadRoundData()
 *  When starting the page the app loads from server data related to the current round.
 *  Data include scorecard that will be filled with the app
 *  and then saved back to the server.
 */
function loadRoundDataToPlay() {
    var theUrl = '/rs/rounds/round/' + getRoundId();
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            window.roundData = data;
            formatFirstHole();
        }
    });
};
/**
 *  formatFirstHole()
 *  This is called only once, after round data are loaded.
 */
function formatFirstHole(){
    var additional_strokes = calculateAdditionalStrokes(window.roundData.scorecard.phcp, window.roundData.course.holes[1].hcp);
    var strokes_in_regulation = window.roundData.course.holes[1].par + additional_strokes;
    $("#hole_number").html(1);
    $("#hole_distance").html(window.roundData.course.holes[1].distance + "m");
    $("#hole_par").html("par " + window.roundData.course.holes[1].par);
    $("#additional_strokes").html(formattedAdditionalStrokes(additional_strokes));
    $("#strokes").html(strokes_in_regulation);
    $("#total_strokes").html(strokes_in_regulation);
};
function addPutt(){
    var puttsNum = parseInt($("#putts").html());
    $("#putts").html(puttsNum + 1);
};
function removePutt(){
    var puttsNum = parseInt($("#putts").html());
    if (puttsNum > 0){
        $("#putts").html(puttsNum - 1);
    }
};
function addStroke(){
    var currentHoleNumber = parseInt($("#hole_number").html());
    var additional_strokes = calculateAdditionalStrokes(window.roundData.scorecard.phcp, window.roundData.course.holes[currentHoleNumber].hcp);
    var oldStrokesNum = parseInt($("#strokes").html());
    var newStrokesNum = oldStrokesNum + 1;
    var mdl = parseInt($("#total_strokes").html());
    var stb = calculateStableford(newStrokesNum, window.roundData.course.holes[currentHoleNumber].par, additional_strokes);
    var points_already_saved = getTotalStableford((currentHoleNumber -1),window.roundData.scorecard);
    var prj = calculateProjection(currentHoleNumber, (points_already_saved + stb));
    $("#strokes").html(newStrokesNum);
    $("#total_strokes").html(mdl + 1);
    $("#total_stb").html(points_already_saved + stb);
    $("#stb_projection").html(prj);
};
function removeStroke(){
    var oldStrokesNum = parseInt($("#strokes").html());
    if (oldStrokesNum == 1){
        return;
    }
    var newStrokesNum = oldStrokesNum - 1;
    var currentHoleNumber = parseInt($("#hole_number").html());
    var additional_strokes = calculateAdditionalStrokes(window.roundData.scorecard.phcp, window.roundData.course.holes[currentHoleNumber].hcp);
    var mdl = parseInt($("#total_strokes").html());
    var stb = calculateStableford(newStrokesNum, window.roundData.course.holes[currentHoleNumber].par, additional_strokes);
    var points_already_saved = getTotalStableford((currentHoleNumber -1),window.roundData.scorecard);
    var prj = calculateProjection(currentHoleNumber, (points_already_saved + stb));
    $("#strokes").html(newStrokesNum);
    $("#total_strokes").html(mdl - 1);
    $("#total_stb").html(points_already_saved + stb);
    $("#stb_projection").html(prj);
};
function nextHole(){
    var currentHoleNumber = parseInt($("#hole_number").html());
    if (currentHoleNumber == 18){
        return;
    }
    saveHole(currentHoleNumber,(currentHoleNumber + 1));
};
function previousHole(){
    var currentHoleNumber = parseInt($("#hole_number").html());
    if (currentHoleNumber == 1){
        return;
    }
    saveHole(currentHoleNumber,(currentHoleNumber - 1));
};
function saveHole(currentHole, nextHole){
    var strokes = parseInt($("#strokes").html());
    var putts = parseInt($("#putts").html());
    var additional_strokes = calculateAdditionalStrokes(window.roundData.scorecard.phcp, window.roundData.course.holes[currentHole].hcp);
    var points = calculateStableford(strokes, window.roundData.course.holes[currentHole].par, additional_strokes);
    window.roundData.scorecard.scores[currentHole].strokes = strokes;
    window.roundData.scorecard.scores[currentHole].putts = putts;
    window.roundData.scorecard.scores[currentHole].points = points;
    formatHole(nextHole);
};
function save18thHole(){
    var strokes = parseInt($("#strokes").html());
    var putts = parseInt($("#putts").html());
    var additional_strokes = calculateAdditionalStrokes(window.roundData.scorecard.phcp, window.roundData.course.holes[18].hcp);
    var points = calculateStableford(strokes, window.roundData.course.holes[18].par, additional_strokes);
    window.roundData.scorecard.scores[18].strokes = strokes;
    window.roundData.scorecard.scores[18].putts = putts;
    window.roundData.scorecard.scores[18].points = points;
};
function formatHole(holeNumber){
    if(window.roundData.scorecard.scores[holeNumber].strokes == 0){
        formatHoleAsNew(holeNumber);
    } else {
        formatHoleFromScorecard(holeNumber);
    }
};
function formatHoleAsNew(holeNumber){
    var additional_strokes = calculateAdditionalStrokes(window.roundData.scorecard.phcp, window.roundData.course.holes[holeNumber].hcp);
    var strokes_in_regulation = window.roundData.course.holes[holeNumber].par + additional_strokes;
    var strokes_already_saved = getTotalStrokes((holeNumber -1),window.roundData.scorecard);
    var mdl = strokes_already_saved + strokes_in_regulation;
    var points_already_saved = getTotalStableford((holeNumber -1),window.roundData.scorecard);
    var stb = points_already_saved + 2;
    formatHolePage(holeNumber, additional_strokes, strokes_in_regulation, 2, mdl, stb);
};
function formatHoleFromScorecard(holeNumber){
    var additional_strokes = calculateAdditionalStrokes(window.roundData.scorecard.phcp, window.roundData.course.holes[holeNumber].hcp);
    var strokes_already_saved = getTotalStrokes((holeNumber -1),window.roundData.scorecard);
    var mdl = strokes_already_saved + window.roundData.scorecard.scores[holeNumber].strokes;
    var points_already_saved = getTotalStableford((holeNumber -1),window.roundData.scorecard);
    var stb = points_already_saved + window.roundData.scorecard.scores[holeNumber].points;
    formatHolePage(holeNumber, additional_strokes, window.roundData.scorecard.scores[holeNumber].strokes, window.roundData.scorecard.scores[holeNumber].putts, mdl, stb);
};
function formatHolePage(holeNumber, additional_strokes, strokes, putts, medal, stableford){
    $("#hole_number").html(holeNumber);
    $("#hole_distance").html(window.roundData.course.holes[holeNumber].distance + "m");
    $("#hole_par").html("par " + window.roundData.course.holes[holeNumber].par);
    $("#additional_strokes").html(formattedAdditionalStrokes(additional_strokes));
    $("#strokes").html(strokes);
    $("#putts").html(putts);
    $("#total_strokes").html(medal);
    $("#total_stb").html(stableford);
    $("#stb_projection").html(calculateProjection(holeNumber, stableford));
    if (holeNumber == 18) {
        //$("#save_scorecard").prop('disabled', false);
        $("#save_scorecard").removeClass("disabled");
    }
};
function saveRoundScorecard() {
    save18thHole();
    var theUrl = '/rs/rounds/scorecard/';
    theUrl += window.roundData.id;
    theUrl += '?phcp=' + window.roundData.scorecard.phcp;
    theUrl += '&scorecard=' + createScorecardString();
    $.ajax({
        url: theUrl,
        type: 'POST',
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            console.log("RESULT: " + data.status);
            window.location.href = 'round_review.html?roundid=' + window.roundData.id;
        }
    });
};
function createScorecardString() {
    var scorecardstring = '';
    for (i=1; i<=18; i++){
        scorecardstring += i + '_';
        scorecardstring += window.roundData.scorecard.scores[i].strokes + '_';
        scorecardstring += window.roundData.scorecard.scores[i].putts + '_';
        scorecardstring += window.roundData.scorecard.scores[i].points + '_';
        scorecardstring += window.roundData.scorecard.scores[i].bunkers + '_';
        scorecardstring += window.roundData.scorecard.scores[i].penalties + '_';
        scorecardstring += window.roundData.scorecard.scores[i].fareway + '-';
    }
    return scorecardstring;
};
/* --- END Hole by Hole ---------------------- */

/**
 *
 *  All the functions used in the Review Round page
 *
 */
function formatReviewRoundHelloPar(playername) {
    $('#hello_par').html("Hello " + playername);
};
function loadRoundDataForReview() {
    var theUrl = '/rs/rounds/round/' + getRoundId();
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            window.roundData = data;
            formatRound();
        }
    });
};
/**
 *  formatRound()
 *  This is called only once, after round data are loaded.
 */
function formatRound(){
    calculateResults();

    var info = 'Player: <strong>' + window.roundData.playerName + '</strong>' + ' (hcp ' + window.roundData.scorecard.phcp + ')';
    info += ' date of event ' + window.roundData.dayOfEvent.day + ' ' + getMonthName(window.roundData.dayOfEvent.month) + ' ' + window.roundData.dayOfEvent.year;
    info += '<br> at Course: <span style="color: #A30000">' + window.roundData.course.name + '</span>';
    $("#playerInfo").html(info);

    var tableHeader = '<thead>';
    tableHeader += '<tr class="round_tbl_hrow">';
    tableHeader += '<td>&nbsp;</td>';
    tableHeader += '<td>1</td>';
    tableHeader += '<td>2</td>';
    tableHeader += '<td>3</td>';
    tableHeader += '<td>4</td>';
    tableHeader += '<td>5</td>';
    tableHeader += '<td>6</td>';
    tableHeader += '<td>7</td>';
    tableHeader += '<td>8</td>';
    tableHeader += '<td>9</td>';
    tableHeader += '<td>out</td>';
    tableHeader += '<td>10</td>';
    tableHeader += '<td>11</td>';
    tableHeader += '<td>12</td>';
    tableHeader += '<td>13</td>';
    tableHeader += '<td>14</td>';
    tableHeader += '<td>15</td>';
    tableHeader += '<td>16</td>';
    tableHeader += '<td>17</td>';
    tableHeader += '<td>18</td>';
    tableHeader += '<td>in</td>';
    tableHeader += '<td>&nbsp;</td>';
    tableHeader += '</tr>';
    tableHeader += '<tr>';
    tableHeader += '<td class="round_tbl_row_title">par</td>';
    tableHeader += '<td>' + window.roundData.course.holes[1].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[2].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[3].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[4].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[5].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[6].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[7].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[8].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[9].par + '</td>';
    tableHeader += '<td class="round_tbl_sum">' + window.results.par_out + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[10].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[11].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[12].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[13].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[14].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[15].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[16].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[17].par + '</td>';
    tableHeader += '<td>' + window.roundData.course.holes[18].par + '</td>';
    tableHeader += '<td class="round_tbl_sum">' + window.results.par_in + '</td>';
    tableHeader += '<td class="round_tbl_sum">' + (window.results.par_out + window.results.par_in) + '</td>';
    tableHeader += '</tr>';
    tableHeader += '</thead>';

    tableHeader += '<tbody>';
    tableHeader += '<tr>';
    tableHeader += '<td class="round_tbl_row_title">strokes</td>';
    tableHeader += formatStrokeTableCell(1);
    tableHeader += formatStrokeTableCell(2);
    tableHeader += formatStrokeTableCell(3);
    tableHeader += formatStrokeTableCell(4);
    tableHeader += formatStrokeTableCell(5);
    tableHeader += formatStrokeTableCell(6);
    tableHeader += formatStrokeTableCell(7);
    tableHeader += formatStrokeTableCell(8);
    tableHeader += formatStrokeTableCell(9);
    tableHeader += '<td class="round_tbl_sum">' + window.results.str_out + '</td>';
    tableHeader += formatStrokeTableCell(10);
    tableHeader += formatStrokeTableCell(11);
    tableHeader += formatStrokeTableCell(12);
    tableHeader += formatStrokeTableCell(13);
    tableHeader += formatStrokeTableCell(14);
    tableHeader += formatStrokeTableCell(15);
    tableHeader += formatStrokeTableCell(16);
    tableHeader += formatStrokeTableCell(17);
    tableHeader += formatStrokeTableCell(18);
    tableHeader += '<td class="round_tbl_sum">' + window.results.str_in + '</td>';
    tableHeader += '<td class="round_tbl_sum">' + (window.results.str_in + window.results.str_out) + '</td>';
    tableHeader += '</tr>';
    tableHeader += '<tr>';
    tableHeader += '<td class="round_tbl_row_title">putts</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[1].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[2].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[3].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[4].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[5].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[6].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[7].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[8].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[9].putts + '</td>';
    tableHeader += '<td class="round_tbl_sum">' + window.results.put_out + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[10].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[11].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[12].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[13].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[14].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[15].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[16].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[17].putts + '</td>';
    tableHeader += '<td>' + window.roundData.scorecard.scores[18].putts + '</td>';
    tableHeader += '<td class="round_tbl_sum">' + window.results.put_in + '</td>';
    tableHeader += '<td class="round_tbl_sum">' + (window.results.put_in + window.results.put_out) + '</td>';
    tableHeader += '</tr>';
    tableHeader += '<tr>';
    tableHeader += '<td class="round_tbl_row_title">STB</td>';
    tableHeader += formatStbTableCell(1);
    tableHeader += formatStbTableCell(2);
    tableHeader += formatStbTableCell(3);
    tableHeader += formatStbTableCell(4);
    tableHeader += formatStbTableCell(5);
    tableHeader += formatStbTableCell(6);
    tableHeader += formatStbTableCell(7);
    tableHeader += formatStbTableCell(8);
    tableHeader += formatStbTableCell(9);
    tableHeader += '<td class="round_tbl_sum">' + window.results.stb_out + '</td>';
    tableHeader += formatStbTableCell(10);
    tableHeader += formatStbTableCell(11);
    tableHeader += formatStbTableCell(12);
    tableHeader += formatStbTableCell(13);
    tableHeader += formatStbTableCell(14);
    tableHeader += formatStbTableCell(15);
    tableHeader += formatStbTableCell(16);
    tableHeader += formatStbTableCell(17);
    tableHeader += formatStbTableCell(18);
    tableHeader += '<td class="round_tbl_sum">' + window.results.stb_in + '</td>';
    tableHeader += '<td class="round_tbl_sum">' + (window.results.stb_out + window.results.stb_in) + '</td>';
    tableHeader += '</tr>';
    tableHeader += '</tbody>';

    $("#tbl_round").html(tableHeader);

};
/**
 *  some util to calculate & format results
 *  This is called only once, after round data are loaded.
 */
function calculateResults(){
    var par_in = 0;
    var par_out = 0;
    var str_in = 0;
    var str_out = 0;
    var put_in = 0;
    var put_out = 0;
    var stb_in = 0;
    var stb_out = 0;
    for (i=1; i<=9; i++){
        par_out += window.roundData.course.holes[i].par;
        str_out += window.roundData.scorecard.scores[i].strokes;
        put_out += window.roundData.scorecard.scores[i].putts;
        stb_out += window.roundData.scorecard.scores[i].points;
    }
    for (i=10; i<=18; i++){
        par_in += window.roundData.course.holes[i].par;
        str_in += window.roundData.scorecard.scores[i].strokes;
        put_in += window.roundData.scorecard.scores[i].putts;
        stb_in += window.roundData.scorecard.scores[i].points;
    }
    window.results.par_in = par_in;
    window.results.str_in = str_in;
    window.results.put_in = put_in;
    window.results.stb_in = stb_in;
    window.results.par_out = par_out;
    window.results.str_out = str_out;
    window.results.put_out = put_out;
    window.results.stb_out = stb_out;
};
function formatStrokeTableCell(holeNumber){
    var diff = window.roundData.scorecard.scores[holeNumber].strokes - window.roundData.course.holes[holeNumber].par;
    if (diff > 1){
        return '<td class="round_tbl_td_dbg">' + window.roundData.scorecard.scores[holeNumber].strokes + '</td>';
    }
    if (diff == 1){
        return '<td class="round_tbl_td_bog">' + window.roundData.scorecard.scores[holeNumber].strokes + '</td>';
    }
    if (window.roundData.scorecard.scores[holeNumber].strokes < window.roundData.course.holes[holeNumber].par){
        return '<td class="round_tbl_td_brd">' + window.roundData.scorecard.scores[holeNumber].strokes + '</td>';
    }
    return '<td class="round_tbl_td_par">' + window.roundData.scorecard.scores[holeNumber].strokes + '</td>';
};
function formatStbTableCell(holeNumber){
    var td = '<td>';
    if (window.roundData.scorecard.scores[holeNumber].points > 0){
        td += window.roundData.scorecard.scores[holeNumber].points;
    } else {
        td += 'X';
    }
    td += '</td>';
    return td;
};
/**
 *
 *  All the functions used in the review rounds section
 *
 */
function getReviewPlayer() {
    var playerid = sessionStorage.getItem("playerid");
    if ((playerid == null) || (playerid.length == 0)){
        playerid = localStorage.getItem("playerid");
        sessionStorage.setItem("playerid", playerid);
    }
    getPlayerName(playerid, formatReviewRoundPlayerInfo);
};
function formatReviewRoundPlayerInfo(playername) {
    $("#pname").html(playername);
};
/* --- END Review Round ---------------------- */

/**
 *
 *  All the functions used to format the round list page
 *
 */
function getRoundListHeaderInfo() {
    var yearToSearch = getUrlParameter("year");
    if ((yearToSearch == null)||(yearToSearch.length == 0)){
        yearToSearch = 2018;
    }
    var monthToSearch = getUrlParameter("month");
    if (monthToSearch == null){
        monthToSearch = 0;
    }
    formatRoundListHeaderInfo(yearToSearch, monthToSearch);
};
function formatRoundListHeaderInfo(theYear, theMonth) {
    $("#roundListPlayerName").html(sessionStorage.getItem("playerName"));
    $("#roundListYear").html(theYear);
    if (Number(theMonth) == 0){
        $("#roundListMonth").html('all months');
    } else {
        $("#roundListMonth").html(getMonthName(Number(theMonth)));
    }
    loadRoundsData(theYear,theMonth);
};
function loadRoundsData(yy,mm) {
    var theUrl = '/rs/rounds/list?playerid=' + sessionStorage.getItem("playerid");
    theUrl += '&year=' + yy;
    if (Number(mm) > 0){
        theUrl += '&month=' + mm;
    }
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            formatRoundListTable(data.results);
        }
    });
};
function formatRoundListTable(rounds) {
    var tableContent = '<thead>';
    tableContent += '<tr><th>Date</th><th>Course</th><th>pHcp</th><th>MDL</th><th>STB</th></tr>';
    tableContent += '</thead>';
    tableContent += '<tbody>';
    $.each(rounds, function (index, round) {
        tableContent += '<tr class="goto_view_round" data-id="' + round.id + '">';
        tableContent += '<td>' + round.dayOfEvent + '</td>';
        tableContent += '<td>' + round.course + '</td>';
        tableContent += '<td>' + round.phcp + '</td>';
        tableContent += '<td>' + round.mdl + '</td>';
        tableContent += '<td>' + round.stb + '</td>';
        tableContent += '</tr>';
    });
    tableContent += '</tbody>';
    $("#tbl_round_list").html(tableContent);
};
/* --- END Review Round ---------------------- */
/* --- Tournament List Page ---------------------- */
function initTListPage() {
    var yearToSearch = getUrlParameter("year");
    if ((yearToSearch == null)||(yearToSearch.length == 0)){
        yearToSearch = 2018;
    }
    var monthToSearch = getUrlParameter("month");
    if (monthToSearch == null){
        monthToSearch = 0;
    }
    $("#tListYear").html(yearToSearch);
    if (Number(monthToSearch) == 0){
        $("#tListMonth").html('all months');    //var theUrl = '/rs/rounds/search?playerid=andrea.leoncini';

    } else {
        $("#tListMonth").html(getMonthName(Number(monthToSearch)));
    }
    loadTournamentsData(yearToSearch, monthToSearch);
};
function loadTournamentsData(yy,mm) {
    var theUrl = '/rs/tournaments/search?';
    theUrl += 'year=' + yy;
    if (Number(mm) > 0){
        theUrl += '&month=' + mm;
    }
    $.get(theUrl, function(data) {
        formatTListTable(data.tournaments);
    });
};
function formatTListTable(tournaments) {
    var tableContent = '<thead>';
    tableContent += '<tr><th>Date</th><th>Title</th></tr>';
    tableContent += '</thead>';
    tableContent += '<tbody>';
    $.each(tournaments, function (index, t) {
        tableContent += '<tr class="goto_view_tournament" data-id="' + t.id + '">';
        tableContent += '<td>' + t.dayOfEvent.day + ' ' + getMonthName(Number(t.dayOfEvent.month)) + ' ' + t.dayOfEvent.year + '</td>';
        tableContent += '<td>' + t.title + '</td>';
        tableContent += '</tr>';
    });
    tableContent += '</tbody>';
    $("#tbl_t_list").html(tableContent);
};
/* --- END Tournament List Page ------------------ */
/* --- Edit Round Page --------------------------- */
function initEditRoundPage() {
    var theUrl = '/rs/rounds/round/' + getRoundId();
    $.ajax({
        url: theUrl,
        type: 'GET',
        data: {},
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            window.roundData = data;
            $("#plname").html(window.roundData.playerName);
            $("#plhcp").text(window.roundData.scorecard.phcp);
            $("#plhcp").val(window.roundData.scorecard.phcp);
            $("#pldate").html((window.roundData.dayOfEvent.day + '.' + window.roundData.dayOfEvent.month + '.' + window.roundData.dayOfEvent.year));
            $("#cname").html(window.roundData.course.name);
            $("#day_of_event_hidden_value").val(window.roundData.dayOfEvent.day);
            $("#month_of_event_hidden_value").val(window.roundData.dayOfEvent.month);
            $("#year_of_event_hidden_value").val(window.roundData.dayOfEvent.year);
        }
    });

};
function deleteRound() {
    var theUrl = '/rs/rounds/remove/' + getRoundId();
    $.ajax({
        url: theUrl,
        type: 'POST',
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            console.log("RESULT: " + data.status);
            if (data.status == 'success'){
                $("#changes_saved").fadeIn();
                setTimeout(function() {
                    $("#changes_saved").fadeOut();
                    setTimeout(function() {
                        window.location.href = "review.html";
                    }, 500);
                }, 2000);
            } else {
                $("#error_while_saving").fadeIn();
                setTimeout(function() {
                    $("#error_while_saving").fadeOut();
                }, 2000);
            }
        }
    });
};
function saveRoundInfoOnly() {
    updateRoundInfo(false);
};
function saveRoundInfo() {
    updateRoundInfo(true);
};
function updateRoundInfo(edit) {
    var phcp = Number($("#plhcp").val());

    if ((phcp < 0) || (phcp > 36)){
        $("#phcp_warning").fadeIn();
        setTimeout(function() {
            $("#phcp_warning").fadeOut();
        }, 2000);
        return;
    }

    var theUrl = '/rs/rounds/updinfo/' + getRoundId();
    theUrl += '?phcp=' + phcp;
    theUrl += '&day=' + $("#day_of_event_hidden_value").val();
    theUrl += '&month=' + $("#month_of_event_hidden_value").val();
    theUrl += '&year=' + $("#year_of_event_hidden_value").val();
    console.log("URL: " + theUrl);
    $.ajax({
        url: theUrl,
        type: 'POST',
        dataType: 'json',
        complete: function(response, status, xhr){
            var data = jQuery.parseJSON(response.responseText);
            console.log("RESULT: " + data.status);
            if (data.status == 'success'){
                $("#changes_saved").fadeIn();
                setTimeout(function() {
                    $("#changes_saved").fadeOut();
                    setTimeout(function() {
                        if (edit) {
                            window.location.href = "hole.html?roundid=" + window.roundData.id;
                        } else {
                            window.location.href = "round_review.html?roundid=" + window.roundData.id;
                        }
                    }, 500);
                }, 2000);
            } else {
                $("#error_while_saving").fadeIn();
                setTimeout(function() {
                    $("#error_while_saving").fadeOut();
                }, 2000);
            }
        }
    });
};
/* --- END Edit Round Page ----------------------- */
