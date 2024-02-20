package com.testnow720.cotlsavereader;

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
				
				String data2 = saveSerializer.read(commandArg[0]);
				if (data2 == null) {
					System.out.println("There was an error reading a file to be encoded.");
					return;
				}
				
				boolean status = saveSerializer.write(path2, saveSerializer.read(commandArg[0]), true);
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
