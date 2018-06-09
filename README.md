# Hopscotch Engine
A simple Java engine for creating 2D game worlds.

## Where does the name come from?
The name has to do with the nature of development for this engine which jumped from project to project rather than being developed in isolation. Each project had very different requirements and so the engine had to be adapted to meet them. This jumping from project to project and requirement set to requirement set is where the name "Hopscotch Engine" ultimately comes from.

## Helping support development
Development on this engine will likely continue, and if you are interested in helping to extend it then send an email to ktstephano@gmail.com.

Things I am interested in adding:
* Full OpenGL renderer to replace the JavaFX backend
* Rewritten GUI system since the current is dependent on JavaFX
* Better physics engine that also supports collision response
* Cleaning up the camera control system
* Adding a full suite of unit tests

## Previous projects powered by Hopscotch (each was in collaboration with other people with the engine providing a common base)
This was originally designed and used for https://github.com/gboujaoude/telegram-game as a very thin layer to make designing the game easier.

From here development continued for https://github.com/jmccall2/CS460_EHB_Simulation_Environment where the engine was used to power an electronic handbrake simulation. This is where the console variables, messaging system, rendering and camera controls were first introduced and fleshed out.

Its next project was https://github.com/gboujaoude/cs523-project2.git, a two-part project involving a cellular automata and neutral networks. This required the engine to support a truly multi-threaded environment and was the birth of the task manager.

Next it was used to help power a multi-elevator control system (code base is not currently public) which had a surprising amount of overlap with the cellular automata, at least in terms of what it required the engine to be able to do.

Its final journey (for now) was with an idea model of the immune system found at https://github.com/gboujaoude/cs523-project3. This is where collision detection was added as well as the addition of the quad tree data structure to allow the engine to support thousands of objects in the world.

## Examples
There are two very small and very simple examples found inside of application. A more detailed and thorough walkthrough will be put up later, but you can also refer to the linked projects to see some examples of the older engine in action. cs523-project3, as of writing this, uses an engine that is identical to what you see here.
