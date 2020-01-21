
var modelList;
var processList;
var selectedId = null;
var selectedProcessId = "";
var selectedActivityId = "";

function Model(id, name, maxBatchSize, minBatchSize, timeLimit, time, order, process, processId, activity, activityId, attributes){
    "use strict";
    this.id = id;
    this.name = name;
    this.maxBatchSize = maxBatchSize;
    this.minBatchSize = minBatchSize;
    this.timeLimit = timeLimit;
    this.time = time;
    this.order = order;
    this.process = process;
    this.processId = processId;
    this.activity = activity;
    this.activityId = activityId;
    this.attributes = attributes;
}

function Process(id, name, activityList){
    "use strict";
    this.id = id;
    this.name = name;
    this.activities = new Map();
    for(var i in activityList){
        this.activities.set(activityList[i].id, activityList[i].name);
    }
}

window.onload = function(){
    "use strict";	
    modelList = [];
    processList = [];
    requestModels();
    requestProcesses();
};

function requestProcesses(){

    var request = new XMLHttpRequest();
    request.open("GET", "http://localhost:8080/engine-rest/process-definition/activities", true);
    request.send(null);
    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 200) {
            var json = JSON.parse(request.responseText);
            populateLists(json);
        }
        request.onerror = function (e) {
            alert("error: " +request.status);
        };
    };

}

function requestModels(){

    var request = new XMLHttpRequest();
    request.open("GET", "http://localhost:9000/", true);
    request.send(null);
    request.onreadystatechange = function() {
        if (request.readyState === 4 && request.status === 200) {
            var json = JSON.parse(request.responseText);
            var i;
            for(i in json){
                var model = new Model(json[i].id, json[i].name, json[i].maxBatchSize, json[i].minBatchSize, json[i].timeLimit,
                    json[i].time, json[i].order, json[i].process, json[i].processId, json[i].activity, json[i].activityId, json[i].attributes);
                addToModelList(model);
                modelList.push(model);
            }
        }
        request.onerror = function (e) {
            alert("error: " +request.status);
        };
    };

}

function populateLists(json){
    "use strict";
    for(var i in json){
        var process = new Process(json[i].id, json[i].name, json[i].activityList);
        processList.push(process);
        var parts = json[i].id.split(":");
        var version = parts[1];
        addToProcessList(json[i].name + " v" + version, json[i].id);

    }

}

function addToModelList(model) {
    "use strict";
    var list = document.getElementById("batch-model-list");
    var element = document.createElement("a");
    element.appendChild(document.createTextNode(model.name));
    element.setAttribute("class", "list-group-item list-group-item-danger");
    element.setAttribute("href", "#");
    element.setAttribute("id", model.id);
    element.onclick = function() {selectedModel(this.id);};
    list.appendChild(element);
}

function addToProcessList(processName, processId) {
    "use strict";
    var list = document.getElementById("processList");
    var element = document.createElement("option");
    element.appendChild(document.createTextNode(processName));
    element.setAttribute("id", processId);
    list.appendChild(element);
}

function addToActivityList(activityName, activityId){
    "use strict";
    var list = document.getElementById("activityList");
    var element = document.createElement("option");
    element.appendChild(document.createTextNode(activityName));
    element.setAttribute("id", activityId);
    list.appendChild(element);
}

