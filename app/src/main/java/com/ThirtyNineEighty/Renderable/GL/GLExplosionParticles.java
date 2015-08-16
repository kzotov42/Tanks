package com.ThirtyNineEighty.Renderable.GL;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.ThirtyNineEighty.Common.ILocationProvider;
import com.ThirtyNineEighty.Common.Math.Vector3;
import com.ThirtyNineEighty.Renderable.RendererContext;
import com.ThirtyNineEighty.Renderable.Shaders.Shader;
import com.ThirtyNineEighty.Renderable.Shaders.ShaderExplosionParticles;
import com.ThirtyNineEighty.Resources.Entities.Geometry;
import com.ThirtyNineEighty.Resources.Entities.Texture;
import com.ThirtyNineEighty.Resources.Sources.FileTextureSource;
import com.ThirtyNineEighty.Resources.Sources.ISource;
import com.ThirtyNineEighty.Resources.Sources.HemisphereParticlesSource;
import com.ThirtyNineEighty.System.GameContext;

public class GLExplosionParticles
  extends GLBase
{
  public static final int Sphere = 0;
  public static final int Hemisphere = 1;

  private Texture textureData;
  private Geometry geometryData;
  private int count;
  private float time;
  private float life;

  public GLExplosionParticles(int type, float lifeSec, int count, ILocationProvider<Vector3> provider)
  {
    super(provider);

    this.geometryData = GameContext.resources.getGeometry(getSource(type));
    this.textureData = GameContext.resources.getTexture(new FileTextureSource("particle", false));
    this.count = count;
    this.time = 0;
    this.life = lifeSec;
  }

  private static ISource<Geometry> getSource(int type)
  {
    if (type == Hemisphere)
      return new HemisphereParticlesSource();
    return null;
  }

  @Override
  public int getShaderId()
  {
    return Shader.ShaderParticles;
  }

  @Override
  public void draw(RendererContext context)
  {
    ShaderExplosionParticles shader = (ShaderExplosionParticles) Shader.getCurrent();
    time += GameContext.getDelta();

    setModelMatrix();

    // build result matrix
    Matrix.multiplyMM(modelProjectionViewMatrix, 0, context.getProjectionViewMatrix(), 0, modelMatrix, 0);

    // bind texture to 0 slot
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureData.getHandle());

    // send data to shader
    GLES20.glUniform1i(shader.uniformTextureHandle, 0);
    GLES20.glUniformMatrix4fv(shader.uniformMatrixHandle, 1, false, modelProjectionViewMatrix, 0);
    GLES20.glUniform2f(shader.uniformLifeTimeHandle, life, time);

    // bind data buffer
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, geometryData.getHandle());

    // set offsets to arrays for buffer
    GLES20.glVertexAttribPointer(shader.attributePositionStartHandle, 3, GLES20.GL_FLOAT, false, 40, 0);
    GLES20.glVertexAttribPointer(shader.attributeDirectionVectorHandle, 3, GLES20.GL_FLOAT, false, 40, 12);
    GLES20.glVertexAttribPointer(shader.attributeColorHandle, 4, GLES20.GL_FLOAT, false, 40, 24);

    // enable attribute arrays
    GLES20.glEnableVertexAttribArray(shader.attributePositionStartHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeDirectionVectorHandle);
    GLES20.glEnableVertexAttribArray(shader.attributeColorHandle);

    // validating if debug
    shader.validate();
    geometryData.validate();

    // draw
    GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);

    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, count);

    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    // disable attribute arrays
    GLES20.glDisableVertexAttribArray(shader.attributePositionStartHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeDirectionVectorHandle);
    GLES20.glDisableVertexAttribArray(shader.attributeColorHandle);
  }
}