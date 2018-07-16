var HOST = "http://localhost:8080/"

function getXhr(){
	var XHR = ("onload" in new XMLHttpRequest()) ? XMLHttpRequest : XDomainRequest;

	var xhr = new XHR();

	return xhr;
}

function createDir(obj){
    var newDiv = document.createElement('div');
    newDiv.className = "package";

    newDiv.innerHTML = "<img class = \"ico\" src=\"" + "image/package.png" + "\" alt=\"package\">"
    newDiv.innerHTML += "<div class = 'name'>" + obj.name + "</div>";

    //

    newDiv.addEventListener("dblclick", function(){openPackage(obj.fullDirectory);});
    document.getElementById("manager").appendChild(newDiv);
}

function createFile(obj){
    var newDiv = document.createElement('div');
    newDiv.className = "file";

    newDiv.innerHTML = "<img class = \"ico\" src=\"" + "image/file.png" + "\" alt=\"package\">"
    newDiv.innerHTML += "<div class = 'name'>" + obj.name + "</div>";

    newDiv.addEventListener("dblclick",function(){openFile(obj.fullDirectory)});
    //newDiv.at

    document.getElementById("manager").appendChild(newDiv);
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
function openPackage(uri){
	document.getElementById("answer").innerHTML="openPackage";
	
	var xhr = getXhr();
	
	var event = {
		type : "getPackage", 
		path: uri
	};
	document.getElementById("textAddPath").innerHTML = uri;

	var str = JSON.stringify(event);
	
	
	xhr.open("GET",HOST+"123?"+str,true);
	xhr.addEventListener("load", function() {
	    //document.getElementById("answer").innerHTML = xhr.responseText;
        //console.log(xhr.responseText);
		document.getElementById("answer").innerHTML = "";
		document.getElementById("manager").innerHTML = "";
		var json = JSON.parse(xhr.responseText);
		if(json.status){
		    //кнопка назад

		    //console.log(json.answer.fatherDir);
		    if(json.answer.isMainDir){
		        //инвиз
		    }else{
		        //uninviz
                document.getElementById("back").onclick = function(){
                    openPackage(json.answer.fatherDir);
                }
		    }
            for (ind in json.answer.array) {
                obj = json.answer.array[ind];
                if(obj.type == "package")
                    createDir(obj);
                if(obj.type == "file")
                    createFile(obj);
                //document.getElementById("answer").innerHTML += json.answer[ind].name + "<br>";
            }
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



window.onload = function() {
   document.getElementById("1re").onclick = autorization;
   document.getElementById("2re").onclick = function(){openPackage(document.getElementById("addPath").value);};
   document.getElementById("3re").onclick = function(){openFile(document.getElementById("addPath").value);};
   
};

