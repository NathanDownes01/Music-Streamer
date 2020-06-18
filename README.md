# Music Streamer
<br>
<br> This is a simple music streaming app where a user can upload .wav audio files <br>
to a server to be streamed to the client.
<br><br>

Requirements
-------------
<br>
<br>MYSQL Server/MariaDB 
<br>Java on both client and sever
<br>Server must be running on a linux device (only tested on ubuntu/debian based distros)
<br>The sever side application will require the Java MYSQL Driver
<br><br>

Setup
------
<br>
<br>Run the 'CreateDatabase.sh' script to setup the database. The defualt user<br>
is root with the default MYSQL password (YES)
<br>
<br>To add songs to the database, run 'addMusic.sh'. This will add any .wav file<br>
in the specified directory (default /Documents) that are not already in the database <br>
to the database.
<br><br>
Run the java code using main class ServerCore on the server
<br><br>
change line 27 in client outgoing to the server ip address (ipv4 if on local network, public ip if port forwarded)
<br><br>
Run the java code using main class ClientGui on the Client
