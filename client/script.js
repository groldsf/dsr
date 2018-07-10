var HOST = "http://localhost:8080/"




function getXhr(){
	var XHR = ("onload" in new XMLHttpRequest()) ? XMLHttpRequest : XDomainRequest;

	var xhr = new XHR();

	return xhr;
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
		path: "12/34/56"
	};
	
	var str = JSON.stringify(event);
	
	
	xhr.open("GET",HOST+"123?"+str,true);
	xhr.addEventListener("load", function() {
		document.getElementById("answer").innerHTML=xhr.responseText;
	});
	xhr.send();
}
function openFile(){
	document.getElementById("answer").innerHTML="openFile";
	var xhr = getXhr();
	
	var event = {
		type : "getFile", 
		path: "12/34/56/123.txt"
	};
	
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

