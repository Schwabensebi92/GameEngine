package com.own.gameengine.renderingengine.concept.pipeline;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;

import com.own.gameengine.coreengine.math.Vector3f;
import com.own.gameengine.coreengine.math.matrix.Matrix4f;
import com.own.gameengine.renderingengine.*;
import com.own.gameengine.renderingengine.concept.shader.Shader;
import com.own.gameengine.renderingengine.graphics.*;
import com.own.gameengine.renderingengine.graphics.light.*;
import com.own.gameengine.renderingengine.graphics.object.*;

public abstract class Legacy_RenderingPipeline {

	private RenderingEngine				renderingEngine;	// TODO: Phase out

	private int							id;
	private Shader[]					shaders;
	private HashMap<String, Integer>	uniforms;

	public Legacy_RenderingPipeline() {
		try {
			id = glCreateProgram();
			uniforms = new HashMap<String, Integer>();

			if (id == 0) {
				throw new Exception("Pipeline creation failed: No valid memory location.");
			}

			shaders = createShaders();
			addShaders();
			compile();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public abstract Shader[] createShaders();

	private void addShaders() {
		for (Shader shader : shaders)
			addShader(shader);
	}

	public void bind() {
		glUseProgram(id);
	}

	public void drawMesh(Transform transform, Camera camera, Material material, Mesh mesh) {
		bind();
		updateUniforms(transform, camera, material);
		mesh.draw();
	}

	public void setAttribLocation(String attributeName, int location) {
		glBindAttribLocation(id, location, attributeName);
	}

	public abstract void updateUniforms(Transform transform, Camera camera, Material material);

	public void addUniform(String uniform) {
		try {
			int uniformLocation = glGetUniformLocation(id, uniform);

			if (uniformLocation == 0xFFFFFFFF) {
				throw new Exception("No valid location for uniform: " + uniform);
			}

			uniforms.put(uniform, uniformLocation);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void addShader(Shader shader) {
		glAttachShader(id, shader.getID());
	}

	public void compile() {
		try {
			glLinkProgram(id);

			if (glGetProgrami(id, GL_LINK_STATUS) == 0) {
				throw new Exception(glGetShaderInfoLog(id, 1024));
			}

			glValidateProgram(id);

			if (glGetProgrami(id, GL_VALIDATE_STATUS) == 0) {
				throw new Exception(glGetShaderInfoLog(id, 1024));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void setUniformi(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
	}

	public void setUniformf(String uniformName, float value) {
		glUniform1f(uniforms.get(uniformName), value);
	}

	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());
	}

	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4(uniforms.get(uniformName), true, RenderingEngineUtil.createFlippedBuffer(value));
	}

	public void setUniform(String uniformName, AmbientLight ambientLight) {
		setUniform(uniformName, ambientLight.getIntensity());
	}

	public void setUniform(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void setUniform(String uniformName, DirectionalLight directionalLight) {
		setUniform(uniformName + ".baseLight", directionalLight.getBaseLight());
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}

	public void setUniform(String uniformName, PointLight pointLight) {
		setUniform(uniformName + ".baseLight", pointLight.getBaseLight());
		setUniformf(uniformName + ".attenuation.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".attenuation.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
		setUniform(uniformName + ".position", pointLight.getPosition());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}

	public void setUniform(String uniformName, SpotLight spotLight) {
		setUniform(uniformName + ".pointLight", spotLight.getPointLight());
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}

	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}

	public void setRenderingEngine(RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
	}
}