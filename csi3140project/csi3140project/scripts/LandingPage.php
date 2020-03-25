<?php
	if (isset($_POST['action'])){
		switch ($_POST['action']){
			case 'submit':
				submit();
				break;
			case 'directory':
				directory();
				break;
		}
	}
	
	function submit(){
		echo "Submit";
		exit;
	}
	
	function insert(){
		echo "Insert";
		exit;
	}
?>