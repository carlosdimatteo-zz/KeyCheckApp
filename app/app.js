// Utils
const $ = (id) => document.getElementById(id);

// function upload () {
// 	var formData = new FormData();
// 	formData.append("file", document.getElementById("file").files[0]);
// 	myFile = document.getElementById("file").files[0].name;
	
// 	fetch("./test",{method:"POST",credentials:"same-origin",body:formData}).then((res)=>{
// 		return res.json()
// 	}).then(data=>{
// 		console.log(data);
// 		document.getElementById("uploadStatus").textContent=data.res
	
// 	}).catch(e=>console.log(e))
	
// }


function fetchDecryptedFile() {
	fetch("./test?filename=prueba1.enc", {
		method: 'GET',
		credentials: 'same-origin',		
	}).then(res => res.json())
	.then(data => {
		console.log(data)
	}).catch(err => {
		console.log(err)
	})
} 

function upload() {
	let fd = new FormData();
	let files = $('files').files;
	
	for(let i = 0; i < files.length; i++) {		
		fd.append('file[]', files[i], files[i].name);
	}
	
	fetch('./test', { 
		method: "POST", 
		credentials: 'same-origin', 
		body: fd, 
	}).then(res => res.json()).then(data => {
		console.log(data);
		$('status').textContent = data.res;
	}).catch(err => {
		console.log(err);
	});
}
