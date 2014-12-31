package ovh.tgrhavoc.mvpvpgadgets.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

public class JarUtil {
	
	public static List<String> getGadetClasses(String pathToJar){
		ArrayList<String> classes = new ArrayList<String>();
		
		String packageName = "ovh/tgrhavoc/mvpgadgets/gadgets"; // Package in which to find the gadgets
		System.out.println("Getting classes for jar :" + pathToJar + " in package "
				+ packageName);
		
		JarInputStream jarFile = null;
		try{
			jarFile = new JarInputStream(
					new FileInputStream( pathToJar ));
			JarEntry jarEntry = null;
			
			while (true){
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null)
					break;
				if (jarEntry.getName().endsWith("/Gadget.class") ||
						jarEntry.getName().contains("Listener.class") ||
						Pattern.compile("\\$[0-9]").matcher(jarEntry.getName()).find()){
					
					System.out.println("Not adding.. " + jarEntry.getName());
					
				}else if (jarEntry.getName().startsWith(packageName)
					&& jarEntry.getName().endsWith(".class") ){
						classes.add(jarEntry.getName().replace("/", ".").replace(".class", ""));
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				jarFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return classes;
	}
	
}
