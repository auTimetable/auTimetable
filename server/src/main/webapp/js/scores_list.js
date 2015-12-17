function deleteScores(scoresFullKey, groupNumber, subgroupNumber) {
    var path="/delete_scores?scores_full_key=" + scoresFullKey +
            "&group_number=" + groupNumber +
            "&subgroup_number=" + subgroupNumber;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', path, true);
    xhr.send();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            location.reload();
        }
    }
}
