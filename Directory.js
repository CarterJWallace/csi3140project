var level1 = document.getElementsByClassName("level1");
var level2 = document.getElementsByClassName("level2");

var i;

for(i = 0; i < level1.length; i++){
	level1[i].addEventListener("click", function(){
		var label = this.innerHTML;
		if (label[0] == '+'){
			label = label.replace("+", "-");
			this.innerHTML = label;
		} else{
			label = label.replace("-", "+");
			this.innerHTML = label;
		}
		this.classList.toggle("active");
		var content = this.nextElementSibling;
		if(content.style.maxHeight){
			content.style.maxHeight = null;
		} else{
			content.style.maxHeight = content.scrollHeight + "px";
		}
	});
}

for(i = 0; i < level2.length; i++){
	level2[i].addEventListener("click", function(){
		var label = this.innerHTML;
		if (label[0] == '+'){
			label = label.replace("+", "-");
			this.innerHTML = label;
		} else{
			label = label.replace("-", "+");
			this.innerHTML = label;
		}
		this.classList.toggle("active");
		var content = this.nextElementSibling;
		if(content.style.maxHeight){
			content.style.maxHeight = null;
		} else{
			content.style.maxHeight = content.scrollHeight + "px";
		}
	});
}