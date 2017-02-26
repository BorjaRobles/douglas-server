# Douglas server

The server is available on port 8080 unless specified otherwise in the arguments

This can simply be controlled by passing a different port as argument `java -jar douglas.jar 4040`

## How to install

Spin up an Ubuntu server and run the following commands to setup the basic required software
```
# Add Chrome repo to Ubuntu package manager
wget -q -O - "https://dl-ssl.google.com/linux/linux_signing_key.pub" | sudo apt-key add -

echo 'deb http://dl.google.com/linux/chrome/deb/ stable main' >> /etc/apt/sources.list

apt-get update
apt-get -y install openjdk-8-jre google-chrome-stable xvfb unzip

# Download and copy the ChromeDriver and Selenium server to /usr/local/bin
cd /tmp
wget "https://chromedriver.storage.googleapis.com/2.27/chromedriver_linux64.zip"
wget "https://selenium-release.storage.googleapis.com/3.0/selenium-server-standalone-3.0.1.jar"
unzip chromedriver_linux64.zip
mv chromedriver /usr/local/bin
mv selenium-server-standalone-3.0.1.jar /usr/local/bin
sudo chmod +x /usr/local/bin/chromedriver
```

Hereafter install MySql and start it

```
apt-get install mysql-server
mysql_secure_installation
sudo systemctl mysql start
```

Connect to the MySql instance and open the CLI using `mysql -u root -p`
Create a new user and grant privileges

```
CREATE USER 'douglas'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON * . * TO 'douglas'@'localhost';
```

Download the Douglas server distributable from Github using
`wget "https://github.com/madskonradsen/douglas-server/raw/master/dist/douglas.jar"`

Start Xvfb, and Selenium in the background and finally launch Douglas server
Xvfb makes sure that we can start Chrome without installing a full-fledged user environment
```
export DISPLAY=:10
Xvfb :10 -screen 0 1366x768x24 -ac &
nohup java -jar /usr/local/bin/selenium-server-standalone-3.0.1.jar &
java -jar douglas.jar