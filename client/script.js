var HOST = "http://localhost:8080/"

function getXhr(){
	var XHR = ("onload" in new XMLHttpRequest()) ? XMLHttpRequest : XDomainRequest;

	var xhr = new XHR();

	return xhr;
}

window.onload = function() {
   document.getElementById("1re").onclick = authorization;
   document.getElementById("2re").onclick = function(){openPackage(document.getElementById("addPath").value);};
   document.getElementById("3re").onclick = function(){openFile(document.getElementById("addPath").value);};
}

function authorization(){
	document.getElementById("answer").innerHTML="log";

	var xhr = getXhr();

	var event = {
		type : "auto",
		login: "login",
		password : "password"
	};

	var str = JSON.stringify(event);


	xhr.open("GET",HOST+"123?"+str,true);
	xhr.addEventListener("load", function() {
		document.getElementById("answer").innerHTML=xhr.responseText;
	});
	xhr.send();
}

function openPackage(uri){
	document.getElementById("answer").innerHTML="openPackage";

	var xhr = getXhr();

	var event = {
		type : "getPackage",
		path: uri
	};
	document.getElementById("textAddPath").innerHTML = uri;

	var str = JSON.stringify(event);


	xhr.open("GET",HOST+"?"+str,true);
	xhr.addEventListener("load", function() {
	    //document.getElementById("answer").innerHTML = xhr.responseText;
        //console.log(xhr.responseText);
		document.getElementById("answer").innerHTML = "";
		document.getElementById("manager").innerHTML = "";
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
	document.getElementById("answer").innerHTML="openFile";
	var xhr = getXhr();

	var event = {
		type : "getFile",
		path: uri
	};

	document.getElementById("textAddPath").innerHTML = uri;

	var str = JSON.stringify(event);


	xhr.open("GET",HOST+"123?"+str,true);
	xhr.addEventListener("load", function() {
		document.getElementById("answer").innerHTML=xhr.responseText;
	});
	xhr.send();
}

function createPackage(answer){
    console.log(answer);
    if(answer.isMainDir){
    	//инвиз
    }else{
        //uninviz
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

        //
        if(obj.type == "package")
            newDiv.addEventListener("dblclick", function(){openPackage(obj.fullDirectory);});
        else if(obj.type == "file")
            newDiv.addEventListener("dblclick",function(){openFile(obj.fullDirectory)});

        document.getElementById("manager").appendChild(newDiv);
}









