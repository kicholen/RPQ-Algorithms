package spd.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils {

	public static List<Integer> loadFile(String fileName) {
    	File file = new File(fileName);
    	List<Integer> list = new ArrayList<Integer>();
    	
		try (FileInputStream inputStream = new FileInputStream(file)) {
			Scanner scanner = new Scanner(inputStream);
			
			while(scanner.hasNext()) {
				list.add(scanner.nextInt());
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return list;
	}
	
}
