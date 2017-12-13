package org.esim.util;
import java.io.InputStream;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = ClassLoader.class.getClass().getResourceAsStream(fileName);
                Scanner scanner = new Scanner(in, "UTF-8")) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
    
    public static String pointAsString(float...x) {
    	StringBuilder builder = new StringBuilder();
    	builder.append("[");
    	for(float f : x) {
    		builder.append(f);
    		builder.append(", ");
    	}
    	builder.deleteCharAt(builder.length() - 1);
    	builder.deleteCharAt(builder.length() - 2);
    	builder.append("]");
    	return builder.toString();
    }


}