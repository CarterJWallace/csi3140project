<?php
	$names = array();
	$mysqli = new mysqli("localhost", "root", "", "csi3140project");
	if ($mysqli -> connect_errno){
		echo '<p>Failed to connect to MySQL: ' . $mysqli -> connect_error . ' </p>';
	} else{
		//echo '<p>Successfully connected to MySQL</p>';
		if ($result = $mysqli -> query("SELECT name, typeID FROM items ORDER BY name ASC")){
			//global $names;
			for($i = 0; $i < $result -> num_rows; $i++){
				$row = $result -> fetch_array(MYSQLI_NUM);
				$names[$i] = array();
				$names[$i]['name'] = $row[0];
				$names[$i]['id'] = $row[1];
			}
			$result -> close();
		}
	}
	
	if(array_key_exists('submit', $_POST)){
		submit();
	} else if(array_key_exists('directory', $_POST)){
		goToDir();
	}
	
	//Need $names to be accessible in this function.
	function submit(){
		if(array_key_exists('searchbar', $_POST)){
			if($id = array_search($_POST['searchbar'], $names)){
				echo $id;
			}
		}
	}
	
	//Need to figure out how directory will be managed, php?
	function goToDir(){	
		header("Location:Directory.html");
	}
?>
<html>
	<head>
		<meta charset="utf-8">
		<title>Landing Page</title>
		<link type="text/css" rel="stylesheet" href="./styles/LandingPage.css">
		<script src = "./scripts/LandingPage.js"></script>
	</head>
	<body>
		<div id = "Header">
			<h1>Welcome to the Landing Page</h1>
		</div>
		<div id = "Body">
			<form autocomplete = "on" method = "post">
				<div class = "Search">
					<div class = "dropdownBar">
						<input type = "text" placeholder = "Search.." id = "searchBar" class = "searchbar" name = "searchbar">
						<div id = "dropdown" class = "dropdown-content">
							<?php
								//global $names;
								for ($i = 0; $i < count($names); $i++){
									echo '<p>' . $names[$i]['name'] . '</p>';
								}
							?>
						</div>
					</div>
					<input type ="submit" class="button" name="submit" value="Go" />
					<input type ="submit" class="button" name="directory" value="Directory" />
				</div>
			</form>
		</div>
	</body>
</html>