#!/bin/sh
SELENIUM_SERVER_JAR=./selenium/selenium-server-standalone-2.49.1.jar

java -jar $SELENIUM_SERVER_JAR -Dwebdriver.chrome.driver=./selenium/chromedriver -port 7777 
