# Music Streamer
<br>
<br> This is a simple music streaming app where a user can upload .wav audio files <br>
to a server to be streamed to the client.
<br><br>

Requirements
-------------
<br>
<br>MYSQL Server/MariaDB 
<br>Java (written in Java 12 but can be compiled for Java 8)
<br>Server must be running on a linux device (only tested on ubuntu/debian based distros)
<br><br>
## Setup
<br>Run the 'CreateDatabase.sh' script to setup the database. The defualt user<br>
is root with the default MYSQL password (YES)
<br>
<br>To add songs to the database, run 'addMusic.sh'. This will add any .wav file<br>
in the specified directory (default /Documents) that are not already in the database <br>
to the database.
<br><br>
Run the java code using main class ServerCore on the server
<br><br>
Run the java code using main class ClientGui on the Client
