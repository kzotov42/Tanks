package com.ThirtyNineEighty.Providers;

import com.ThirtyNineEighty.Game.Objects.Descriptions.VisualDescription;
import com.ThirtyNineEighty.Game.Objects.Tank;
import com.ThirtyNineEighty.Renderable.GL.GLModel;

public class GLModelTankProvider
  extends GLModelGameObjectProvider<Tank>
{
  private static final long serialVersionUID = 1L;

  public GLModelTankProvider(Tank tank, VisualDescription description)
  {
    super(tank, description);
  }

  @Override
  public void set(GLModel.Data data, VisualDescription description)
  {
    super.set(data, description);

    switch (description.id)
    {
    case 1: // turret
      data.localAngles.setFrom(0, 0, object.getRelativeTurretAngle());
      break;
    }

    if (object.getHealth() <= 0)
    {
      data.RedCoeff = 0.2f;
      data.GreenCoeff = 0.2f;
      data.BlueCoeff = 0.2f;
    }
  }
}
