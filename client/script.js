var HOST = "http://localhost:8080/"




function getXhr(){
	var XHR = ("onload" in new XMLHttpRequest()) ? XMLHttpRequest : XDomainRequest;

	var xhr = new XHR();

	return xhr;
}

function createDir(obj){

}

function createFile(obj){

}

function autorization(){
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
function openPackage(){
	document.getElementById("answer").innerHTML="openPackage";
	
	var xhr = getXhr();
	
	var event = {
		type : "getPackage", 
		path: document.getElementById("addPath").value
	};
	document.getElementById("textAddPath").innerHTML = document.getElementById("addPath").value;

	var str = JSON.stringify(event);
	
	
	xhr.open("GET",HOST+"123?"+str,true);
	xhr.addEventListener("load", function() {
	    //document.getElementById("answer").innerHTML = xhr.responseText;

		document.getElementById("answer").innerHTML="";
		var json = JSON.parse(xhr.responseText);
		if(json.status){
            for (ind in json.answer) {
                obj = json.answer[ind];
                if(obj.type == "directory")
                    createDir(obj);
                if(obj.type == "file")
                    createFile(obj);
                document.getElementById("answer").innerHTML += json.answer[ind].name + "<br>";
            }
		}else{
		    document.getElementById("answer").innerHTML = xhr.responseText;
		}

	});
	xhr.send();
}
function openFile(){
	document.getElementById("answer").innerHTML="openFile";
	var xhr = getXhr();
	
	var event = {
		type : "getFile", 
		path: document.getElementById("addPath").value
	};

	document.getElementById("textAddPath").innerHTML = document.getElementById("addPath").value;

	var str = JSON.stringify(event);
	
	
	xhr.open("GET",HOST+"123?"+str,true);
	xhr.addEventListener("load", function() {
		document.getElementById("answer").innerHTML=xhr.responseText;
	});
	xhr.send();
}



window.onload = function() {
   document.getElementById("1re").onclick = autorization;
   document.getElementById("2re").onclick = openPackage;
   document.getElementById("3re").onclick = openFile;
   
};

