AdemcoAlarm Controller Readme

What it does:

Allows you to control your home alarm system using any web-enabled mobile, or non-mobile device.
Using the easy to use interface, you can:

- Check the current status of your alarm
- Arm/disarm the alarm
- Recieve alerts whenever your alarm is setoff via text message

What you'll need to make it work:

1) Server that can run a Java container (i.e. Tomcat) with access to internet
2) AD2USB Adapter from Nutech (http://www.nutech.com)
3) A home alarm system with an extra keypad lead run somewhere near your server/AD2USB adapter


Setup:

1) Connect and setup your AD2USB adapter as per manufacturer's directions
2) Connect adapter via USB to your server's USB port
3) Install necessary drivers, following Nutech's directions
4) Take note of the COM port that the adapter uses
5) Modify the your app container's deviceComPort JNDI setting to reflect the COM port (default is COM4)
6) Using Ant, package your app into a WAR file
7) Deploy the WAR to your favorite app server (tested on Tomcat 6/7)
8) Enjoy!
9) Check local log files for any issues that may arise
