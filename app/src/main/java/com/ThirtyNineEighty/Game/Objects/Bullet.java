package com.ThirtyNineEighty.Game.Objects;

import com.ThirtyNineEighty.Game.Objects.Descriptions.GameDescription;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;

public class Bullet
  extends GameObject<GameDescription, GameProperties>
{
  private static final long serialVersionUID = 1L;

  public Bullet(String type)
  {
    super(type, new GameProperties());
  }

  @Override
  public void collide(WorldObject<?, ?> object)
  {
    if (!(object instanceof GameObject))
      return;

    GameObject<?, ?> target = (GameObject<?, ?>) object;
    GameDescription bulletDescription = getDescription();

    target.subtractHealth(bulletDescription.getDamage());
  }
}
