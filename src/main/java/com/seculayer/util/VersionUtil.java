package com.seculayer.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VersionUtil {
	static void example() {
		System.out.println("Invalid Option!");
		System.out.println("Usage> VersionUtil [OPTION]");
		System.out.println();
		System.out.println("Options :");
		System.out.println("	-p	Print Version Information");
		System.out.println("	-h	Print History");
	}
	
	public String getVersion() {
		String result = "";
		InputStream is    = null;
		BufferedReader br = null;
		
		try {
			is = getClass().getResourceAsStream("/version.info");
		    br = new BufferedReader(new InputStreamReader(is));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if(line.startsWith("VERSION=")) {
		    		result = line;
					break;
		    	}
		    }
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(is != null) is.close();
				if(br != null) br.close();
			} catch(Exception e) {}
		}
		return result;
	}
	
	public String getRevision() {
		String result = "";
		InputStream is    = null;
		BufferedReader br = null;
		
		try {
			is = getClass().getResourceAsStream("/version.info");
			br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				if(line.startsWith("REVISION=")) {
					result = line;
					break;
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(is != null) is.close();
				if(br != null) br.close();
			} catch(Exception e) {}
		}
		return result;
	}
	
	void printVersion() {
		InputStream is    = null;
		BufferedReader br = null;
		
		try {
			is = this.getClass().getResourceAsStream("/version.info");
		    br = new BufferedReader(new InputStreamReader(is));
		    String line;
		    System.out.println("----------------------------------------");
		    System.out.println("[ eyeCloudAI Version ]");
		    System.out.println("----------------------------------------");
		    while ((line = br.readLine()) != null) {
		    	if(line.startsWith("SYSTEM=") || line.startsWith("VERSION=") || line.startsWith("REVISION=")) {
		    		System.out.println(line);
		    	}
		    }
		    System.out.println("----------------------------------------");
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(is != null) is.close();
				if(br != null) br.close();
			} catch(Exception e) {}
		}
	}
	
	void printHistory() {
		InputStream is    = null;
		BufferedReader br = null;
		
		try {
			is = this.getClass().getResourceAsStream("/version.info");
		    br = new BufferedReader(new InputStreamReader(is));
		    String line;
		    int cnt = 1;
		    System.out.println("----------------------------------------");
		    System.out.println("[ Change History ]");
		    System.out.println("----------------------------------------");
		    while ((line = br.readLine()) != null) {
		    	if((cnt++) > 10) System.out.println(line);
		    }
		    System.out.println("----------------------------------------");
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(is != null) is.close();
				if(br != null) br.close();
			} catch(Exception e) {}
		}
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			example();
			return;
		}
		
		VersionUtil v = new VersionUtil();
		if(args[0].equals("-p")) {
			v.printVersion();
		} else if(args[0].equals("-h")) {
			v.printHistory();
		} else example();
	}
}
