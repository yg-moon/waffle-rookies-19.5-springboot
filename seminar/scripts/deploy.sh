cd ~/waffle-rookies-19.5-springboot
git pull
cd seminar
./gradlew bootJar
sudo systemctl start seminar
sudo systemctl restart nginx