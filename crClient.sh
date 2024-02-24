find -name "*.java" > javafiles.txt
javac @javafiles.txt
java -cp "src" src/multiplayer/networking/client/MPCLient.java