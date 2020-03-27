<?php
	$items = array();
	$factions = array();
	$mysqli = new mysqli("localhost", "root", "", "csi3140project");
	if ($mysqli -> connect_errno){
		echo '<p>Failed to connect to MySQL: ' . $mysqli -> connect_error . ' </p>';
	} else{
		//echo '<p>Successfully connected to MySQL</p>';
		if ($result = $mysqli -> query("SELECT name, factionID, typeID FROM items ORDER BY name ASC")){
			global $items;
			for($i = 0; $i < $result -> num_rows; $i++){
				$row = $result -> fetch_array(MYSQLI_NUM);
				$items[$i] = array();
				$items[$i]['name'] = $row[0];
				$items[$i]['factionID'] = $row[1];
				$items[$i]['typeID'] = $row[2];
			}
			$result -> close();
		}
		if($result = $mysqli -> query("SELECT * FROM factions ORDER BY factionID ASC")){
			global $factions;
			for($i = 0; $i < $result -> num_rows; $i++){
				$row = $result -> fetch_array(MYSQLI_NUM);
				$factions[$i] = array();
				$factions[$i]['factionName'] = $row[1];
				$factions[$i]['factionID'] = $row[0];
			}
			$result -> close();
			$mysqli -> close();
		}
	}
?>
<html>
	<head>
		<meta charset="utf-8">
		<title> Directory </title>
		<link type = "text/css" rel = "stylesheet" href = "./styles/Directory.css">
		<script src = "./scripts/Directory.js"></script>
	</head>
	<body>
		<div id = "Header">
			<h1>Welcome to the Directory</h1>
		</div>
		<div id = "Body">
			<button class = "level1">+ Frigates</button>
			<div class = "content">
			<?php
				global $items;
				global $factions;
				for($i = 0; $i < count($factions); $i++){
					echo '<button class = "level2">+ ' . $factions[$i]['factionName'] . '</button>';
					echo '<div class = "content">';
					echo '<div class = "level3">';
					echo '<ul>';
					for($j = 0; $j < count($items); $j++){
						if($factions[$i]['factionID'] == $items[$j]['factionID']){
							$string = "window.location.href = './SearchResults.html?q=" . $items[$j]['typeID'] . "';";
							echo '<li><button class = "level3button" onclick = "' . $string . '">' . $items[$j]['name'] . '</button></li>';
						}
					}
					echo '</div>';
					echo '</div>';
				}
			?>		
			</div>
		</div>
		<div id = "Footer">
			<button type ="button" id = "landingbutton" class = "landingButton" onclick="window.location.href = './';">Back to the Landing Page</button>
		</div>
	</body>
</html>