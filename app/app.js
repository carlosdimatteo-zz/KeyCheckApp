function upload () {
	var formData = new FormData();
	formData.append("file", document.getElementById("file").files[0]);
	myFile = document.getElementById("file").files[0].name;
	
	fetch("./test",{method:"POST",credentials:"same-origin",body:formData}).then((res)=>{
		return res.json()
	}).then(data=>{
		console.log(data);
		document.getElementById("uploadStatus").textContent=data.res
	
	}).catch(e=>console.log(e))
	
}


