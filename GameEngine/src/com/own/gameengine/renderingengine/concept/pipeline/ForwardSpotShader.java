package com.own.gameengine.renderingengine.concept.pipeline;

import com.own.gameengine.renderingengine.graphics.*;
import com.own.gameengine.renderingengine.graphics.light.SpotLight;
import com.own.gameengine.renderingengine.graphics.object.Material;

public class ForwardSpotShader extends ForwardLightPipeline<SpotLight> {

	public ForwardSpotShader() {
		super();

		addVertexShaderFromFile("forward_spot.vs");
		addFragmentShaderFromFile("forward_spot.fs");

		compileShader();

		addUniform("eyePosition");

		addUniform("transform");
		addUniform("transformProjected");

		addUniform("specularIntensity");
		addUniform("specularExponent");

		addUniform("spotLight.pointLight.baseLight.color");
		addUniform("spotLight.pointLight.baseLight.intensity");
		addUniform("spotLight.pointLight.attenuation.constant");
		addUniform("spotLight.pointLight.attenuation.linear");
		addUniform("spotLight.pointLight.attenuation.exponent");
		addUniform("spotLight.pointLight.position");
		addUniform("spotLight.pointLight.range");

		addUniform("spotLight.direction");
		addUniform("spotLight.cutoff");
	}

	public void updateUniforms(Transform transform, Camera camera, Material material) {
		material.getTexture().bind();

		setUniform("eyePosition", camera.getPosition());

		setUniform("transform", transform.getTransformation());
		setUniform("transformProjected", transform.getProjectedTransformation(camera));

		setUniformf("specularIntensity", material.getSpecularIntensity());
		setUniformf("specularExponent", material.getSpecularExponent());

		setUniform("spotLight", getCurrentLight());
	}
}
