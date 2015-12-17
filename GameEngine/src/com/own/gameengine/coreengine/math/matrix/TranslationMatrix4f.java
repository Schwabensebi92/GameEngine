package com.own.gameengine.coreengine.math.matrix;


import com.own.gameengine.coreengine.math.Vector3f;


public class TranslationMatrix4f extends Matrix4f {
	
	public TranslationMatrix4f(final Vector3f translation) {
		super();
		
		//@formatter:off
		elements[0][0] = 1;					elements[1][0] = 0;					elements[2][0] = 0;					elements[3][0] = 0;
		elements[0][1] = 0;					elements[1][1] = 1;					elements[2][1] = 0;					elements[3][1] = 0;
		elements[0][2] = 0;					elements[1][2] = 0;					elements[2][2] = 1;					elements[3][2] = 0;
		elements[0][3] = translation.getX();elements[1][3] = translation.getY();elements[2][3] = translation.getZ();elements[3][3] = 1;
		//@formatter:on
	}
}
