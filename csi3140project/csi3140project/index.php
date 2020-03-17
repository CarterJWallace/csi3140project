<?php /*
	<?php
	$mysqli = new mysqli("localhost", "root", "", "test");
	?>
	<html>
		<head>
			<title>PHP Test</title>
		</head>
		<body>

		<?php echo '<p>Hello World</p>'; ?>
		<?php echo '<p>This indicates that the PHP is working</p>'; ?>
		<?php if ($mysqli -> connect_errno){
			echo '<p>Failed to connect to MySQL: ' . $mysqli -> connect_error . ' </p>';
		} else{
			echo '<p>Successfully connected to MySQL</p>';
			if ($result = $mysqli -> query("SELECT * FROM testtable")){
				echo '<p>Returned number of rows are: ' . $result -> num_rows;
				$row = $result -> fetch_row();
				echo '<p>First row: ', $row[0], ' ', $row[1], '</p>';
				$result -> close();
			}
		}
		$mysqli -> close(); ?>
		</body>
	</html>
*/ ?>
<?php
	$names = array();
	$mysqli = new mysqli("localhost", "root", "", "csi3140project");
	if ($mysqli -> connect_errno){
		echo '<p>Failed to connect to MySQL: ' . $mysqli -> connect_error . ' </p>';
	} else{
		//echo '<p>Successfully connected to MySQL</p>';
		if ($result = $mysqli -> query("SELECT name FROM items")){
			global $names;
			for($i = 0; $i < $result -> num_rows; $i++){
				$names[$i] = ($result -> fetch_row())[0];
			}
			$result -> close();
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
			<form autocomplete = "on">
				<div class = "Search">
					<div class = "dropdownBar">
						<input type = "text" placeholder = "Search.." id = "searchBar" class = "searchbar">
						<div id = "dropdown" class = "dropdown-content">
							<?php
								global $names;
								for ($i = 0; $i < count($names); $i++){
									echo '<p>' . $names[$i] . '</p>';
								}
							?>
						</div>
					</div>
					<button type ="button" id = "goButton" class = "button" ;">Go</button>
					<button type ="button" id = "directoryButton" class = "button" onclick="window.location.href = 'Directory.html';">Directory</button>
				</div>
			</form>
		</div>
	</body>
</html>
	
<?php 
	
	//echo file_get_contents("LandingPage.html"); 
?>