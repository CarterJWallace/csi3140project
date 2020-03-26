window.addEventListener("load", start, false);

function start() {
	let searchBar = document.getElementById("searchBar");
	let content = document.getElementsByTagName('p');
	let myForm = document.getElementById("myForm");
	myForm.reset();
	searchBar.value = "";
	searchBar.addEventListener("focus", expand, false);
	searchBar.addEventListener("blur", hide, false);
	searchBar.addEventListener("keyup", filter, false);
	for(let i = 0; i < content.length; i++){
		content[i].addEventListener("mousedown", fill, false);
	}
}

function expand(){
	let list = document.getElementById("dropdown");
	list.style.display = "block";
}

function hide(){
	let list = document.getElementById("dropdown");
	list.style.display = "none";
}

function fill(){
	event.preventDefault();
	let fillText = this.innerHTML;
	document.getElementById("searchBar").value = fillText;
	hide();
	document.activeElement.blur();
}

function filter(){
	let input = document.getElementById("searchBar");
	let filter = input.value.toUpperCase();
	let div = document.getElementById("dropdown");
	let p = div.getElementsByTagName('p');
	let textValue;
	let counter = p.length;
	for(let i = 0; i < p.length; i++){
		textValue = p[i].textContent || p[i].innerText;
		if(textValue.toUpperCase().indexOf(filter) > -1){
			p[i].style.display = "";
			counter++;
		} else{
			p[i].style.display = "none";
			counter--;
		}
		if(counter == 0){
			div.style.border = "none";
		} else{
			div.style.border = "";
		}
	}
}