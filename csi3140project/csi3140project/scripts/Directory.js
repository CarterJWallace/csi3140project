window.addEventListener("load", start, false);

function start() {
	let level1 = document.getElementsByClassName("level1");
	let level2 = document.getElementsByClassName("level2");

	let i;

	for(i = 0; i < level1.length; i++){
		level1[i].addEventListener("click", function(){
			let label = this.innerHTML;
			if (label[0] == '+'){
				label = label.replace("+", "-");
				this.innerHTML = label;
			} else{
				label = label.replace("-", "+");
				this.innerHTML = label;
			}
			this.classList.toggle("active");
			let content = this.nextElementSibling;
			if(content.style.maxHeight){
				content.style.maxHeight = null;
			} else{
				content.style.maxHeight = "100%"
			}
		});
	}

	for(i = 0; i < level2.length; i++){
		level2[i].addEventListener("click", function(){
			let label = this.innerHTML;
			if (label[0] == '+'){
				label = label.replace("+", "-");
				this.innerHTML = label;
			} else{
				label = label.replace("-", "+");
				this.innerHTML = label;
			}
			this.classList.toggle("active");
			let content = this.nextElementSibling;
			let contentParent = content.parentElement;
			if(content.style.maxHeight){
				content.style.maxHeight = null;
			} else{
				content.style.maxHeight = "100%";
				contentParent.style.maxHeight = "100%";
			}
		});
	}
}

