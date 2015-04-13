
export INSTALL_DIR=/home/pocan101/projects/Scheduler
export IP=192.168.56.101
cd $INSTALL_DIR 
mvn clean install
mvn clean site
cd $INSTALL_DIR/target/site
echo "navigate to  http://$IP:8000/apidocs/index.html"
 python -m SimpleHTTPServer
