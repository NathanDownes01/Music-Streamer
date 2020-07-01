# Music Streamer
<br>
<br> This is a simple music streaming app where a user can upload .wav audio files <br>
to a server to be streamed to the client.
<br><br>

Requirements
-------------
<br>
<br>MYSQL Server/MariaDB 
<br>Java 8 
<br>Server must be running on a linux device (only tested on ubuntu/debian based distros)
<br>The sever side application will require the Java MYSQL Driver (pom.xml will get this automatically)
<br><br>

Setup
------
<br>
<br>Run the 'CreateDatabase.sh' script to setup the database. The default user<br>
is root with the default MYSQL password (YES)
<br>
<br>To add songs to the database, run 'addMusic.sh'. This will add any .wav files<br>
in the specified directory (default /Documents) that are not already in the database <br>
to the database. Wav files must have song name,artist and duration in the metadata of the file. 
<br><br>
Change line 27 in client outgoing to the server ip address (ipv4 if on local network, public ip if port forwarded)
<br><br>
Build using pom.xml, this will generate two .jar files (client and server)
<br><br>
Run the java code using main class ServerCore on the server
<br><br>
Run the java code using main class ClientGui on the Client
