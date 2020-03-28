window.addEventListener("load", start, false);
let originalTotal = 0.00;

function start() {
	//let webpage = window.location.href;
	//let query = webpage.substring((webpage.indexOf('?q=') + 1));
	//let header = document.getElementById("tableHeader");
	let options = document.getElementsByClassName("option");
	/*if(query){
		query = query.charAt(0).toUpperCase() + query.substring(1);
		query = query.substring(0,4) + " " + query.substring(4);
		header.innerHTML = query;
	} else{
		header.innerHTML = "Item Not Found";
	}*/
	calculate();
	for(let i = 0; i < options.length; i++){
		options[i].addEventListener("change", update, false);
	}
}

function update(){
	let finalCost = document.getElementById("finalCost");
	let total = originalTotal;
	let value1 = document.getElementById("Option-1").value;
	let value2 = document.getElementById("Option-2").value;	
	let value3 = document.getElementById("Option-3").value;
	if(parseFloat(value1)){
		total = total * value1;
	}
	if(parseFloat(value2)){
		total = total * value2;
	}
	if(parseFloat(value3)){
		total = total * value3;
	}
	total = total.toFixed(2);
	finalCost.innerHTML = total;
}

function calculate(){
	let itemCosts = document.getElementsByClassName("cost");
	let itemQuantities = document.getElementsByClassName("quantity");
	let numberItems = document.getElementsByClassName("rowstart").length;
	for(let i = 0; i < numberItems; i++){
		originalTotal = originalTotal + (itemCosts[i].innerHTML * itemQuantities[i].innerHTML); 
	}
	originalTotal = originalTotal.toFixed(2);
	document.getElementById("finalCost").innerHTML = originalTotal;
}