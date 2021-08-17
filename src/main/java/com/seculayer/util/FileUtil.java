package com.seculayer.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FileUtil {

//	private static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static File[] listFiles(File dir) {
	    String[] ss = dir.list();
	    if (ss == null)
	        return null;
	    int n = ss.length;
	    File[] fs = new File[n];
	    for(int i = 0; i < n; i++) {
	        fs[i] = new File(dir.getPath(), ss[i]);
	    }
	    return fs;
    }
	
	public static File[] listFiles(File dir, String fileType) {
		ArrayList<String> fileList = new ArrayList<String>();
	    String[] ss = dir.list();
	    if (ss == null)
	        return null;

	    for(int i = 0; i < ss.length; i++){
			String fileName = ss[i];
			if( fileName.indexOf(fileType) == -1 ) {
				continue;
			}
			fileList.add(fileName);
		}
	    
	    Collections.sort(fileList);
	    
	    File[] fs = new File[fileList.size()];
	    for(int i = 0; i < fileList.size(); i++) {
	    	String fname = fileList.get(i);
	        fs[i] = new File(dir.getPath(), fname);
	    }
	    return fs;
    }
	
	public static ArrayList<String> listFileNames(File dir, String fileType) {
		ArrayList<String> fileList = new ArrayList<String>();
		String[] ss = dir.list();
		if (ss == null)
			return null;
		
		for(int i = 0; i < ss.length; i++){
			String fileName = ss[i];
			if( fileName.indexOf(fileType) == -1 ) {
				continue;
			}
			fileList.add(fileName);
		}
		
		Collections.sort(fileList);

		return fileList;
	}
	
	public static File[] listFiles(File dir, String fileType, String exceptName) {
		ArrayList<String> fileList = new ArrayList<String>();
	    String[] ss = dir.list();
	    if (ss == null)
	        return null;

	    for(int i = 0; i < ss.length; i++){
			String fileName = ss[i];
			if( fileName.indexOf(fileType) == -1 || fileName.equals(exceptName)) {
				continue;
			}
			fileList.add(fileName);
		}
	    
	    Collections.sort(fileList);
	    
	    File[] fs = new File[fileList.size()];
	    for(int i = 0; i < fileList.size(); i++) {
	    	String fname = fileList.get(i);
	        fs[i] = new File(dir.getPath(), fname);
	    }
	    return fs;
    }
	
	public static File[] listFiles2(File dir, String fileType) {
		ArrayList<File> fileList = new ArrayList<File>();
		File[] ss = dir.listFiles();
	    if (ss == null)
	        return null;

	    for(int i = 0; i < ss.length; i++){
			File file = ss[i];
			if( file.getName().indexOf(fileType) == -1 ) {
				continue;
			}
			fileList.add(file);
		}
	    
	    File[] files = new File[fileList.size()];
	    fileList.toArray(files);
		Arrays.sort(files, new Comparator() {
			public int compare(final Object o1, final Object o2) {
				return new Long(((File) o1).lastModified()).compareTo(new Long(((File) o2).lastModified()));
			}
		});
		return files;
    }
	
	public static boolean copy(File fOrg, File fTarget) {
		try {
			FileInputStream inputStream = new FileInputStream(fOrg);
			FileOutputStream outputStream = new FileOutputStream(fTarget, true);
			FileChannel fcin =  inputStream.getChannel();
			FileChannel fcout = outputStream.getChannel();
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
			fcout.close();
			fcin.close();
			outputStream.close();
			inputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void copys(File source, File dest) throws IOException {

        File parentFile = null;

        if (source.isDirectory()) {
            if (dest.isDirectory()) {
				throw new IOException("file copy error : aleady exist directory : " + dest + "");
            } else if (dest.exists()) {
                throw new IOException("file copy error : aleady exist file : " + dest + "");
            } else {
                dest = new File(dest, source.getName());
                dest.mkdirs();
            }
        }
        if (source.isFile() && dest.isDirectory()) {
            parentFile = new File(dest.getAbsolutePath());
            dest = new File(dest, source.getName());
        } else {
            parentFile = new File(dest.getParent());
        }

        parentFile.mkdirs();

        if (!source.canRead()) {
	        throw new IOException("Cannot read file '" + source + "'.");
        }

	    if (dest.exists() && (!dest.canWrite())) {
	        throw new IOException("Cannot write to file '" + dest + "'.");
	    }

        BufferedInputStream in =
	        new BufferedInputStream( new FileInputStream(source) );
	    BufferedOutputStream out =
	        new BufferedOutputStream( new FileOutputStream(dest) );
       
		byte[] buffer = new byte[1024];
	    int read = -1;
	    
		while ((read = in.read(buffer, 0, 1024)) != -1)
	        out.write(buffer, 0, read);
	   
		out.flush();
	    out.close();
	    in.close();
    }
	
	public static boolean fileMove(File srcFile, String fileName) {
	    boolean success = srcFile.renameTo(new File(fileName));
		return success;
	}
	
	public static void fileAppend(String fileName, String appendText) {
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter("filename", true));
	        out.write(appendText);
	        out.newLine();
	        out.close();
	    } catch (IOException e) {
	    }
	}
	
	public static String getFileMD5Hash(File f) {
		StringBuffer buffer = new StringBuffer();
		
		try {
			DigestInputStream dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(f)), MessageDigest.getInstance("MD5"));
			byte[] buf = new byte[1024];
			while (dis.read(buf) <= 0) {
				break;
			}
			dis.close();

			MessageDigest md5 = dis.getMessageDigest();
			byte[] digest = md5.digest();
			for (int i = 0; i < digest.length; i++) {
				buffer.append(Integer.toHexString(255 & (char) digest[i]));
			}
		} catch (Exception ex) {
			//TODO : 
			return null;
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Invalid Parameter!");
			System.out.println("ex> FileUtil pom.xml");
			return;
		}
		
		File f = new File(args[0]);
		if(!f.isFile()) {
			System.out.println(f.getAbsolutePath() + " is not exist!");
			return;
		}
		System.out.println("> Hash=[" + getFileMD5Hash(f) + "]");
	}
}
