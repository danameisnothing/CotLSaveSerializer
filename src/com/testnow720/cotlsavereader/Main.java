package com.testnow720.cotlsavereader;

import java.io.FileReader;
import java.io.IOException;

import com.testnow720.cotlsavereader.cotlsavereadwriter.CotLSaveSerializer;

public class Main {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage : CotLSaveReader.jar -m{mode}");
			return;
		}
		
		CotLSaveSerializer saveSerializer = new CotLSaveSerializer();
		
		String[] commandArg = new String[args.length - 1];
		System.arraycopy(args, 1, commandArg, 0, args.length - 1);
		
		switch (args[0]) {
			case "-mDecode":
				if (commandArg.length != 1 && commandArg.length != 2) {
					System.out.println("Invalid argument length.");
					return;
				}
				
				String data = saveSerializer.read(commandArg[0]);
				if (data == null) {
					System.out.println("There was a problem decoding the save file.");
					return;
				}
				
				String path = (commandArg.length == 2 && commandArg[1] != null) ? commandArg[1] : System.getProperty("user.dir") + "\\output.json";
				
				// it's not meant to do this but oh well
				if (!saveSerializer.write(path, data, false)) {
					System.out.println("There was a problem saving the decoded save file.");
					return;
				}
				System.out.println("Successfully decoded and written file at " + path);
				break;
			case "-mEncode":
				if (commandArg.length != 1 && commandArg.length != 2) {
					System.out.println("Invalid argument length.");
					return;
				}
				
				String path2 = (commandArg.length == 2 && commandArg[1] != null) ? commandArg[1] : System.getProperty("user.dir") + "\\output.json";
				
				FileReader reader = null;
				StringBuilder data2 = new StringBuilder();
				try {
					reader = new FileReader(commandArg[0]);
					
					int curr = -1;
					while ((curr = reader.read()) != -1) {
						data2.append((char) curr);
					}
				} catch (IOException e) {
					System.out.println("There was an error reading a file.");
				} finally {
					try {
						if (reader != null) {
							reader.close();
						}
					} catch (IOException e) {}
				}
				
				if (data2.toString() == null) {
					System.out.println("There was an error reading a file to be encoded.");
					return;
				}
				
				boolean status = saveSerializer.write(path2, data2.toString(), true);
				if (!status) {
					System.out.println("Failed to write encrypted save file to path.");
					return;
				}
				System.out.println("Successfully encoded and written file at " + path2);
				break;
			default:
				System.out.println("Invalid command.");
				break;
		}
	}

}
