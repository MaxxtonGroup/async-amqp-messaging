# Async AMQP Messaging
Development of a Java (Spring) library which expands the current Spring AMQP components.

It has support for:
* Communication
* Endpoint control
* Status control
* Logging for muliple logsystems
* Monitoring through Zabbix (using [Zabbix/J](http://quigley.com/software/zabbixj/)
* Message Types
* Highly Available and Failover
* Callbacks

Use of this component/library in your project should make it easier to communicate between clients.

If your thinking about using this library in your next project feel free to use it as you like. Feel free to inform me about the details :D .

Dependencies
---
First of all the library has to be build using Java 1.8.
This dependency comes with the use of [Lambda Expressions](http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/Lambda-QuickStart/index.html#overview) which require JDK 8.

The library makes use of some Maven dependencies.
All are listed below.

| Name          | GroupId				  | Version       |
| ------------- | ----------------------- |:-------------:|
| Spring Rabbit | org.springframework.amqp| 1.4.3.RELEASE |
| Zabbix/J		| com.quigley			  | 1.0.1		  |
| SLF4J API     | org.slf4j				  | 1.7.10        |
| JUnit			| junit					  | RELEASE       |

Throughout the development of the project this list may grow.

Installation
---
There are two ways to install and configure the library to work for your project.
One uses the JAR file specified in the release and the other one the source code.

##### Option One
* Download the jar file from [the latest release](https://github.com/MaxxtonGroup/async-amqp-messaging/releases).
* In Eclipse right click your project and select **Properties**.
* Goto **Java Build Path** and select the tab **Libraries**.
* Click **Add External JARs** and locate the previously downloaded JAR file. (Moving the file in your project space is recommended).

##### Option Two
* Clone the project using a Git tool or download the sources ZIP file.
* Unpack the sources and place them where you like.
* Right click the **Package Explorer**.
* Click **Import**.
* Under the **Maven** folder choose **Existing Maven Projects** and click **Next**.
* Browse to the location where you checked out the git repository by clicking **Browse**.
* A new line comes up in the **Projects section**. Check the box next to it.
* Click **Finish**.

Usage
---
Using the library is quite simple as long as you don't violate any design principles.
You can create a new Messenger object by running this code.
```
MessagingFactory objFactory = MessagingFactory.getInstance();
Messenger objMessenger = objFactory.createMessenger("my-messenger-name");
objMessenger.loadConfiguration(getClass().getClassLoader().getResource("custom.properties").getPath());
objMessenger.start();
```
This creates a Messenger instance with the name **my-messenger-name**.
It also loads a custom configuration properties file.
Be sure to call the **start** method in oder to send and receive messages.
Any calls to the same name will return the same object.

License
---
The Async AMQP Messaging project makes use of the Spring AMQP project. Due this dependency this wil be released under the terms of **Apache Licence Version 2.0**.
Also the dependency Zabbix/J commes with the licence restriction of the **GNU Library General Public License**.