function addToBlockedProcesses(id, process, activity){
    "use strict";
    var list = document.getElementById("blockedProcessList");
    var element = document.createElement("a");
    element.setAttribute("class", "list-group-item list-group-item-danger");
    element.setAttribute("href", "#");
    element.setAttribute("id", id);
    var row = document.createElement("div");
    row.setAttribute("class", "row");
    var col1 = document.createElement("div");
    col1.setAttribute("class", "col-lg-3 col-md-3 col-sm-3 col-xs-3");
    col1.appendChild(document.createTextNode(id));
    var col2 = document.createElement("div");
    col2.setAttribute("class", "col-lg-3 col-md-3 col-sm-3 col-xs-3 float-right");
    col2.appendChild(document.createTextNode(process));
    var col3 = document.createElement("div");
    col3.setAttribute("class", "col-lg-3 col-md-3 col-sm-3 col-xs-3 float-right");
    col3.appendChild(document.createTextNode(activity));
    var col4 = document.createElement("div");
    col4.setAttribute("class", "col-lg-3 col-md-3 col-sm-3 col-xs-3");
    var span = document.createElement("span");
    span.setAttribute("class", "d-inline-block float-right");
    var button = document.createElement("button");
    button.setAttribute("type", "button");
    button.innerHTML = "Resume";
    button.setAttribute("class", "btn btn-danger");
	button.onclick = function() {resumeProcess(element.id);};
    span.appendChild(button);
    col4.appendChild(span);
    row.appendChild(col1);
    row.appendChild(col2);
    row.appendChild(col3);
    row.appendChild(col4);
    element.appendChild(row);
    list.appendChild(element);
}

function selectedModel(selectedModelId){

    //alert(selectedModelId);
    selectedId = selectedModelId;
    var model = modelList.find(x => x.id.toString() === selectedModelId);
    var name = document.getElementById("name");
    var maxSize = document.getElementById("maxSize");
    var minBatchSize = document.getElementById("rule1");
    var timeLimit = document.getElementById("rule2");
    name.value = model.name;
    maxSize.value = model.maxBatchSize;
    minBatchSize.value = model.minBatchSize;
    timeLimit.value = model.timeLimit;
    var idx = document.getElementById("rule3").options.namedItem(model.time.toLowerCase()).index;
    document.getElementById("rule3").selectedIndex = idx;
    idx = document.getElementById("order").options.namedItem(model.order.toLowerCase()).index;
    document.getElementById("order").selectedIndex = idx;
    idx = document.getElementById("processList").options.namedItem(model.processId);
    if(idx != null){
        document.getElementById("processList").selectedIndex = idx.index;
        selectedProcess(idx.id);
        selectedProcessId = idx.value;
    }
    else
        document.getElementById("processList").selectedIndex = "0";
    idx = document.getElementById("activityList").options.namedItem(model.activityId);
    if(idx != null){
        document.getElementById("activityList").selectedIndex = idx.index;
        selectedActivityId = idx.value;
    }
    else
        document.getElementById("activityList").selectedIndex = "0";

    document.getElementById("attributes").value = model.attributes;
}

function selectedProcess(processId){
    var list = document.getElementById("processList");
    if(selectedProcessId !== processId){
        selectedProcessId = processId;
        document.getElementById("activityList").options.length = 1;
        var selectedProcess = processList.find(x => x.id.toString() === processId);
        selectedProcess.activities.forEach(function(key, value){
            addToActivityList(key, value);
        });
    }
}

function selectedActivity(activityId){
    selectedActivityId = activityId;
}

function save(){
    var model = prepareJsonRequest();
    var xhr;
    if(selectedId == null){
        var json = JSON.stringify(model);
        xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:9000/create/"+json, true);
        xhr.onload = function (e) {
            if (xhr.status === 200) {
                modelList.push(model);
                addToModelList(model);
                clearPropertyFields();
				alert(xhr.responseText);
            }
            else {
                alert(xhr.responseText);
            }
        };
        xhr.onerror = function (e) {
            alert(xhr.responseText);
        };
        xhr.send(null);
    }
    else{
        xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:9000/update/"+model, true);
        xhr.onload = function (e) {
            if (xhr.readyState === 4 && xhr.status === 200) {
                document.getElementById(selectedId).innerHTML = JSON.parse(model).name;
                clearPropertyFields();
                alert(xhr.responseText);
            }
            else {
                alert(xhr.responseText);
            }
        }
        xhr.onerror = function (e) {
            alert(xhr.responseText);
        };
        xhr.send(null);
    }
}

