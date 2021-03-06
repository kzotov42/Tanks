package com.ThirtyNineEighty.Game.Subprograms;

import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Game.Menu.MainMenu;
import com.ThirtyNineEighty.Game.Objects.WorldObject;
import com.ThirtyNineEighty.Game.Objects.GameObject;
import com.ThirtyNineEighty.Game.Objects.Properties.GameProperties;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.System.GameContext;
import com.ThirtyNineEighty.System.Subprogram;

import java.util.ArrayList;

public class KillMarkedSubprogram
  extends Subprogram
{
  private static final long serialVersionUID = 1L;

  private ArrayList<GameObject<?, ?>> marked;

  public KillMarkedSubprogram()
  {

  }

  @Override
  protected void onUpdate()
  {
    Map map = GameContext.mapManager.getMap();
    IWorld world = GameContext.content.getWorld();
    GameObject<?, ?> player = (GameObject<?, ?>) world.getPlayer();

    if (map.getState() != Map.StateInProgress)
    {
      unbind();
      GameContext.content.setWorld(null);
      GameContext.content.setMenu(new MainMenu());
      return;
    }

    // Check lose
    if (player.getHealth() <= 0)
    {
      map.setState(Map.StateLose);
      delay(5000);
      return;
    }

    // Check win
    if (marked == null)
    {
      marked = new ArrayList<>();
      ArrayList<WorldObject<?, ?>> worldObjects = new ArrayList<>();
      world.getObjects(worldObjects);

      for (int i = worldObjects.size() - 1; i >= 0; i--)
      {
        WorldObject<?, ?> object = worldObjects.get(i);
        if (object instanceof GameObject)
        {
          GameObject<?, ?> gameObject = (GameObject<?, ?>) object;
          GameProperties properties = gameObject.getProperties();
          if (properties.needKill())
            marked.add(gameObject);
        }
      }
    }

    for (int i = marked.size() - 1; i >= 0; i--)
    {
      GameObject<?, ?> current = marked.get(i);
      if (current.getHealth() <= 0 || !current.isInitialized())
        marked.remove(i);
    }

    if (marked.size() == 0)
    {
      map.setState(Map.StateWin);
      delay(5000);
    }
  }
}
