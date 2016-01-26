package com.own.sky.resource;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import com.own.sky.renderingengine.concept.shader.uniform.Uniform;


public class ShaderParser {
	
	private final static String UNIFORM_KEYWORD = "Uniform";
	
	public static ArrayList<Uniform<?>> parse(final String sourceCode) throws IOException {
		ArrayList<Uniform<?>> uniforms = new ArrayList<>();
		BufferedReader shaderReader = new BufferedReader(new StringReader(sourceCode));
		
		String line;
		while ((line = shaderReader.readLine()) != null) {
			line.trim(); // Remove extra white space
			if (line.startsWith(UNIFORM_KEYWORD)) {
			
			}
		}
		
		shaderReader.close();
		
		return uniforms;
	}
}