default:	all

all:
	rm -rf ./build
	mkdir build
	find ./src -name *.java > sources_list.txt
	javac -cp .:./lib/* @sources_list.txt -d ./build

clean:
	rm -rf ./build
