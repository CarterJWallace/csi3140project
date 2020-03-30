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
	$query['sell'] = 0;
	$query['buy'] = 0;
	$query['baseCost'] = 0;
	$salesTax = 0.05;
	$image = "./images/0_64.png";
	$mysqli = new mysqli("localhost", "root", "", "csi3140project");
	$url = "https://market.fuzzwork.co.uk/aggregates/?station=60003760&types=34,35,36,37,38,39,40";
	$url2 = "https://api.eve-industry.org/job-base-cost.xml?names=";
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
			$image = "./images/" . $query['typeID'] . "_64.png";
			$tritanium['quantity'] = $items[$index]['tritanium'];
			$pyerite['quantity'] = $items[$index]['pyerite'];
			$mexallon['quantity'] = $items[$index]['mexallon'];
			$isogen['quantity'] = $items[$index]['isogen'];
			$nocxium['quantity'] = $items[$index]['nocxium'];
			$zydrine['quantity'] = $items[$index]['zydrine'];
			$megacyte['quantity'] = $items[$index]['megacyte'];
			$url = $url . "," . $query['typeID'];
			$url2 = $url2 . $items[$index]['name'];
			$json = (json_decode(file_get_contents($url), true));
			$xml = simplexml_load_file($url2);
			$tritanium['cost'] = number_format((float)$json["34"]["sell"]["min"], 2, '.', '');
			$pyerite['cost'] = number_format((float)$json["35"]["sell"]["min"], 2, '.', '');
			$mexallon['cost'] = number_format((float)$json["36"]["sell"]["min"], 2, '.', '');
			$isogen['cost'] = number_format((float)$json["37"]["sell"]["min"], 2, '.', '');
			$nocxium['cost'] = number_format((float)$json["38"]["sell"]["min"], 2, '.', '');
			$zydrine['cost'] = number_format((float)$json["39"]["sell"]["min"], 2, '.', '');
			$megacyte['cost'] = number_format((float)$json["40"]["sell"]["min"], 2, '.', '');
			$query['sell'] = number_format((float)$json[$query['typeID']]["sell"]["min"], 2, '.', '');
			$query['buy'] = number_format((float)$json[$query['typeID']]["buy"]["max"], 2, '.', '');
			$query['baseCost'] = $xml->{'job-base-cost'};
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
			<?php
				global $items;
				global $index;
				if($index !== false){
						$header = $items[$index]['name'];
					} else{
						$header = "No item found";
					}
				echo '<h1>' . $header . '<h2>';
			?>
		</div>
		<div id = "Body">
			<div class = "LeftColumn">
				<div class = "Options">
					<table>
						<tr>
							<td>Accounting Skill</td>
							<td>Broker Relations Skill</td>
							<td>Blueprint Material Efficiency</td>
						</tr>
						<tr>
							<td>
								<select id = "Option-1" class = "option left">
									<option value = "1.00">Level 0 - 0%</option>
									<option value = "0.89">Level 1 - 11%</option>
									<option value = "0.78">Level 2 - 22%</option>
									<option value = "0.67">Level 3 - 33%</option>
									<option value = "0.56">Level 4 - 44%</option>
									<option value = "0.45">Level 5 - 55%</option>
								</select>
							</td>
							<td>
								<select id = "Option-2" class = "option middle">
									<option value = "1.00">Level 0 - 0%</option>
									<option value = "0.997">Level 1 - 0.3%</option>
									<option value = "0.994">Level 2 - 0.6%</option>
									<option value = "0.991">Level 3 - 0.9%</option>
									<option value = "0.988">Level 4 - 1.2%</option>
									<option value = "0.985">Level 5 - 1.5%</option>
								</select>
							</td>
							<td>
								<select id = "Option-3" class = "option right">
									<option value = "1.00">Level 0 - 0%</option>
									<option value = "0.99">Level 1 - 1%</option>
									<option value = "0.98">Level 2 - 2%</option>
									<option value = "0.97">Level 3 - 3%</option>
									<option value = "0.96">Level 4 - 4%</option>
									<option value = "0.95">Level 5 - 5%</option>
									<option value = "0.94">Level 6 - 6%</option>
									<option value = "0.93">Level 7 - 7%</option>
									<option value = "0.92">Level 8 - 8%</option>
									<option value = "0.91">Level 9 - 9%</option>
									<option value = "0.90">Level 10 - 10%</option>
								</select>
							</td>
						</tr>
					</table>
				</div>
				<div class = "Data">
					<table>
						<thead>
							<tr>
								<th>Item</th>
								<th>Quantity</th>
								<th>Cost</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class = "rowstart">Tritanium</td>
								<td class = "quantity" id = "qTri"><?php global $tritanium; echo $tritanium['quantity']; ?></td>
								<td class = "cost"><?php global $tritanium; echo $tritanium['cost']; ?></td>
							</tr>
							<tr>
								<td class="rowstart">Pyerite</td>
								<td class = "quantity" id = "qPye"><?php global $pyerite; echo $pyerite['quantity']; ?></td>
								<td class = "cost"><?php global $pyerite; echo $pyerite['cost']; ?></td>
							</tr>
							<tr>
								<td class="rowstart">Mexallon</td>
								<td class = "quantity" id = "qMex"><?php global $mexallon; echo $mexallon['quantity']; ?></td>
								<td class = "cost"><?php global $mexallon; echo $mexallon['cost']; ?></td>
							</tr>
							<tr>
								<td class="rowstart">Isogen</td>
								<td class = "quantity" id = "qIso"><?php global $isogen; echo $isogen['quantity']; ?></td>
								<td class = "cost"><?php global $isogen; echo $isogen['cost']; ?></td>
							</tr>
							<tr>
								<td class = "rowstart">Nocxium</td>
								<td class = "quantity" id = "qNoc"><?php global $nocxium; echo $nocxium['quantity']; ?></td>
								<td class = "cost"><?php global $nocxium; echo $nocxium['cost']; ?></td>
							</tr>
							<tr>
								<td class = "rowstart">Zydrine</td>
								<td class = "quantity" id = "qZyd"><?php global $zydrine; echo $zydrine['quantity']; ?></td>
								<td class = "cost"><?php global $zydrine; echo $zydrine['cost']; ?></td>
							</tr>
							<tr>
								<td class = "rowstart">Megacyte</td>
								<td class = "quantity" id = "qMeg"><?php global $megacyte; echo $megacyte['quantity']; ?></td>
								<td class = "cost"><?php global $megacyte; echo $megacyte['cost']; ?></td>
							</tr>							
						</tbody>
						<tfoot>
							<tr>
								<td align = "right" colspan = "2" class = "totalLabel">Ship's Material Cost:</td>
								<td align = "center" id = "materialCost"> 0 </td>
							</tr>
							<tr>
								<td align = "right" colspan = "2" class = "totalLabel">Ship's Manufacture Cost:</td>
								<td align = "center" id = "manufactureCost"><?php global $query; echo number_format($query['baseCost'] * (0.1242 * 1.1), 2, '.', ''); ?></td>
							</tr>							
						</tfoot>
					</table>
				</div>
				<div class = "Market">
					<table>
						<tbody>
							<tr>
								<td align = "right" colspan = "2" class = "totalLabel">Ship's Market Cost:</td>
								<td align = "center" id = "marketCost"><?php global $query; echo $query['buy']; ?></td>
							</tr>
							<tr>
								<td align = "right" colspan = "2" class = "totalLabel">Ship's Market Value:</td>
								<td align = "center" id = "marketValue"><?php global $query; echo $query['sell']; ?></td>
							</tr>							
							<tr>
								<td align = "right" colspan = "2" class = "totalLabel">Sales Tax:</td>
								<td align = "center" id = "salesTax">0</td>
							</tr>
							<tr>
								<td align = "right" colspan = "2" class = "totalLabel">Broker Fee to Buy:</td>
								<td align = "center" id = "brokerFeeBuy">0</td>
							</tr>		
							<tr>
								<td align = "right" colspan = "2" class = "totalLabel">Broker Fee to Sell:</td>
								<td align = "center" id = "brokerFeeSell">0</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class = "RightColumn">
				<div class = "Frame">
					<img src = "<?php global $image; echo $image; ?>" alt = "Ship image" height = "128" width = "128">
				</div>
				<div class = "Results">
					<table>
						<tbody>
							<tr>
								<td align = "right" class = "totalLabel">Manufacture Cost:</td>
								<td align = "center" id = "totalManufactureCost">0</td>								
							</tr>
							<tr>
								<td colspan = "2" height = "0"> </td>
							</tr>
							<tr>
								<td align = "right" class = "totalLabel">Trade Cost:</td>
								<td align = "center" id = "totalTradeCost">0</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div id = "Footer">
			<button type ="button" id = "landingbutton" class = "landingButton" onclick="window.location.href = './';">Back to the Landing Page</button>
		</div>
	</body>
</html>