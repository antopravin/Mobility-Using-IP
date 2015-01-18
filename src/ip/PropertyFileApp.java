package ip;
import java.util.*;
import java.io.*;

public class PropertyFileApp {
	public static void writeProperty(String key, String value) {
		Properties prop = new Properties();
        prop.setProperty(key, value);
        try {
        	FileOutputStream fileOut = new FileOutputStream("src\\ip\\IPMobility.properties",true);
        	//FileOutputStream fileOut = new FileOutputStream("/home/004/a/ax/axf130930/MobilityUsingIP/src/ip/IPMobility.properties",true);
        	prop.store(fileOut, "hi");
			System.out.println("Writen succseefully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static String readProperty(String key) {
		Properties prop = new Properties();
		try {
			FileInputStream fileIn = new FileInputStream("src\\ip\\IPMobility.properties");
			//FileInputStream fileIn = new FileInputStream("/home/004/a/ax/axf130930/MobilityUsingIP/src/ip/IPMobility.properties");
        	
			prop.load(fileIn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return prop.getProperty(key);
        
	}
//	public static void main(String[] args) {
//		try {
//			//System.out.println(readProperty("name"));
//			writeProperty("name5", "karthik");
//			System.out.println(readProperty("name5")); 
//		}catch(Exception ex) {
//			System.out.println(ex.getMessage());
//		}
//	}

}
