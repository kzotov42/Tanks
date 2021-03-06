package com.ThirtyNineEighty.Resources.Entities;

import android.opengl.GLES20;

import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Resources.MeshMode;
import com.ThirtyNineEighty.System.GameContext;

import java.nio.FloatBuffer;

public class Geometry
{
  private MeshMode mode;

  private int handle;
  private FloatBuffer data;

  private int trianglesCount;
  private Vector3 position;
  private Vector3 angles;

  public Geometry(int handle, int trianglesCount, Vector3 position, Vector3 angles)
  {
    this.handle = handle;
    this.trianglesCount = trianglesCount;
    this.mode = MeshMode.Static;
    this.data = null;

    this.position = new Vector3(position);
    this.angles = new Vector3(angles);
  }

  public Geometry(FloatBuffer data, int trianglesCount, Vector3 position, Vector3 angles)
  {
    this.handle = 0;
    this.trianglesCount = trianglesCount;
    this.mode = MeshMode.Dynamic;
    this.data = data;

    this.position = new Vector3(position);
    this.angles = new Vector3(angles);
  }

  public MeshMode getMode() { return mode; }

  public int getHandle() { return handle; }
  public FloatBuffer getData()
  {
    if (mode != MeshMode.Dynamic)
      throw new IllegalStateException("not right mode");
    return data;
  }

  public int getTrianglesCount() { return trianglesCount; }
  public Vector3 getPosition() { return position; }
  public Vector3 getAngles() { return angles; }

  public void updateData(int handle, int trianglesCount, Vector3 position, Vector3 angles)
  {
    if (mode != MeshMode.Static)
      throw new IllegalStateException("not right mode");

    this.handle = handle;
    this.trianglesCount = trianglesCount;

    this.position = position == null ? null : new Vector3(position);
    this.angles = angles == null ? null : new Vector3(angles);
  }

  public void updateData(FloatBuffer data, int trianglesCount, Vector3 position, Vector3 angles)
  {
    if (mode != MeshMode.Dynamic)
      throw new IllegalStateException("not right mode");

    this.data = data;
    this.trianglesCount = trianglesCount;
    this.position = position == null ? null : new Vector3(position);
    this.angles = angles == null ? null : new Vector3(angles);
  }

  public void validate()
  {
    if (!GameContext.isDebuggable() || mode != MeshMode.Static)
      return;

    if (!GLES20.glIsBuffer(handle))
      throw new IllegalStateException("Buffer handle deprecated");
  }
}
