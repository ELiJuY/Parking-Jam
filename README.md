All files written by the application are .txt format, and the ones intended to be read by it must also be .txt format.

To add a new level to play, you need to create a plain text file that follows the format specified in the project requirements, store it in the directory src\main\resources\levels and name it level_N.txt, with N being the number of this level. Notice that you can't leave gaps between levels. That is to say, if there is only one level in the levels' directory named level_1.txt, the next one created must be level_2.txt. Otherwise, it won't be detected by the game.

The game is played with just a mouse. The only interactions with the game are left-clicking the different buttons or the cars to drag them.

While playing a level, there is a set of 5 different buttons. This is an explanation of what does each button, from left to right:

Restart button: Restarts a level and updates the levelScore to 0.
Back-to-menu button: Allows you to go back to the menu to select another level.
Undo button: Each click undoes the last movement. If there are no more movements to undo, it has no effect. Notice that undoing a movement doesn't subtract this movement from neither the LevelScore nor TotalScore.
Save button: Allows to save the progress in the middle of a game. It will create a plain text file that can be named and stored wherever the user wants. You can later load this level again with your progress by clicking the "Load Game" button in the menu and selecting this file. The saved file adheres to the following format:
- The first three lines contain the **level number**, the **current score** and the **accumulated score**, respectively.
- The subsequent lines follow the **same format** as the level files specified in the project requirements.
- After the **board** representing the level elements, the following lines contain the movements made in the level, formatted as **vehicleID** **coordX** **coordY**, with one movement per line in order from most recent to oldest.

This represents the format specified above:


```
2
9
0
Second level
8 8
++++++++
+bbbjd +
+   jd +
+**    @
+afff c+
+a heec+
+gghiic+
++++++++
c 2 6
e 5 5
* 3 2
d 3 5
j 2 4
b 1 4
a 1 1
* 3 1
f 4 1
```


Music button: Allows to play or stop the background music of the game.
