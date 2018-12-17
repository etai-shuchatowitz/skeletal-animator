# skeletal-animator
This is a simple skeletal-animator.
 
Upon running, it will render a scene with a house object and 
upon user input will add various skeletal characters to the scene. 

Any Collada file (.dae) can be rendered with its associated animations.

Similarly, it can support any .obj file and .md5mesh. As it was built using assimp, I suspect it can handle
other file types though these are the only ones it has been tested on.

## How to Build

```shell
git clone https://github.com/NYUGraphics/final-project-etai-shuchatowitz.git
```

Then open up your favorite IDE to run. 

NOTE: I spent way too long very late at night trying to get the jar to build and for some reason on my Mac this would not work. 
I would like to be able to build and compile through the jar but it won't happen right now.

## How to Control

You will start in a house. Control the camera by using the left-click of the mouse. You can further zoom in and out and up and down using 
W, A, S, D respectively. 

Pressing 1 will make a Bob the lantern guy appear and his animation will run automatically.
Pressing 0 will make the Cowboy appear. He is the only controllable character in the game. Use the spacebar to further the animation
keyframe (that is, pressing and holding on spacebar will continously render the game). You can control his movements using I, J, K, L.

Many thanks to 
1. Antonio Hernández Bejarano and his Starter Code from https://legacy.gitbook.com/@lwjglgamedev
2. https://github.com/TheThinMatrix/OpenGL-Animation - for help getting lots of kinks worked out
