package com.ThirtyNineEighty.Renderable.Shaders;

import android.opengl.GLES20;

public class Shader3D
  extends Shader
{
  public int attributePositionHandle;
  public int attributeTexCoordHandle;
  public int attributeNormalHandle;

  public int uniformMatrixProjectionHandle;
  public int uniformMatrixHandle;
  public int uniformTextureHandle;
  public int uniformLightVectorHandle;
  public int uniformColorCoefficients;

  @Override
  public void compile()
  {
    compile("Shaders/3D.vert", "Shaders/3D.frag");
  }

  @Override
  protected void getLocations()
  {
    //get shaders handles
    attributePositionHandle       = GLES20.glGetAttribLocation(programHandle, "a_position");
    attributeTexCoordHandle       = GLES20.glGetAttribLocation(programHandle, "a_texcoord");
    attributeNormalHandle         = GLES20.glGetAttribLocation(programHandle, "a_normal");
    uniformMatrixProjectionHandle = GLES20.glGetUniformLocation(programHandle, "u_modelViewProjectionMatrix");
    uniformMatrixHandle           = GLES20.glGetUniformLocation(programHandle, "u_modelMatrix");
    uniformLightVectorHandle      = GLES20.glGetUniformLocation(programHandle, "u_light");
    uniformTextureHandle          = GLES20.glGetUniformLocation(programHandle, "u_texture");
    uniformColorCoefficients      = GLES20.glGetUniformLocation(programHandle, "u_colorCoefficients");
  }
}
