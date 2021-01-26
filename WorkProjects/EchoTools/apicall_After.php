<?php

/*

This file is in charge of getting requested information from the Echo ALP API
and passing it back via JSON.

*/

/**************************************************************************************************************************
/* INITIALIZATION
/*************************************************************************************************************************/

	require('../assets/php/config.php');
	require('../assets/php/functions_global.php');
	require('../assets/php/functions_strings.php');
	require('../assets/php/functions_echoapi.php');
	require('../assets/php/functions_mysql.php');
	require('../assets/php/init.php');

	if($bExpired) die('{ "expired" : "true" }');

	// Init MySQL
	$db = new mysql();

/**************************************************************************************************************************
/* DETERMINE ACTION
/*************************************************************************************************************************/

	// Defaults
	$error = false;		// Error tracking
	$response = false;	// API response tracking

	if(isset($_POST['action'])){

		// Determine user action
		switch($_POST['action']){

			/**************************************************************************************************************************
			/* SEARCH FOR USER (BY EMAIL or ID)
			/*************************************************************************************************************************/

			case 'userSearch':
				// Admin only function
				if(!$bAdmin){ break; }

				/* API DATA RETURNED:
				[
					{
						"id": "string",
						"institutionId": "string",
						"email": "string",
						"timeZone": "string",
						"timeZoneOffsetMinutes": 0,
						"firstName": "string",
						"lastName": "string",
						"phoneNumber": {
							"region": "string",
							"number": "string"
						},
						"profileImageUrl": "string",
						"roles": [
							"string"
						]
					}
				]
				*/

				// Log
				addLog('Initiated email search for '.(isset($_POST['query']) ? $db->real_escape_string($_POST['query']) : ' (empty string)').'.',$db);

				// Validate email address
				if(validateEmail($_POST['query'])){
					// Query API
					$response = apiCall('/public/api/v1/users/'.urlencode($_POST['query']).'?access_token='.$_SESSION['auth']['accessToken']);
					$response = json_decode($response,true);

					// No / bad response
					if(!$response){
						$error = 103;
					}
					// User was not found
					elseif(isset($response['error']) && $response['error'] == 'NotFound'){
						$error = 100;
					}
					// Another error occurred
					elseif(isset($response['error'])){
						$error = 102;
					}
					else{
						// Clean up un-necessary data
						unset($response['institutionId']);
						unset($response['timeZone']);
						unset($response['timeZoneOffsetMinutes']);
						unset($response['phoneNumber']);

						// Set data
						$data['user'] = $response;
					}

				}
				else{
					$error = 101;
				}
			break;

			/**************************************************************************************************************************
			/* SUBMIT NEW USER (INVITATION)
			/*************************************************************************************************************************/

			case 'userInvite':
				// Admin only function
				if(!$bAdmin){ break; }

				/* API DATA RETURNED:
				[
					{
						"id": "string",
						"institutionId": "string",
						"email": "string",
						"timeZone": "string",
						"timeZoneOffsetMinutes": 0,
						"firstName": "string",
						"lastName": "string",
						"phoneNumber": {
							"region": "string",
							"number": "string"
						},
						"profileImageUrl": "string",
						"roles": [
							"string"
						]
					}
				]
				*/

				// Log
				addLog('Sent ALP invitation to '.(isset($_POST['email']) ? '"'.utf8_decode($db->real_escape_string($_POST['email'])).'"' : ' (empty string)').'.',$db);

				// Validate email address
				if(validateEmail($_POST['email'])){

					// Query API
					$response = apiCall('/public/api/v1/users/'.urlencode($_POST['email']).'?access_token='.$_SESSION['auth']['accessToken']);
					$response = json_decode($response,true);

					// Create JSON data for POST submission
					$userData = array(
						'access_token' => $_SESSION['auth']['accessToken'],
						'email' => $_POST['email'],
						'firstName' => $_POST['firstName'],
						'lastName' => $_POST['lastName'],
						'roles' => array(
							'Instructor'
						)
					);

					// Email address doesn't exists, send out a new invite
					if(isset($response['error']) && isset($response['message']) && strpos($response['message'],'not found')){

						// Send new invitation via API
						$response = apiCall('/public/api/v1/users','POST',json_encode($userData));
						$response = json_decode($response,true);

						// Clean up un-necessary data
						unset($response['institutionId']);
						unset($response['timeZone']);
						unset($response['timeZoneOffsetMinutes']);
						unset($response['phoneNumber']);

						// Set data
						$data['user'] = $response;

					}
					// Error occurred sending invitation
					elseif(isset($response['error'])){
						$error = 202;
					}
					// If we don't have a valid user ID for our supposedly
					// existing user, error out
					elseif(!isset($response['id'])){
						$error = 204;
					}
					// User already exists, perform an update call adding instructor role
					else{

						// Assign our user ID
						$userId = $response['id'];

						// Determine current roles, then add them to $userData array so that
						// we update the user's roles instead of outright replacing them
						if(in_array('Admin',$response['roles'])) $userData['roles'][] = 'Admin';
						if(in_array('Student',$response['roles'])) $userData['roles'][] = 'Student';

						// Update user via API
						$response = apiCall('/public/api/v1/users/'.urlencode($userId),'PUT',json_encode($userData));
						$response = json_decode($response,true);

						// Error occurred updating user roles
						if(isset($response['error'])){
							$error = 200;
						}
						else{
							// Clean up un-necessary data
							unset($response['institutionId']);
							unset($response['timeZone']);
							unset($response['timeZoneOffsetMinutes']);
							unset($response['phoneNumber']);

							// Set data
							$data['user'] = $response;
						}

					}

				}
				else{
					$error = 201;
				}
			break;

			/**************************************************************************************************************************
			/* SUBMIT NEW SCHEDULE
			/*************************************************************************************************************************/

			case 'newSchedule':
				// Log to DB
				addLog("User submit request to create a new schedule",$db);

				/*************************/
				/* Basic Data Validation */
				/*************************/

				// Valid by default
				$validForm = true;

				/*
				** Basic validation
				*/
				// Term
				if($_POST['inputTerm'] == ''){ $validForm = false; $error = 300; }
				// Course
				elseif($_POST['inputCourse'] == ''){ $validForm = false; $error = 301; }
				// Section (text, not ID)
				elseif($_POST['inputSection'] != '' && !preg_match('/([A-Z]{1,2})/i',$_POST['inputSection'])){ $validForm = false; $error = 302; }
				// Title
				elseif($_POST['inputTitle'] == ''){ $validForm = false; $error = 303; }
				// Room
				elseif($_POST['inputRoom'] == ''){ $validForm = false; $error = 304; }
				// Start date
				elseif($_POST['inputStartDate'] == ''){ $validForm = false; $error = 305; }
				// Recurrence and end date (combination)
				elseif($_POST['inputRecurring'] == 1 && $_POST['inputEndDate'] == ''){ $validForm = false; $error = 306; }

				// Form is valid (basic)
				if($validForm){

					/******************* */
					/* Data Construction */
					/*********************/

					/*
					** Map data
					** See below for schema of data to be sent via JSON
					*/
					/*
					Schedule {
                        name (string, optional): Name of the Schedule; defaults to 'Untitled' if omitted,
                        startDate (string): Start date for capture in ISO 8601 Date format = ['YYYY-MM-DD'],
                        startTime (string): Start time for the capture (to the minute) in ISO 8601 Time format. The time zone for the schedule's linked room will be used = ['HH:MM'],
                        endTime (string): End time for the capture (to the minute) in ISO 8601 Time format; HH:MM - The Time Zone obtained from the Campus,
                        endDate (string, optional): The last date for the capture, in ISO 8601 format. If this is specified, daysOfWeek must also be provided = ['YYYY-MM-DD'],
                        daysOfWeek (string, optional),
                        exclusionDates (string, optional),
                        venue (Schedule V2 - SetScheduleVenue): The Venue details comprising of Campus, Building, Room details where the schedule will capture,
                        presenter (Schedule V2 - SetSchedulePresenter, optional): The Presenter details for the Schedule comprising of User Id and Email,
                        guestPresenter (string, optional): Name of any Guest Presenter without an existing User account,
                        sections (Array[Schedule V2 - SetScheduleSection]): List of the Section details in which the capture will be published to,
                        shouldCaption (boolean): Boolean value indicating the Capture should be Captioned,
                        shouldStreamLive (boolean): Boolean value indicating the Capture should be Streamed Live,
                        input1 (string, optional): Input 1 Logical Graphics Type = ['display' or 'video' or 'altvideo'],
                        input2 (string, optional): Input 2 Logical Graphics Type = ['display' or 'video' or 'altvideo'],
                        captureQuality (string): Quality of the Capture = ['medium' or 'high'],
                        streamQuality (string, optional): Streaming Quality of the Capture = ['medium' or 'high']
                        externalId (string, optional): External ID of the Schedule
					}
					*/

					// Access token
					$postData['access_token']				                    = $_SESSION['auth']['accessToken'];
                    // Name
                    $postData['name']                                           = $_POST['inputTitle'];
                    // Start date
                    $postData['startDate']                                      = $_POST['inputStartDate'];
                    // Start time
                    $postData['startTime']                                      = date("H:i:s",strtotime($_POST['inputStartTime']));
                    // Duration
                    $duration = $_POST['inputDuration'] - 1;
                    // End time
                    $postData['endTime']                                        = date("H:i:s", strtotime("{$_POST['inputStartTime']} + $duration minutes"));
                    // Recurring event? (optional)
                    if($_POST['inputRecurring'] == 1){
                            // End date
                            $postData['endDate']                                = $_POST['inputEndDate'];
                            // Days of the week to repeat on
                            if($_POST['inputDays0'] == 1){ $postData['daysOfWeek'][] = 'SU'; } // Sunday
                            if($_POST['inputDays1'] == 1){ $postData['daysOfWeek'][] = 'MO'; } // Monday
                            if($_POST['inputDays2'] == 1){ $postData['daysOfWeek'][] = 'TU'; } // Tuesday
                            if($_POST['inputDays3'] == 1){ $postData['daysOfWeek'][] = 'WE'; } // Wednesday
                            if($_POST['inputDays4'] == 1){ $postData['daysOfWeek'][] = 'TH'; } // Thursday
                            if($_POST['inputDays5'] == 1){ $postData['daysOfWeek'][] = 'FR'; } // Friday
                            if($_POST['inputDays6'] == 1){ $postData['daysOfWeek'][] = 'SA'; } // Saturday
                    }
                    // Venue
                    $postData['venue']                                          = array(
                                                                                    "campusName" => "uOttawa",
                                                                                    "buildingId" => $_POST['inputBuilding'],
                                                                                    "roomId"     => $_POST['inputRoom']);
                    // Presenter
                    $postData['presenter']                                      = array(
                                                                                    "userId"     => $_SESSION['user']['id']);

					/********************/
					/* Section Handling */
					/********************/

					// Term, course and section generation
					$termId		= $_POST['inputTerm'];
					$termStart	= $_SESSION['terms'][$termId]['startDate'];
					$termYear	= substr($termStart,2,2);
					$termMonth	= substr($termStart,6,1);
					switch($termMonth){ // Normalize irregular term months (e.g., fall (usually 09) starting in August (08) or summer starting in April)
						case 8: $termMonth = 9;
						case 4: $termMonth = 5;
					}
					$courseId	= $_POST['inputCourse'];
					$section	= $termYear.'-'.$termMonth.($_POST['inputSection'] != "" ? '-'.strtoupper($_POST['inputSection']) : '');
					$sectionId	= false;

					// Check if section exists
					$querySQL = ('
						SELECT id FROM alp_sections
						WHERE courseId = "'.$courseId.'"
						AND sectionNumber = "'.$section.'"
					');
					$arraySQL = $db->query($querySQL);

					// SQL Error
					if(!$arraySQL){
						$error = 307;
					}

					// Section does not exist, we need to create one
					if($arraySQL->num_rows == 0){
						// Create JSON data for POST submission
						$sectionData = array(
							'access_token' => $_SESSION['auth']['accessToken'],
							'courseId' => $courseId,
							'termId' => $termId,
							'sectionNumber' => $section,
							'description' => "",
							'instructorId' => $_SESSION['user']['id']
						);

						// Send new section to API
						$response = apiCall('/public/api/v1/sections','POST',json_encode($sectionData));
						$response = json_decode($response,true);

						// Error occurred submitting new section
						if(isset($response['error'])){
							$error = 308;
						}
						else{
							// Log
							addLog("Section ".$section." added for ".$_SESSION['courses'][$courseId]['name'],$db);

							// Get new section Id
							$sectionId = $db->real_escape_string($response['id']);

							// Add new section to DB (until next sync)
							$querySQL = ('
								INSERT INTO alp_sections (id, termId, courseId, instructorId, sectionNumber)
								VALUES ("'.$sectionId.'", "'.$termId.'", "'.$courseId.'", "'.$_SESSION['user']['id'].'", "'.$section.'")
							');
							$resultSQL = $db->query($querySQL);

							// SQL Error
							if(!$resultSQL){
								$error = 309;
							}
						}
					}
					// Get section ID
					else{
						while($row = $arraySQL->fetch_object()){
							$sectionId = $row->id;
						}
					}

					// Make sure we have a section
					if(!$sectionId){
						$error = 310;
					}

					/****************************/
					/* Data Construction Cont'd */
					/****************************/

					// Section and publishing information
					// Section ID may have been created a moment ago
					$postData['sections'][0]['courseId']	= $courseId;
					$postData['sections'][0]['termId']	    = $termId;					
					$postData['sections'][0]['sectionId']	= $sectionId;
					$postData['sections'][0]['availability']['availability']		= ($_POST['inputStreamLive'] == 1 || $_POST['inputAutoPublish'] == 1 ? "Immediate" : "Unavailable");
					$postData['sections'][0]['availability']['relativeDelay']		= 0;
					$postData['sections'][0]['availability']['concreteTime']		= $postData['startDate'];
					$postData['sections'][0]['availability']['unavailabilityDelay']	= 0;
					// Caption video?
					$postData['shouldCaption']				= false;
					// Stream video live?
					$postData['shouldStreamLive']			= ($_POST['inputStreamLive'] == 1 ? true : false);
					// Input Source #1
					$postData['input1']						= 'display';
					// Input source #2 (optional)
					if($_POST['inputRecordCam'] == 1){
						$postData['input2']					= 'video';
					}
					// Capture quality
					$postData['captureQuality']				= "high";

					/*************************/
					/* Final Data Validation */
					/*************************/

                    if(!preg_match('/[1-2][0-9]{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])/',$postData['startDate'])){ $validForm = false; $error = 311; }
                    // End date (recurring only)
                    elseif($_POST['inputRecurring'] == 1 && !preg_match('/[1-2][0-9]{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])/',$postData['endDate'])){ $validForm = false; $error = 312; }
                    // Days of week (recurring only)
                    elseif($_POST['inputRecurring'] == 1 && !isset($postData['daysOfWeek'])){ $validForm = false; $error = 313; }
                    // Start time
                    elseif(!preg_match('/([0-9]|1[0-9]|2[0-3]):[0-5][0-9]/',$postData['startTime'])){ $validForm = false; $error = 314; }
                    // Duration (9-239)
                    elseif(!preg_match('/(1[0-9][0-9]|2[0-3][0-9]|[1-9][0-9]|9)/',$duration)){ $validForm = false; $error = 315; }
                    elseif($duration < 9 || $duration > 239){ $validForm = false; $error = 316; }
                    // Section, room and instructor (all uuid)
                    elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$postData['sections'][0]['courseId'])){ $validForm = false; $error = 329; }
                    elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$postData['sections'][0]['termId'])){ $validForm = false; $error = 330; }
                    elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$postData['sections'][0]['sectionId'])){ $validForm = false; $error = 317; }
                    elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$postData['venue']['roomId'])){ $validForm = false; $error = 318; }
                    elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$postData['presenter'])){ $validForm = false; $error = 319; }
                    elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$postData['venue']['buildingId'])){ $validForm = false; $error = 328; }
                    elseif(!preg_match('/uOttawa/',$postData['venue']['campusName'])){ $validForm = false; $error = 329; }

					// Fully validated
					if($validForm){
						/*********************/
						/* Submission to API */
						/*********************/

						// We don't json_decode($response,true) right away so that we
						// quickly analyze it using strpos() below
						$response = apiCall('/public/api/v2/schedules','POST',json_encode($postData));

						// Schedule conflict
						if(strpos($response,"Schedule timing clash")){
							$error = 320;
						}
						// Schedule conflict?  Uncertain about this error...
						else if(strpos($response,"ExpectedModelToBePersisted")){
							$error = 321;
						}
						// API resource not found (offline?)
						else if(strpos($response,"Resource not found")){
							$error = 322;
						}
						// Schedule in the past
						else if(strpos($response,"Time in past")){
							$error = 323;
						}
						// Section not found
						else if(strpos($response,"Section Not Found")){
							$error = 327;
						}
						// Error occurred submitting new schedule
						else if(strpos($response,"Invalid json")){
							$error = 324;
						}
						// Other / generic error
						else if(strpos($response,"error")){
							$error = 325;
							//echo var_dump($response);
							//die(json_encode($postData));
						}
						else{
							// Log
							addLog("Schedule added for ".$_SESSION['user']['email']." for ".$postData['name']." on ".$postData['startDate']." at ".$postData['startTime'],$db);

							// Successful API call, decode $response and set output
							$response = json_decode($response,true);
							$data = $response;

							/*
							// Blank end date and days of week for simple events
							if($_POST['inputRecurring'] == 0){
								$postData['endDate'] = "";
								$postData['daysOfWeek'] = "";
							}
							// Otherwise convert days of week to a string
							else{
								$postData['daysOfWeek'] = implode('|',$postData['daysOfWeek']);
							}

							// Add new schedule to DB (until next sync)
							$querySQL = ('
								INSERT INTO alp_schedules (
									 id
									,startDate
									,startTime
									,endDate
									,daysOfWeek
									,durationMinutes
									,sectionId
									,name
									,roomId
									,instructorId
								)
								VALUES (
									 "'.$db->real_escape_string($response['id']).'"
									,"'.$postData['startDate'].'"
									,"'.$postData['startTime'].'"
									,"'.$postData['endDate'].'"
									,"'.$postData['daysOfWeek'].'"
									,"'.$postData['durationMinutes'].'"
									,"'.$postData['sectionId'].'"
									,"'.$postData['name'].'"
									,"'.$postData['roomId'].'"
									,"'.$postData['instructorId'].'"
								)
							');
							$resultSQL = $db->query($querySQL);

							// SQL Error
							if(!$resultSQL){
								// Commented this out since it's not necessary for the
								// user to see this error - it will fix itself on its
								// own the next time syncdb.php is run.
								//$error = 326;
							}
							*/
						}
					} // End thorough form validation
				} // End basic form validation
			break;

			/**************************************************************************************************************************
			/* DELETE EXISTING SCHEDULE
			/*************************************************************************************************************************/

			case 'deleteSchedule':
				// Log to DB
				addLog("User submit a request to delete an existing schedule",$db);

				// Sanitize
				$id = $db->real_escape_string($_POST['scheduleId']);

				// Try to delete local DB schedule
				$querySQL = ('
					DELETE FROM alp_schedules
					WHERE instructorId = "'.$db->real_escape_string($_SESSION['user']['id']).'"
					AND id = "'.$id.'"
					LIMIT 1
				');

				$resultSQL = $db->query($querySQL);

				// Query was successful, but we need to check if anything was
				// actually removed
				if($resultSQL){
					// Number of affected rows
					$affectedSQL = $db->affected_rows;

					// If we successfully removed a row, go ahead with API call
					if($affectedSQL == 1){
						// Send delete requestvia API
						$response = apiCall('/public/api/v2/schedules/'.$id.'?access_token='.$_SESSION['auth']['accessToken'],'DELETE');
						$response = json_decode($response,true);

						// Make sure we didn't get an error in our response
						if(strpos($response,"error")){
							$error = 402;
						}
						// No errors, go fourth with confidence!
						else{
							// Set data
							$data['success'] = true;

							// Log to DB
							addLog("User deleted schedule ID ".$id,$db);
						}


					}
					// Nothing affected (bad ID, permission error, etc.)
					else{
						$error = 400;
					}
				}
				// Bad SQL query
				else{
					$error = 401;
				}
			break;

			/**************************************************************************************************************************
			/* SUBMIT NEW SECTION (BIND COURSE SECTION TO USER)
			/*************************************************************************************************************************/

			case 'newSection':
				// Log to DB
				addLog("User submit a request to add a section to a course",$db);

				// Admin only function
				if(!$bAdmin){ break; }

				/* API DATA RETURNED:
				{
				  "id": "string",
				  "institutionId": "string",
				  "courseId": "string",
				  "termId": "string",
				  "scheduleIds": [
					 "string"
				  ],
				  "sectionNumber": "string",
				  "instructorId": "string",
				  "description": "string",
				  "lessonCount": 0,
				  "instructorCount": 0,
				  "studentCount": 0,
				  "secondaryInstructorIds": [
					 "string"
				  ],
				  "lmsCourseIds": [
					 "string"
				  ]
				}
				*/

				// Validate email address
				if(validateEmail($_POST['email'])){
					// Query API
					$response = apiCall('/public/api/v1/users/'.urlencode($_POST['email']).'?access_token='.$_SESSION['auth']['accessToken']);
					$response = json_decode($response,true);

					// No / bad response
					if(!$response){
						$error = 500;
					}
					// User was not found
					elseif(isset($response['error']) && $response['error'] == 'NotFound'){
						$error = 501;
					}
					// Another error occurred
					elseif(isset($response['error'])){
						$error = 502;
					}
					else{// Set data
						$instructorId = $response['id'];

						// Clean-up
						unset($response);
					}
				}
				else{
					$error = 503;
				}

				// Make sure we have a valid instructor
				if(isset($instructorId)){
					// Determine section year (e.g., 17)
					$sectionYear = substr($_POST['inputTermText'],-2,2);

					// Section month (e.g., 1)
					if(strpos($_POST['inputTermText'],"Fall")){
						$sectionMonth = 9;
					}
					elseif(strpos($_POST['inputTermText'],"Winter")){
						$sectionMonth = 1;
					}
					else{
						$sectionMonth = 5;
					}

					// Generate full section number (e.g., 17-1-X)
					$sectionNumber = $sectionYear.'-'.$sectionMonth.(isset($_POST['inputSection']) && $_POST['inputSection'] !== "" ? '-'.strtoupper($_POST['inputSection']) : '');

					// Create JSON data for POST submission
					$sectionData = array(
						'access_token' => $_SESSION['auth']['accessToken'],
						'courseId' => $_POST['inputCourse'],
						'termId' => $_POST['inputTerm'],
						'sectionNumber' => $sectionNumber,
						'description' => "",
						'instructorId' => $instructorId
					);

					// Section number validation
					if(!preg_match('/([0-9]){2}-([0-9]){1}(-[A-Z]){0,1}/',$sectionNumber)){ $error = 505; }
					// Course, term and instructor (UUID)
					elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$sectionData['courseId'])){ $error = 506; }
					elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$sectionData['termId'])){ $error = 507; }
					elseif(!preg_match('/([a-f]|[0-9]){8}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){4}-([a-f]|[0-9]){12}/',$sectionData['instructorId'])){ $error = 508; }
					// Still no errors.  Move on and attempt section creation.
					else{
						// Query API
						$response = apiCall('/public/api/v1/sections','POST',json_encode($sectionData));
						$response = json_decode($response,true);

						// Error occurred sending invitation
						if(isset($response['error'])){
							$error = 509;
						}
						// If we don't have a valid section ID
						elseif(!isset($response['id'])){
							$error = 510;
						}
						// Looks like it worked!
						else{

							// Log
							addLog('Created section '.$sectionNumber.' under '.$_POST['inputCourseText'].' for '.(isset($_POST['email']) ? utf8_decode($db->real_escape_string($_POST['email'])) : ' (empty string)').'.',$db);

							// Add new section to DB (until next sync)
							$querySQL = ('
								INSERT INTO alp_sections (id, termId, courseId, instructorId, sectionNumber)
								VALUES ("'.$response['id'].'", "'.$sectionData['termId'].'", "'.$sectionData['courseId'].'", "'.$instructorId.'", "'.$sectionNumber.'")
							');
							$resultSQL = $db->query($querySQL);

							// SQL Error
							if(!$resultSQL){
								$error = 511;
							}
							// Section added to DB
							else{
								// Set data
								$data['success'] = true;
							}

						}
					}
				}
				// No valid instructor was found
				else{
					$error = 504;
				}
			break;

			/**************************************************************************************************************************
			/* ERROR or BAD ACTION
			/*************************************************************************************************************************/
			default:
				$error = 1;
			break;
		}
	}

/**************************************************************************************************************************
/* ERROR HANDLING and JSON OUTPUT
/*************************************************************************************************************************/

	// If we have an error
	if($error){
		// Set data
		$data['error'] = $error;

		// API response dump
		// We use addslashes() as opposed to real_escape_string() to preserve carriage returns
		$apiResponse = ($response ? "\n\nAPI response:\n\n".addslashes(print_r($response,true)) : "");

		// Log
		addLog('Failed with ERROR ('.$db->real_escape_string($data['error']).').'.$apiResponse,$db);
	}
	// If we have no data
	elseif(!isset($data)){
		// Set data
		$data['error'] = 0;

		// Log
		addLog('Failed with ERROR ('.$db->real_escape_string($data['error']).').',$db);
	}

	// Prevent spam!
	sleep(rand(1,2));

	// Output json
	$data = json_encode($data);
	echo $data;

?>
