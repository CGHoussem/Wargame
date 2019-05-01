package com.pxcode.main;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

final public class ResourceLoader {
	public static InputStream load(String path) {
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		if (input == null) {
			input = ResourceLoader.class.getResourceAsStream("/" + path);
		}
		return input;
	}
	
}
