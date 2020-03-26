<?php
	$items = array();
	$mysqli = new mysqli("localhost", "root", "", "csi3140project");
	if ($mysqli -> connect_errno){
		echo '<p>Failed to connect to MySQL: ' . $mysqli -> connect_error . ' </p>';
	} else{
		//echo '<p>Successfully connected to MySQL</p>';
		if ($result = $mysqli -> query("SELECT name, typeID FROM items ORDER BY name ASC")){
			global $items;
			for($i = 0; $i < $result -> num_rows; $i++){
				$row = $result -> fetch_array(MYSQLI_NUM);
				$items[$i] = array();
				$items[$i]['name'] = $row[0];
				$items[$i]['id'] = $row[1];
			}
			$result -> close();
			$mysqli -> close();
		}
	}
	
	if($_SERVER["REQUEST_METHOD"] == "POST"){
		$search = $_POST['searchbar'];
		if(!empty($search)){
			submit($search);
		}
	}
	
	function submit($search){
		global $items;
		$index = array_search($search, array_column($items, 'name'));
		if($index !== false){
			header("Location:SearchResults.html");
		}
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
			<form id = "myForm" autocomplete = "on" method = "post" action="<?php echo $_SERVER['PHP_SELF'];?>">
				<div class = "Search">
					<div class = "dropdownBar">
						<input type = "text" placeholder = "Search.." id = "searchBar" class = "searchbar" name = "searchbar">
						<div id = "dropdown" class = "dropdown-content">
							<?php
								global $items;
								for ($i = 0; $i < count($items); $i++){
									echo '<p>' . $items[$i]['name'] . '</p>';
								}
							?>
						</div>
					</div>
					<input type ="submit" class="button" name="submit" value="Go" />
					<input type ="button" class="button" onclick="window.location.href = './Directory.php';" value="Directory" />
				</div>
			</form>
		</div>
	</body>
</html>