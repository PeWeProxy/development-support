package sk.fiit.peweproxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {
	public static void cp(File in, File out) throws IOException {
		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}
	
	public static void cp_r(File source, File target) throws IOException {
		if(!source.isDirectory()) {
			throw new IllegalArgumentException(source.getAbsolutePath() + " is not a directory");
		}
		
		if(!target.exists()) {
			if(!target.mkdir()) {
				throw new IllegalStateException("Could not create a directory: " + target.getAbsolutePath());
			}
		}
		
		for(File f : source.listFiles()) {
			File targetF = new File(target.getAbsolutePath() + File.separator + f.getName()); 
			if(f.isFile()) {
				cp(f, targetF);
			}
			if(f.isDirectory()) {
				cp_r(f, targetF);
			}
		}
	}
	static public boolean rm_r(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					rm_r(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}
}