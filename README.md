lowlatency
==========

Low Latency is an osu!-esque rhythm game for Android.

Purpose
-------
After playing the original Audiosurf (and loving it), I wanted a similar experience on my phone. I also wanted to know how the music analysis code worked. So, I decided to try to write a rhythm game for Android with the same features -- fast-paced, in-sync music-driven hyphenated-words gameplay that ran on the user's music. lowlatency is the result.

Gameplay
--------
Like osu! the objective is to hit circles in time with the music. The sound energy of the beats (determined by Fourier analysis) scales the point value and color heat of each note (more intense beat = redder note = more points). A personal scoreboard and three difficulties plus a visualizer mode are available.

Graphics
--------
lwbd features dynamic visuals that pulse and move with the music and gameplay. It uses a raycasting light engine.

Platforms
---------
Low Latency targets Android, but since it uses LibGDX as a backend, runs just fine on the desktop (OSX, GNU/Linux, Windows).

Low Latency uses:
+ LibGDX, a FOSS x-platform Java game framework
+ MyID3 for Android for MP3 metadata
+ JVorbisComment for Vorbis metadata
+ Box2DLights for raycasted lighting
+ lwbd, an extensible & extremely simple beat detection library

License
-------
```
Copyright 2015  Quentin Young

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses/.
```
