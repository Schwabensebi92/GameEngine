package com.own.gameengine.coreengine.math;


public class Quaternion {
	
	//@formatter:off
	/*
	 * x []
	 * y []
	 * z []
	 * w []
	 */
	//formatter:on

	private float x;
	private float y;
	private float z;
	private float w;

	public Quaternion(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion(final Vector3f axis, final float angle) {
		//@formatter:off
		this(
				axis.getX() * (float) Math.sin(angle / 2.0f),
				axis.getY() * (float) Math.sin(angle / 2.0f),
				axis.getZ() * (float) Math.sin(angle / 2.0f),
				(float) Math.cos(angle / 2.0f)
			);
		//@formatter:on
	}
	
	public Quaternion(final Quaternion source) {
		this(source.x, source.y, source.z, source.w);
	}
	
	public Quaternion() {
		this(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Quaternion normalize() {
		float length = length();
		
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		
		return this;
	}
	
	public Quaternion conjugate() {
		x *= -1;
		y *= -1;
		z *= -1;
		
		return this;
	}
	
	public Quaternion mul(final Quaternion quaternion) {
		float newW = w * quaternion.w - x * quaternion.x - y * quaternion.y - z * quaternion.z;
		float newX = x * quaternion.w + w * quaternion.x + y * quaternion.z - z * quaternion.y;
		float newY = y * quaternion.w + w * quaternion.y + z * quaternion.x - x * quaternion.z;
		float newZ = z * quaternion.w + w * quaternion.z + x * quaternion.y - y * quaternion.x;
		
		x = newX;
		y = newY;
		z = newZ;
		w = newW;
		
		return this;
	}
	
	public Quaternion mul(final Vector3f vector) {
		float newW = -x * vector.getX() - y * vector.getY() - z * vector.getZ();
		float newX = w * vector.getX() + y * vector.getZ() - z * vector.getY();
		float newY = w * vector.getY() + z * vector.getX() - x * vector.getZ();
		float newZ = w * vector.getZ() + x * vector.getY() - y * vector.getX();
		
		x = newX;
		y = newY;
		z = newZ;
		w = newW;
		
		return this;
	}
	
	public Quaternion rotate(final Vector3f axis, final float angle) {
		Quaternion rotatedThis = new Quaternion(axis, angle).mul(this).normalize();
		
		x = rotatedThis.x;
		y = rotatedThis.y;
		z = rotatedThis.z;
		w = rotatedThis.w;
		
		return this;
	}
	
	public Vector3f getForwardVector() {
		//@formatter:off
		return new Vector3f(
					2.0f * (getX() * getZ() - getW() * getY()),
					2.0f * (getY() * getZ() + getW() * getX()),
					1.0f - 2.0f * (getX() * getX() + getY() * getY())
				);
		//@formatter:on
	}
	
	public Vector3f getBackVector() {
		return getForwardVector().mul(-1.0f);
	}
	
	public Vector3f getUpVector() {
		//@formatter:off
		return new Vector3f(
					2.0f * (getX() * getY() + getW() * getZ()),
					1.0f - 2.0f * (getX() * getX() + getZ() * getZ()),
					2.0f * (getY() * getZ() - getW() * getX())
				);
		//@formatter:on
	}
	
	public Vector3f getDownVector() {
		return getUpVector().mul(-1.0f);
	}
	
	public Vector3f getRightVector() {
		//@formatter:off
		return new Vector3f(
					1.0f - 2.0f * (getY() * getY() + getZ() * getZ()),
					2.0f * (getX() * getY() - getW() * getZ()),
					2.0f * (getX() * getZ() + getW() * getY())
				);
		//@formatter:on
	}
	
	public Vector3f getLeftVector() {
		return getRightVector().mul(-1.0f);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(final float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(final float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(final float z) {
		this.z = z;
	}
	
	public float getW() {
		return w;
	}
	
	public void setW(final float w) {
		this.w = w;
	}
	
	@Override
	public String toString() {
		return "(" + x + "/" + y + "/" + z + "/" + w + ")";
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof Quaternion))
			return false;
		Quaternion otherQuaternion = (Quaternion) other;
		return (x == otherQuaternion.x) && (y == otherQuaternion.y) && (z == otherQuaternion.z) && (w == otherQuaternion.w);
	}
	
	@Override
	public int hashCode() {
		return (int) (x * 7 + y * 11 + z * 13 + w * 17);
	}
}
