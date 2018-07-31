var HOST = "http://localhost:8080/api/fs/"
var dir;
function getXhr(){
	var XHR = ("onload" in new XMLHttpRequest()) ? XMLHttpRequest : XDomainRequest;

	var xhr = new XHR();

	return xhr;
}

window.onload = function() {
   openPackage("");
}


function openPackage(uri){
	document.getElementById("answer").innerHTML="openPackage...";

	var xhr = getXhr();


	document.getElementById("textAddPath").innerHTML = uri;


	xhr.open("GET", HOST + uri, true);
	xhr.addEventListener("load", function() {
		dir = uri;
	    //document.getElementById("answer").innerHTML = xhr.responseText;
        //console.log(xhr.responseText);
		document.getElementById("answer").innerHTML = "";
		document.getElementById("manager").innerHTML = "";
		document.getElementById("textFile").style.display = "none";
		var json = JSON.parse(xhr.responseText);
		if(json.status){
		   createPackage(json.answer);
		}else{
		    document.getElementById("answer").innerHTML = xhr.responseText;
		}

	});
	xhr.send();
}

function openFile(uri){
	document.getElementById("answer").innerHTML="openFile...";
	var xhr = getXhr();
	document.getElementById("textAddPath").innerHTML = uri;

	xhr.open("GET", HOST + uri, true);
	xhr.addEventListener("load", function() {
        document.getElementById("answer").innerHTML = "";
		//document.getElementById("manager").innerHTML = "";
		document.getElementById("textFile").innerHTML = "";
	    var json = JSON.parse(xhr.responseText);
		if(json.status){
		    document.getElementById("textFile").innerHTML = json.answer.text;
		    document.getElementById("textFile").style.display = "block";
		}else{
		    document.getElementById("answer").innerHTML = json;
		}
	});
	xhr.send();
}

function deleteFile(uri){
	document.getElementById("answer").innerHTML="deleteFile...";
    var xhr = getXhr();
    document.getElementById("textAddPath").innerHTML = uri;

    xhr.open("DELETE", HOST + uri, true);
    xhr.addEventListener("load", function() {
        document.getElementById("answer").innerHTML = xhr.status;
		openPackage(dir);
    });
    xhr.send();
}

function moveFile(uri, path){
	document.getElementById("answer").innerHTML="moveFile...";
    var xhr = getXhr();
    document.getElementById("textAddPath").innerHTML = uri;
    var body = 'newName=' + encodeURIComponent(path);

    xhr.open("POST", HOST + "move/" + uri + "?" + body, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.addEventListener("load", function() {
        document.getElementById("answer").innerHTML = xhr.responseText;
		openPackage(dir);
    });
    xhr.send();
}

function copyFile(uri, path){
	document.getElementById("answer").innerHTML="copyFile...";
    var xhr = getXhr();
    document.getElementById("textAddPath").innerHTML = uri;
    var body = 'newName=' + encodeURIComponent(path);

    xhr.open("POST", HOST + "copy/" + uri + "?" + body, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.addEventListener("load", function() {
        document.getElementById("answer").innerHTML = xhr.responseText;
		openPackage(dir);
    });
    xhr.send();
}

function createPackage(answer){
	var isMainDir = new Boolean(answer.isMainDir);

    if(isMainDir == true){
    	document.getElementById("back").style.display = "none";

    }else{

        document.getElementById("back").style.display = "inline-block";
        document.getElementById("back").onclick = function(){
            openPackage(answer.fatherDir);
        }
    }
    for (ind in answer.array) {
        obj = answer.array[ind];
        createFileElem(obj);
    }
}

function createFileElem(obj){
    var newDiv = document.createElement('div');
    newDiv.className = obj.type;

    newDiv.innerHTML = "<img class = \"ico\" src=\"" + "image/" + obj.type + ".png" + "\" alt=\"package\">"
    newDiv.innerHTML += "<div class = 'name'>" + obj.name + "</div>";


    var copy = document.createElement('button');
    copy.className = "copy";
    copy.innerHTML = "copy";
    copy.onclick = function(){
        var ans = prompt('Введите новый путь/имя ' + obj.name);
        if (ans) {
            copyFile(obj.fullDirectory,ans);
            console.log("copy");
        }
    };

    var move = document.createElement('button');
    move.className = "move";
    move.innerHTML = "move";
	move.onclick = function(){
        var ans = prompt('Введите новый путь/имя ' + obj.name);
        if (ans) {
            moveFile(obj.fullDirectory,ans);
			console.log("move");
        }
	};

    var del = document.createElement('button');
    del.className = "delete";
    del.innerHTML = "delete";
	del.onclick = function(){
		var ans = confirm('Вы действительно хотите удалить ' + obj.name +'?');
		if (ans) {
			deleteFile(obj.fullDirectory);
			console.log("delete");
		};

	};

	newDiv.appendChild(copy);
	newDiv.appendChild(move);
    newDiv.appendChild(del);

    //
    if(obj.type == "package")
        newDiv.addEventListener("dblclick", function(){openPackage(obj.fullDirectory);});
    else if(obj.type == "file")
        newDiv.addEventListener("dblclick",function(){openFile(obj.fullDirectory)});

    document.getElementById("manager").appendChild(newDiv);

}