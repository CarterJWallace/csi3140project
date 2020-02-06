# CSI3140_Project

## Synopsis
Current plan is to use EVE Online's available API and downloadable data assets to create a tool to check if an item
is profitable to trade or to manufacture. The user should be able to change settings based on their own trained skills
that will affect the calculated and shown data.

## UI Design System
### Colour Palette
Colour palette found using coolors.co:
  * #02040F - Off-black for text
  * #F5DADA - Off-white for backgrounds
  * #AAAE7F - Grey for soft emphasis (borders)
  * #0F5257 + #0B3142 Blues for UI elements (buttons)
### Fonts and Type Scale

### Icons (and other images)

### Buttons and Form Elements

### UI Components (e.g. popups)

### Example Pages

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
