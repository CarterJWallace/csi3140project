<?php
	$items = array();
	$factions = array();
	$index = false;
	$tritanium = array();
	$pyerite = array();
	$mexallon = array();
	$isogen = array();
	$nocxium = array();
	$zydrine = array();
	$megacyte = array();
	$query = array();
	$tritanium['quantity'] = 0;
	$pyerite['quantity'] = 0;
	$mexallon['quantity'] = 0;
	$isogen['quantity'] = 0;
	$nocxium['quantity'] = 0;
	$zydrine['quantity'] = 0;
	$megacyte['quantity'] = 0;
	$tritanium['cost'] = 0;
	$pyerite['cost'] = 0;
	$mexallon['cost'] = 0;
	$isogen['cost'] = 0;
	$nocxium['cost'] = 0;
	$zydrine['cost'] = 0;
	$megacyte['cost'] = 0;
	$query['cost'] = 0;
	$mysqli = new mysqli("localhost", "root", "", "csi3140project");
	$url = "https://market.fuzzwork.co.uk/aggregates/?station=60003760&types=34,35,36,37,38,39,40";
	if ($mysqli -> connect_errno){
		echo '<p>Failed to connect to MySQL: ' . $mysqli -> connect_error . ' </p>';
	} else{
		//echo '<p>Successfully connected to MySQL</p>';
		if ($result = $mysqli -> query("SELECT name, typeID, Tritanium, Pyerite, Mexallon, Isogen, Nocxium, Zydrine, Megacyte FROM items ORDER BY name ASC")){
			for($i = 0; $i < $result -> num_rows; $i++){
				$row = $result -> fetch_array(MYSQLI_NUM);
				$items[$i] = array();
				$items[$i]['name'] = $row[0];
				$items[$i]['typeID'] = $row[1];
				$items[$i]['tritanium'] = $row[2];
				$items[$i]['pyerite'] = $row[3];
				$items[$i]['mexallon'] = $row[4];
				$items[$i]['isogen'] = $row[5];
				$items[$i]['nocxium'] = $row[6];
				$items[$i]['zydrine'] = $row[7];
				$items[$i]['megacyte'] = $row[8];
			}
			$result -> close();
		}
	}
	if(array_key_exists("q", $_GET)){
		$query['typeID'] = $_GET["q"];
		$index = array_search($query['typeID'], array_column($items, 'typeID'), true);
		if($index !== false){
			$tritanium['quantity'] = $items[$index]['tritanium'];
			$pyerite['quantity'] = $items[$index]['pyerite'];
			$mexallon['quantity'] = $items[$index]['mexallon'];
			$isogen['quantity'] = $items[$index]['isogen'];
			$nocxium['quantity'] = $items[$index]['nocxium'];
			$zydrine['quantity'] = $items[$index]['zydrine'];
			$megacyte['quantity'] = $items[$index]['megacyte'];
			$url = $url . "," . $query['typeID'];
			$json = (json_decode(file_get_contents($url), true));
			$tritanium['cost'] = ((float)$json["34"]["sell"]["min"]);
			$pyerite['cost'] = ((float)$json["35"]["sell"]["min"]);
			$mexallon['cost'] = ((float)$json["36"]["sell"]["min"]);
			$isogen['cost'] = ((float)$json["37"]["sell"]["min"]);
			$nocxium['cost'] = ((float)$json["38"]["sell"]["min"]);
			$zydrine['cost'] = ((float)$json["39"]["sell"]["min"]);
			$megacyte['cost'] = ((float)$json["40"]["sell"]["min"]);
			$query['cost'] = ((float)$json[$query['typeID']]["sell"]["min"]);
		}
	}
