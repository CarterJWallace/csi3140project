window.addEventListener("load", start, false);
let originalTotal = 0.00;
let originalTri = 0;
let originalPye = 0;
let originalMex = 0;
let originalIso = 0;
let originalNoc = 0;
let originalZyd = 0;
let originalMeg = 0;
let salesTax = 0.05;
let brokerFee = 0.05;

function start() {
	let options = document.getElementsByClassName("option");
	calculate();
	updateSalesTax();
	updateBrokerFees();
	updateTotalCost();
	options[0].addEventListener("change", updateSalesTax, false);
	options[1].addEventListener("change", updateBrokerFees, false);
	options[2].addEventListener("change", update, false);
}

function update(){
	let value3 = document.getElementById("Option-3").value;
	document.getElementById("materialCost").innerHTML = (originalTotal * value3).toFixed(2);
	document.getElementById("qTri").innerHTML = (originalTri * value3).toFixed(0);
	document.getElementById("qPye").innerHTML = (originalPye * value3).toFixed(0);
	document.getElementById("qMex").innerHTML = (originalMex * value3).toFixed(0);
	document.getElementById("qIso").innerHTML = (originalIso * value3).toFixed(0);
	document.getElementById("qNoc").innerHTML = (originalNoc * value3).toFixed(0);
	document.getElementById("qZyd").innerHTML = (originalZyd * value3).toFixed(0);
	document.getElementById("qMeg").innerHTML = (originalMeg * value3).toFixed(0);
}

function calculate(){
	let itemCosts = document.getElementsByClassName("cost");
	let itemQuantities = document.getElementsByClassName("quantity");
	let numberItems = document.getElementsByClassName("rowstart").length;
	for(let i = 0; i < numberItems; i++){
		originalTotal = originalTotal + (parseFloat(itemCosts[i].innerHTML) * parseInt(itemQuantities[i].innerHTML)); 
	}
	originalTotal = parseFloat(originalTotal).toFixed(2);
	originalTri = parseInt(document.getElementById("qTri").innerHTML);
	originalPye = parseInt(document.getElementById("qPye").innerHTML);
	originalMex = parseInt(document.getElementById("qMex").innerHTML);
	originalIso = parseInt(document.getElementById("qIso").innerHTML);
	originalNoc = parseInt(document.getElementById("qNoc").innerHTML);
	originalZyd = parseInt(document.getElementById("qZyd").innerHTML);
	originalMeg	= parseInt(document.getElementById("qMeg").innerHTML);
	document.getElementById("materialCost").innerHTML = originalTotal;
	updateManufactureCost();
}

function updateSalesTax(){
	let finalSalesTax = document.getElementById("salesTax");
	let total = document.getElementById("marketValue").innerHTML;
	let salesTaxReduction = document.getElementById("Option-1").value;
	total = total * salesTax * salesTaxReduction;
	total = total.toFixed(2);
	finalSalesTax.innerHTML = total;
	updateTotalCost();
}

function updateBrokerFees(){
	let finalBrokerBuyFee = document.getElementById("brokerFeeBuy");
	let finalBrokerSellFee = document.getElementById("brokerFeeSell");
	let buyTotal = document.getElementById("marketCost").innerHTML;
	let sellTotal = document.getElementById("marketValue").innerHTML;
	let brokerFeeReduction = document.getElementById("Option-2").value;
	buyTotal = buyTotal * brokerFee * brokerFeeReduction;
	sellTotal = sellTotal * brokerFee * brokerFeeReduction;
	buyTotal = buyTotal.toFixed(2);
	sellTotal = sellTotal.toFixed(2);
	finalBrokerBuyFee.innerHTML = buyTotal;
	finalBrokerSellFee.innerHTML = sellTotal;
	updateTotalCost();
}

function updateTotalCost(){
	let marketCost = parseFloat(document.getElementById("marketCost").innerHTML);
	let salesTax = parseFloat(document.getElementById("salesTax").innerHTML);
	let buyFee = parseFloat(document.getElementById("brokerFeeBuy").innerHTML);
	let sellFee = parseFloat(document.getElementById("brokerFeeSell").innerHTML);
	let finalTradeCost = document.getElementById("totalTradeCost");
	let totalCost = marketCost + salesTax + buyFee + sellFee;
	totalCost = totalCost.toFixed(2);
	finalTradeCost.innerHTML = totalCost;
}

function updateManufactureCost(){
	let finalManufactureCost = document.getElementById("totalManufactureCost");
	let materialCost = parseFloat(document.getElementById("materialCost").innerHTML);
	let manufactureCost = parseFloat(document.getElementById("manufactureCost").innerHTML);
	let totalCost = materialCost + manufactureCost;
	totalCost = totalCost.toFixed(2);
	finalManufactureCost.innerHTML = totalCost;
}
	