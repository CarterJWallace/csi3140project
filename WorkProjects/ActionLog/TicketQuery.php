// Query

	$querySQL = ('
	SELECT
		tickets.id AS "ticket_id"
		,tickets.start_datetime
		,tickets.last_updated
		,tickets.id_user AS "user_id"
		,IF(tickets.id_user="","",IFNULL(techs.display_name,"Unknown")) AS "user_name"
		,tickets.client_name
		,IFNULL(CONCAT(building.acronym, " ", room.number), IFNULL((SELECT acronym FROM classrooms.buildings WHERE id=ABS(tickets.id_room) AND is_deleted="False"), "(None)")) AS "room"
				,tickets.priority AS "priority"
		,IFNULL(assigned.display_name,"0") AS "assigned_to"
		,tickets.id_category AS "class"
		,IFNULL(categories.name_en,tickets.id_category) AS "category"
		,IFNULL(problems.name_en,tickets.id_problem) AS "problem"
		,IFNULL(solutions.name_en,tickets.id_solution) AS "solution"
		,IF(tickets.is_remote=1,"remotely","on-site") AS "location"
		,tickets.comment AS "comment"
		,IF(EXISTS(SELECT * FROM classrooms.schedule WHERE building.acronym = classrooms.schedule.building_acronym AND room.number = classrooms.schedule.room_number AND classrooms.schedule.date = CURRENT_DATE), window_start, "7") AS "availability_start"
		,IFNULL(window_end, "22") AS "availability_end"

	FROM al_tickets AS tickets

	LEFT JOIN classrooms.classrooms AS room
	ON tickets.id_room = room.id

	LEFT JOIN classrooms.buildings AS building
	ON room.id_building = building.id

	LEFT JOIN (
		SELECT
			`building_acronym` AS `acronym`,
			`room_number` AS `number`,
			`end_time` AS `window_start`,
			(`end_time` + `difference`) AS `window_end`
		FROM (
			SELECT
									`building_acronymA` AS `building_acronym`,
									`room_numberA` AS `room_number`,
									`start_A` AS `start_time`,
									`end_A` AS `end_time`,
									IFNULL((`start_B` - `end_A`), 0) AS `difference`
							FROM (
									SELECT
											@row_numA := @row_numA + 1 AS `row_A`,
											`building_acronym` AS `building_acronymA`,
											`room_number` AS `room_numberA`,
											`start_A`,
											`end_A`
									FROM ((
											SELECT
													`building_acronym`,
													`room_number`,
													`start_time` AS `start_A`,
													`end_time` AS `end_A`
											FROM classrooms.schedule
											WHERE date = CURRENT_DATE
											ORDER BY `start_time`)
											UNION (
													SELECT DISTINCT
															`building_acronym`,
															`room_number`,
															`start_day` as `start_time`,
															`start_day` as `end_time`
													FROM classrooms.schedule, (SELECT 7 AS `start_day`) AS start_dayA)
											UNION (
													SELECT DISTINCT
															`building_acronym`,
															`room_number`,
															`end_day` as `start_time`,
															`end_day` as `end_time`
													FROM classrooms.schedule, (SELECT 22 AS `end_day`) AS end_dayA)) AS tableA, (SELECT @row_numA := 0) AS numA
									ORDER BY `building_acronym`, `room_number`, `start_A`) AS a
			LEFT JOIN (
									SELECT
											@row_numB := @row_numB + 1 AS `row_B`,
											`building_acronym` AS `building_acronymB`,
											`room_number` AS `room_numberB`,
											`start_B`,
											`end_B`
									FROM ((
											SELECT
													`building_acronym`,
													`room_number`,
													`start_time` AS `start_B`,
													`end_time` AS `end_B`
											FROM classrooms.schedule
											WHERE date = CURRENT_DATE
											ORDER BY `start_time`)
											UNION (
													SELECT DISTINCT
															`building_acronym`,
															`room_number`,
															`start_day` as `start_time`,
															`start_day` as `end_time`
													FROM classrooms.schedule, (SELECT 7 AS start_day) AS start_dayB)
											UNION (
													SELECT DISTINCT
															`building_acronym`,
															`room_number`,
															`end_day` as `start_time`,
															`end_day` as `end_time`
													FROM classrooms.schedule, (SELECT 22 AS end_day) AS end_dayB)) AS tableB, (SELECT @row_numB := 0) AS numB
									ORDER BY `building_acronym`, `room_number`, `start_B`) AS b
			ON b.row_B - a.row_A = 1 AND a.building_acronymA = b.building_acronymB AND a.room_numberA = b.room_numberB) AS final_table
			WHERE `difference` > 0 AND CURRENT_TIME < (CONCAT(Floor(`end_time` + `difference`), ":", LPAD(Floor((`end_time` + `difference`) * 60 % 60), 2, "0")))
		GROUP BY `building_acronym`, `room_number`) as availability			
			ON building.acronym = availability.acronym AND room.number = availability.number

			LEFT JOIN actionlog.al_users AS techs
			ON techs.id = tickets.id_user

	LEFT JOIN actionlog.al_users AS assigned
	ON assigned.id = tickets.id_assigned

	LEFT JOIN actionlog.al_problems AS categories
	ON categories.id = tickets.id_category

	LEFT JOIN actionlog.al_problems AS problems
			ON problems.id = tickets.id_problem

	LEFT JOIN actionlog.al_problems AS solutions
			ON solutions.id = tickets.id_solution

			'.$condition.'

			ORDER BY tickets.last_updated DESC
			LIMIT 0,100
			');
	// Return array
	/*$result = ($this->db->query($querySQL));
	//die(var_dump($this->db->error));
	while($row = $result->fetch_row()){
		print_r($row);
	}
	die();
	return;*/
	return($this->db->query($querySQL));
}
