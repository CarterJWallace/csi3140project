# CSI3140_Project

## Synopsis
Current plan is to use EVE Online's available API and downloadable data assets to create a tool to check if an item
is profitable to trade or to manufacture. The user should be able to change settings based on their own trained skills
that will affect the calculated and shown data.

## UI Design System
### Colour Palette
Colour palette found using coolors.co:
  * #02040F - Off-black for text
  * #2B2F2C - Dark-grey for page backgrounds
  * #748077 - Light-green for content background
  * #535C55 - Dark-green for content border
  * #6B818C - Light-blue for UI element background
  * #394658 - Dark-blue for UI element border
  
### Fonts and Type Scale
The main font will be from Google's font service, named Play (https://fonts.google.com/specimen/Play). 

The type scale was decided using type-scale.com and will be Minor Third (1.200):
  * 2.488em for page header
  * 1.728em for page footer (return to main page)
  * 1.44em for column header
  * 1.2em for second header
  * 1em for basic text


### Icons (and other images)
Images will be obtained through EVE Online's developer resources (developers.eveonline.com/resource/resources).

Images and icons will be bordered using the Grey with rounded corners.

No icons are currently planned but will be obtained on a need-to-use basis.

### Buttons and Form Elements
Buttons will use the lighter and darker blue with rounded corners. As will the tables and lists.

Forms will use the lighter and darker green with rounded corners. 

Colours will alternate on click/hover.

### UI Components (e.g. popups)
No popups are planned currently but an accordion-style dropdown list is planned. Depending on the number of levels the Blues will be used for the level headers with '+' to denote that it can be opened and '-' to denote that it can be closed. Only one selection from each level can be open at a time to save space on the screen. The lowest level will use the basic Off-white and Off-black stlying.

### Example Pages
[Landing Page](LandingPage.html)
[Directory](Directory.html)
[Search Results](SearchResults.html)

## Development Plan
### Stage 1
Mock up webpage design and styling.
Get basic webpage running locally for development.
Figure out webserver and deployment plan.

### Stage 2
Acquire and host data for static market database and item database.
Populate dropdown menus using item database.
Program client-side calculations for calculating item buy/sell costs based on skill levels, tax rates, and market database.
Give a visual indication if sell value is greater than buy value by acceptable amount (~10% until properly calculated).

### Stage 3
Change static market database into dynamic database using API pulls (daily updates to start?).

### Stage 4
Use more static databases to look into manufacturing costs of items to see if manufacturing is cheaper than buying the item and if there's a greater profit margin there.
Add options for other market sources besides the main market.
Add options for manufacturing location for variable tax rates instead of using default values.
See how frequently market information can be pulled from API.

### Stage 5
Think about adding login capability to automatically pull character information.

## Contributors
Carter Wallace - 6444010
