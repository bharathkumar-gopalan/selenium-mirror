# selenium-mirror(Documenation - Work in progress, Please bear with me and has a few issues :) . I am working on it  ) 
Selenium mirror is a fun proof of concept that replays in real time what you do on a browser using selenium! This project is composed of two parts a server and a chrome plugin . Documentation and bug fixes on the way !

# Introduction 
A while ago a friend of mine asked if there is a way to replay what is is doing in a browser in another browser in real time . I wrote this proof of concept 

This project consists of two parts 
1. A spring boot based Restful service that provides endpoints that drive a browser via selenium grid 
2. A chrome plugin that records the user action and sends the request to the REST service 

# How to use
## Building and starting the server component 
This requires JDK 1.8 and maven 3 to be installed in the local machine
1. Go to the server root directory and simply type ```mvn clean install```
2. This will build the jar under ```mirror-service/target/``` directory 
3. Start the service by simply typing ```java -jar mirror-service.jar```. This should start the mirror service and it should be listening to ```http://localhost:8080


##  Starting selenium grid in standalone mode 
1. Download selenium server and chrome driver to a given directory and start the service . The selenium server should be listening to http://localhost:4444/wd/hub

## chrome plugin 
The chrome plugin is located under the [chrome-plugin](./chrome-plugin) folder. To install it use the following steps 
1. go to chrome://extensions and enable developer mode 
2. Click on the load unpacked button and select the chrome-plugin folder and click select to install the same 
3. Once installed , you should see the selenium mirror icon on the browser tab









