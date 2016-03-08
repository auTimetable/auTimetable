var classesCounter = 0;

var idsToChange = [
    "class_form",
    "start_hour_label",
    "start_hour",
    "start_minute",
    "end_hour_label",
    "end_hour",
    "end_minute",
    "subject_label",
    "subject",
    "type_label",
    "type",
    "classroom_label",
    "classroom",
    "teacher_label",
    "teacher",
    "delete_button"
]

var namesToChange = [
    "start_hour",
    "start_minute",
    "end_hour",
    "end_minute",
    "subject",
    "type",
    "classroom",
    "teacher"
]


function changeNames(prefix, suffix) {
    for (var i = 0; i < namesToChange.length; i++) {
        var input = document.getElementById(namesToChange[i] + suffix);
        input.name = prefix + namesToChange[i];
    }
}

function changeIds(oldNumber, newNumber) {
    for (var i = 0; i < idsToChange.length; i++) {
        var element = document.getElementById(idsToChange[i] + oldNumber);
        element.id = idsToChange[i] + newNumber;

        if (i < idsToChange.length - 1 && element.getAttribute("for") == idsToChange[i + 1] + oldNumber) {
            element.setAttribute("for", idsToChange[i + 1] + newNumber);
        }
    }
}

function addClass(parity, dayName) {
    var divId = parity + "_" + dayName + "_classes";

    changeNames(parity + "_", "");
    changeIds("", ++classesCounter);

    var divToExpand = document.getElementById(divId);
    var classFormTemplate = document.getElementById("hidden-template").innerHTML;
    var classNode = document.createElement('div');

    changeCounter(parity + "_" + dayName, 1);

    changeIds(classesCounter, '');
    changeNames("", "")

    classNode.innerHTML = classFormTemplate;
    divToExpand.appendChild(classNode);

    var button = document.getElementById("delete_button" + classesCounter);
    var currentValue = "" + classesCounter;
    button.onclick = function() {
            removeClass("class_form" + currentValue);
            changeCounter(parity + "_" + dayName, -1);
//            document.getElementById(parity + "_" + dayName + "_counter").value -= 1;
    };
}

function removeClass(divId) {
    (elem = document.getElementById(divId)).parentNode.removeChild(elem);
}

function changeCounter(dayName, d) {
    var value = parseInt(document.getElementById(dayName + "_counter").value, 10);
    value = isNaN(value) ? 0 : value;
    value += d;
    document.getElementById(dayName + "_counter").value = value;
}