?>
<html>
	<head>
		<meta charset="utf-8">
		<title> Search Results </title>
		<link type = "text/css" rel = "stylesheet" href = "./styles/SearchResults.css">
		<script src = "./scripts/SearchResults.js"></script>
	</head>
	<body>
		<div id = "Header">
			<h1>Welcome to the Search Results</h1>
		</div>
		<div id = "Body">
			<div class = "LeftColumn">
				<div class = "Options">
					<table>
						<tr>
							<td>Skill 1</td>
							<td>Skill 2</td>
							<td>Skill 3</td>
						</tr>
						<tr>
							<td>
								<select id = "Option-1" class = "option left">
									<option value = "1.00">Level 1 - 0%</option>
									<option value = "1.05">Level 2 - 5%</option>
									<option value = "1.10">Level 3 - 10%</option>
									<option value = "1.15">Level 4 - 15%</option>
									<option value = "1.20">Level 5 - 20%</option>
								</select>
							</td>
							<td>
								<select id = "Option-2" class = "option middle">
									<option value = "1.00">Level 1 - 0%</option>
									<option value = "1.05">Level 2 - 5%</option>
									<option value = "1.10">Level 3 - 10%</option>
									<option value = "1.15">Level 4 - 15%</option>
									<option value = "1.20">Level 5 - 20%</option>
								</select>
							</td>
							<td>
								<select id = "Option-3" class = "option right">
									<option value = "1.00">Level 1 - 0%</option>
									<option value = "1.05">Level 2 - 5%</option>
									<option value = "1.10">Level 3 - 10%</option>
									<option value = "1.15">Level 4 - 15%</option>
									<option value = "1.20">Level 5 - 20%</option>
								</select>
							</td>
						</tr>
					</table>
				</div>
				<div class = "Data">
					<table>
						<thead>
							<tr>
								<?php
								global $items;
								global $index;
									if($index !== false){
										$thead = $items[$index]['name'];
									} else{
										$thead = "No item found";
									}
									echo '<th colspan ="3"><div id = "tableHeader">' . $thead . '</div></th>';
								?>
							</tr>
							<tr>
								<th>Item</th>
								<th>Quantity</th>
								<th>Cost</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class = "rowstart">Tritanium</td>
								<td class = "quantity"><?php global $tritanium; echo $tritanium['quantity']; ?></td>
								<td class = "cost"><?php global $tritanium; echo $tritanium['cost']; ?></td>
							</tr>
							<tr>
								<td class="rowstart">Pyerite</td>
								<td class = "quantity"><?php global $pyerite; echo $pyerite['quantity']; ?></td>
								<td class = "cost"><?php global $pyerite; echo $pyerite['cost']; ?></td>
							</tr>
							<tr>
								<td class="rowstart">Mexallon</td>
								<td class = "quantity"><?php global $mexallon; echo $mexallon['quantity']; ?></td>
								<td class = "cost"><?php global $mexallon; echo $mexallon['cost']; ?></td>
							</tr>
							<tr>
								<td class="rowstart">Isogen</td>
								<td class = "quantity"><?php global $isogen; echo $isogen['quantity']; ?></td>
								<td class = "cost"><?php global $isogen; echo $isogen['cost']; ?></td>
							</tr>
							<tr>
								<td class = "rowstart">Nocxium</td>
								<td class = "quantity"><?php global $nocxium; echo $nocxium['quantity']; ?></td>
								<td class = "cost"><?php global $nocxium; echo $nocxium['cost']; ?></td>
							</tr>
							<tr>
								<td class = "rowstart">Zydrine</td>
								<td class = "quantity"><?php global $zydrine; echo $zydrine['quantity']; ?></td>
								<td class = "cost"><?php global $zydrine; echo $zydrine['cost']; ?></td>
							</tr>
							<tr>
								<td class = "rowstart">Megacyte</td>
								<td class = "quantity"><?php global $megacyte; echo $megacyte['quantity']; ?></td>
								<td class = "cost"><?php global $megacyte; echo $megacyte['cost']; ?></td>
							</tr>							
						</tbody>
						<tfoot>
							<tr>
								<td colspan = "2" class = "totalLabel"> Ship's Cost: </td>
								<td id = "manuCost"><?php global $query; echo $query['cost']; ?></td>
							</tr>
							<tr>
								<td colspan = "2" class = "totalLabel"> Total Cost: </td>
								<td id = "finalCost"> 0 </td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
			<div class = "RightColumn">
				<div class = "frame">
					This is where the icon would be.
				</div>
				<div class = "results">
					This is where the results would go.
				</div>
			</div>
		</div>
		<div id = "Footer">
			<button type ="button" id = "landingbutton" class = "landingButton" onclick="window.location.href = './';">Back to the Landing Page</button>
		</div>
	</body>
</html>