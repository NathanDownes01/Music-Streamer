#!/bin/bash

#navigate to songs
cd /Documents

#loop through songs
for name in *.wav;do
	#get song title
	title=$(ffprobe -loglevel error -show_entries format_tags=title -of default=noprint_wrappers=1:nokey=1 "$name")
	#sql escape
	title=${title//\'/\'\'}
	#get song atrist
	artist=$(ffprobe -loglevel error -show_entries format_tags=artist -of default=noprint_wrappers=1:nokey=1 "$name")
	#sql escape
	artist=${artist//\'/\'\'}
	#get song length
	duration=$(ffprobe -i "$name" -show_entries format=duration -v quiet -of csv="p=0")
	#removes decimal point
	duration=${duration%.*}
	#check if already in db
	query="SELECT Songid FROM Songs WHERE Title='${title}' AND Artist='${artist}';"
	check=$(mysql -u root -pYES -D Music -e "${query}" -B --skip-column-names)
	#check if result is empty
	echo "Check ${check}"
	if [ -z "$check" ]; then
		#insert into database
		query="INSERT INTO Songs(Title,Artist,Duration) VALUES ('${title}','${artist}',${duration});"
		echo $query
		mysql -u root -pYES -D Music -e "${query}"
		#rename file to id.wav
		query="SELECT Songid FROM Songs WHERE Title='${title}' AND Artist='${artist}';"
		echo $query
		results=$(mysql -u root -pYES -D Music -e "${query}" -B --skip-column-names)
		echo "Editing ${results}" 
		mv -v "${name}" "${results}.wav"
	fi
done




