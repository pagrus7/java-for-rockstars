Java for hipsters and rock stars
================================

This project is a part of "Java for hipsters and rockstars" talk. It is an example of simple live sound processing with java and streams, a fun way to learn and experiment with java 8.

Currently works with windows/asio4all or linux/jack. The pure javasound implementation exhibits unreasonable latencies, at least on windows.


Prerequisutes
=============

Fearlessness.  
  This program may or may not "just work", for various reasons. Be ready to troubleshoot.

ASIO4ALL driver on Windows.  
  http://www.asio4all.com/ 

JACK on linux.  
  Often it means ensuring "jackd" and "qjackctl" packages are installed. Search for instructions relevant to your distro just in case. 


Building and running
====================
Maven build produces distribution zip under target/*-distro.zip. Unpack the distribution and use "run" or "run-ui" script.

Another way that decreases turnaround time with e.g. Eclipse development is top-level run.bat and run-ui.bat.
Run maven build once, then just save/compile modified class in your IDE and launch the batch file of choice - it will pick up modified classes.

Windows users
=============
When the program starts for the first time, activate asio4all control panel from system tray. Set up exactly one input and one output device. Restart the program then. Advice: don't try aggressive settings right away; start with conservative settings (e.g. large buffer size) initially. That setup may work just fine.


Linux users
===========
Start jackd directly via command line or via qjackctl (click a "Start" button). Once jackd is up and running, start the program itself. In case of audio glitches, check if jackd messages report XRUNs. If so, try more conservative settings and google for advice on minimizing XRUNs.  

MacOS users
===========
JACK Audio Connection Kit provides OS X distributions at http://jackaudio.org/downloads/  
Getting qjackctl to run might be tricky, but command line works just fine. These three commands would provide more details on jackd command-line params

jackd --help  
jackd -d coreaudio --help  
jackd -d coreaudio --list-devices  

