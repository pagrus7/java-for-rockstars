Java for hipsters and rock stars
================================

This project is a part of "Java for hipsters and rockstars" talk. It is an example of simple live sound processing with java and streams, a fun way to learn and experiment with java 8.

Currently works with windows/asio4all; the pure javasound implementation exhibits unreasonable latencies on windows.


Prerequisutes
=============

Fearlessness.
  This program may or may not "just work", for various reasons. Be ready to troubleshoot.

ASIO4ALL driver
  Download and install the at http://www.asio4all.com/ 



Building and running
====================
Maven build produces distro zip under target/, so one way would be unpacking and running that.

Another way that decreases turnaround time with e.g. Eclipse is top-level run.bat and run-ui.bat. One runs command-line version, other starts a visualization UI. 
Run maven build once, then just save/compile modified class in your IDE and launch the batch file of choice.

When the program is started for the first time, activate the asio4all control panel from the system tray and set up exactly one input and one output device. 
Advice: don't try aggressive settings right away; start with conservative settings (e.g. large buffer size) initially. That setup may work just fine.


Linux users
===========

If you are willing to port or build something similar on linux and don't know where to start, try https://github.com/jaudiolibs/ .
In my experiments both jack and javasound backend provided reasonable latencies.


