# kthomeautomation
This is the central control and management application of the KT Home Automation System. It is based on Spring Boot and uses
mongodb for database storage. The goal of this program is to be as flexible as possible in regards to what it can connect
to, but primarily it just takes sensor readings from any [ktharaspiservice](https://github.com/thepete89/ktharaspiservice/), 
evaluates these readings (using freely configurable scripts that get executed trough java and it's Nashorn javascript engine)
which in response trigger configurable actions that talk back to the raspberries to control certain things like the heating in your home, 
for example. For future implementations it is also planned to have "active" switches that can be controlled directly from the
included web interface and status/information widgets that update automatically via websocket.

#### WARNING: BETA SOFTWARE!
Please be aware that this system is a hobbyist's project and my first step into the field of home automation, and is primarily
developed to control a bunch of infrared heating units used in my home. It is nowhere near finished nor stable for production
and at the moment just a basic implementation to test things out. More features are comming soon, so expect rapid changes. You have
been warned!
