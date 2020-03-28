<?php
	$items = array();
	$factions = array();
	$query = "";
	$index = null;
	$tritanium = 0;
	$pyerite = 0;
	$mexallon = 0;
	$isogen = 0;
	$nocxium = 0;
	$zydrine = 0;
	$megacyte = 0;
	$mysqli = new mysqli("localhost", "root", "", "csi3140project");
	if ($mysqli -> connect_errno){
		echo '<p>Failed to connect to MySQL: ' . $mysqli -> connect_error . ' </p>';
	} else{
		//echo '<p>Successfully connected to MySQL</p>';
		if ($result = $mysqli -> query("SELECT name, typeID, Tritanium, Pyerite, Mexallon, Isogen, Nocxium, Zydrine, Megacyte FROM items ORDER BY name ASC")){
			global $items;
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
		global $query;
		global $index;
		global $tritanium;
		global $pyerite;
		global $mexallon;
		global $isogen;
		global $nocxium;
		global $zydrine;
		global $megacyte;
		$query = $_GET["q"];
		$index = array_search($query, array_column($items, 'typeID'));
		$tritanium = $items[$index]['tritanium'];
		$pyerite = $items[$index]['pyerite'];
		$mexallon = $items[$index]['mexallon'];
		$isogen = $items[$index]['isogen'];
		$nocxium = $items[$index]['nocxium'];
		$zydrine = $items[$index]['zydrine'];
		$megacyte = $items[$index]['megacyte'];
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
								global $query;
								global $items;
								global $index;
									if(!empty($query)){
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
								<td class = "quantity"><?php global $tritanium; echo $tritanium; ?></td>
								<td class = "cost"> 1 </td>
							</tr>
							<tr>
								<td class="rowstart">Pyerite</td>
								<td class = "quantity"><?php global $pyerite; echo $pyerite; ?></td>
								<td class = "cost"> 2 </td>
							</tr>
							<tr>
								<td class="rowstart">Mexallon</td>
								<td class = "quantity"><?php global $mexallon; echo $mexallon; ?></td>
								<td class = "cost"> 3 </td>
							</tr>
							<tr>
								<td class="rowstart">Isogen</td>
								<td class = "quantity"><?php global $isogen; echo $isogen; ?></td>
								<td class = "cost"> 4 </td>
							</tr>
							<tr>
								<td class = "rowstart">Nocxium</td>
								<td class = "quantity"><?php global $nocxium; echo $nocxium; ?></td>
								<td class = "cost"> 5 </td>
							</tr>
							<tr>
								<td class = "rowstart">Zydrine</td>
								<td class = "quantity"><?php global $zydrine; echo $zydrine; ?></td>
								<td class = "cost"> 6 </td>
							</tr>
							<tr>
								<td class = "rowstart">Megacyte</td>
								<td class = "quantity"><?php global $megacyte; echo $megacyte; ?></td>
								<td class = "cost"> 7 </td>
							</tr>							
						</tbody>
						<tfoot>
							<tr>
								<td colspan = "2" class = "totalLabel"> Manufacturing Cost: </td>
								<td id = "manuCost"> 0 </td>
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