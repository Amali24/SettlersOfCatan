# SettlersOfCatan
Advanced Java Group Project - Merged Version

Advanced Java Group Project – as of 11/23/16

What’s in the project:

1. NetBeans Project and associated files

2. CatanGameboard.jpeg
	Provides a visual reference for the coordinates referenced in the game. 
	This will be especially useful in debug mode in the console.
	The console supports using letters for x-coords (e.g. A [enter] 7 [enter]).
3. Bank.java
	Handles trading, doesn’t “do” much on its own, but is used by GameManager
4. Boundary.java
	Represents the boundaries of the HexTile, upon which players can build roads
	Has property of class Line for UI purposes
5. ClientUI.java
	Will provide the GUI for each player (client side)
	Currently not fully functional and only shows a game board
	Functions exist for beginning to make game playable (next step)
6. Coordinate.java
	Very simple class that represents the location of an Intersection object
	Has getUIX and getUIY methods for returning location multiplied by offsets
	and scale factors for ease of use in UI (allows us to not retype those values every single time)
7. DevelopmentCard.java
	Super class development card (like chance/community chest in monopoly)
	Also holds subclass for each specific type of card
	All cards are playable and working currently
8. GameManager.java
	The main “engine” that drives the game
	Sets up gameboard – meaning it creates all the tiles, Intersections, and Boundaries
	Obnoxious hardcoding makes it far easier to deal with all these objects than 
	trying to remember which number they are in an array, or searching through these arrays constantly
	Has instances of Player and Bank
	Will be run server-side in the finished project – the intent is to be able to run multiple
	GameManagers, and thus games, on a single server app.
9. HexTile.java
	Represents the hexagonal tiles on the game board.
10. Intersection.java
	Represents a corner of a hexagonal tile where players can build settlements and cities
	Has a member of type Circle for UI purposes
11. Player.java
	Represents a human player of the game
	Our version of the game will require exactly four human players
	Contains a Color member for UI purposes
	Allows for adding, printing, resetting, and deducting resources
12. Trade.java
	Relatively complicated class that allows for trading between human players or between the bank and a player
	Documentation in the class will provide more detail about what it does and how it works.
13. READ_THIS_FIRST.txt
	This text file.

What we’ve done:

	We’ve made just about every method we can think of for making the game fully playable.
	We’ve made every method in all the “logic” classes (i.e. not the UI) testable via the console. 
	We’ve begun creating our UI and have laid the groundwork for making the roads and intersections clickable
	based upon some conditions that will allow the player to perform build functions through the GUI.

What we haven’t done:

	We have not yet implemented any client-server interaction. This will be done soon. 
	The GameManager will handle the logic of what is going on in the game and will thus be run server-side. 
	Only UI functions will take place client-side. This data hiding will help to prevent cheating.
	
	We have not yet implemented any database operations. This will be done once we have a playable game. 
	Currently, it is not terribly important to be able to save anything, as we do not have a game to save yet. 
	The database will hold each players’ resource total, their settlements, roads, and development cards, along
	with whose turn it is and the game board. It is our intent to have the game automatically save after each 
	round (i.e. every fourth turn) to ensure that not much progress is lost if the game crashes or you must quit.
	
	We haven’t yet gotten the JavaDoc commenting to work. You will see some comments that are in (or close to) the 
	proper formatting, but it didn’t seem to work quite right. This was not a top priority at the time, so we have not
	taken the time to try to solve the issues we were having. This will hopefully be fixed in the next update.
	
	We have not fully implemented ports/harbors into the game. This should not be terribly difficult and will be
	done by the next update.
	
	We have not yet created an actual game. I know this seems like a major undertaking that should not have been 
	left for this late in the semester, but it’s relatively minor. We have all the methods we will need to make 
	the game playable, it’s just a matter of finishing the UI and “attaching” it to those methods, so to speak.
	
	Last, we have yet to create a rules/instructions file. We will do so before the project is finished, 
	but we were not able to before this update was due. If you go to 
	http://www.catan.com/files/downloads/catan_5th_ed_rules_eng_150303.pdf
	you can view the official rules from the manufacturer of the physical game. 
	Our rules may differ slightly, but this is a good place to begin if you have no idea how to play the game whatsoever.
