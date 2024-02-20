## CotLSaveSerializer
A Java program to encode and decode Cult of the Lamb save files.

## Application Usage
* Decoding a save file (output to output.json in the current directory by default) :
```batch
java -jar CotLSaveSerializer.jar -mDecode save_X.json
```
* Decoding a save file (output to a spesific file) :
```batch
java -jar CotLSaveSerializer.jar -mDecode save_X.json output.json
```
:pushpin: **These commands will only output the *raw* JSON data, with *no formatting*.**
* Encoding a decoded save file (output to output.json in the current directory by default) :
```batch
java -jar CotLSaveSerializer.jar -mEncode file.json
```
* Encoding a decoded save file (output to a spesific file) :
```batch
java -jar CotLSaveSerializer.jar -mEncode file.json output.json
```

## FAQ
## Q : *where the save files at?*

**A :** On Windows, the save file is most likely in `C:/Users/{user}/AppData/LocalLow/Massive Monster/Cult Of The Lamb/saves`

## Building
Tested in Eclipse using JDK 17. just build it yourself I guess.

## CotLSaveSerializer Class API

```java
public String read(String fileName)
```
Reads and decodes the encoded save file. This will *NOT* work with the decrypted JSON file. If this fails, either because of the magic value mismatch, or other errors, it will be logged into the standard output.

**Params :**
* `String fileName` - The path of the file

**Returns :**
* `String` - The decoded save file data in UTF-8

```java
public boolean write(String fileName, Object data, boolean isEncrypted)
```
Writes the data in a JSON file.

**Params :**
* `String fileName` - The path of the target file
* `Object data` - The data to be encoded **(This just calls .toString() on the object!)**
* `boolean isEncrypted` - Whether or not to use AES encryption for the output file

**Returns :**
* `boolean` - Status code of the save process