function deleteModel(){
    if(selectedId !== null){
        if (confirm("Delete this model?")) {
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:9000/delete/"+selectedId, true);
            xhr.onload = function (e) {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var item = document.getElementById(selectedId);
                    item.parentNode.removeChild(item);
                    var idx = modelList.indexOf(modelList.find(x => x.id === selectedId));
                    delete modelList[idx];
                    clearPropertyFields();
                    alert(xhr.responseText);
                }

                else {
                    alert("Server error");
                }
            }
            xhr.onerror = function (e) {
                alert("Could not reach server");
            };
            xhr.send(null);
        }
        }
    else{
        alert("No model is selected!");
    }
}

function clearPropertyFields(){
    selectedId = null;
    selectedProcessId = "";
    selectedActivityId = "";
    var elements = document.getElementsByClassName("model-property-text");
    var i;
    for(i=0; i<elements.length; i++){
        elements[i].value = "";
    }
    document.getElementById("rule3").selectedIndex = "0";
    document.getElementById("order").selectedIndex = "0";
    document.getElementById("processList").selectedIndex = "0";
    document.getElementById("activityList").selectedIndex = "0";
}

function prepareJsonRequest(){

    var name = document.getElementById("name").value;
    var maxSize = document.getElementById("maxSize").value;
    var minBatchSize = document.getElementById("rule1").value;
    var timeLimit = document.getElementById("rule2").value;
    var time = document.getElementById("rule3").value;
    var order = document.getElementById("order").value.toUpperCase();
    var drop = document.getElementById("processList");
    var process = drop[drop.selectedIndex].value;
    var processId = drop[drop.selectedIndex].id;
    drop = document.getElementById("activityList");
    var activity =  drop[drop.selectedIndex].value;
    var activityId = drop[drop.selectedIndex].id;
    var attributes = document.getElementById("attributes").value;
	var id = process+":"+activity;
	if(attributes !== ""){
		  id = id+":"+attributes;
		}
    var model;

    if(selectedId != null){
        model = modelList.find(x => x.id === selectedId);
        model.name = name;
        model.maxBatchSize = maxSize;
        model.minBatchSize = minBatchSize;
        model.timeLimit = timeLimit;
        model.time = time;
        model.order = order;
        model.process = process;
        model.processId = processId;
        model.activity = activity;
        model.activityId = activityId;
        model.attributes = attributes;		

        return JSON.stringify(model);
    }
    else{
        model = new Model(id, name,maxSize,minBatchSize,timeLimit,time,order,process, processId, activity, activityId, attributes);
        return model;
    }
}

function resumeProcess(processId){

    var request = new XMLHttpRequest();
    request.open("POST", "http://localhost:9000/resume/"+processId, true);
    request.onload = function() {
        if (request.status === 200 && request.status <= 299) {
            var process = document.getElementById(processId);
            process.parentNode.removeChild(process);
			alert(request.responseText);
        }
        else{
		    var process = document.getElementById(processId);
            process.parentNode.removeChild(process);
            alert(request.responseText);
        }
    }
	request.onerror = function (e) {
            alert("error: " +request.status);
    };
	request.send(null);

}

var pollDataFunc = setInterval(function(){

    var request = new XMLHttpRequest();
    request.open("GET", "http://localhost:9000/get-blocked-processes", true);
    request.send(null);
    request.onreadystatechange = function() {
        if(request.readyState === 4 && request.status === 200){
            var json = JSON.parse(request.responseText);
            if(json !== ""){
                for(var i in json){
				if(json[i].parentId !== null){
				   addToBlockedProcesses(json[i].parentId, json[i].processDefinition, json[i].activity);
				}
				else{
				   addToBlockedProcesses(json[i].processInstanceId, json[i].processDefinition, json[i].activity);
				}
                }
            }
            else{
                console.log(request.responseText);
            }
        }
        request.onerror = function (e) {
            console.log("error: " +request.status);
        };
    }

}, 2000);
