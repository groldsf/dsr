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


    //newDiv.at

    document.getElementById("manager").appendChild(newDiv);
}