package com.ThirtyNineEighty.System;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.*;
import android.view.*;

import com.ThirtyNineEighty.Game.Data.Entities.SavedWorldEntity;
import com.ThirtyNineEighty.Game.Map.Map;
import com.ThirtyNineEighty.Game.Menu.*;
import com.ThirtyNineEighty.Game.Worlds.GameWorld;
import com.ThirtyNineEighty.Game.Worlds.IWorld;
import com.ThirtyNineEighty.Renderable.*;

public class GameActivity
  extends Activity
  implements View.OnTouchListener
{
  public static final String SavedWorld = "savedWorld";
  public static final int FPS = 30;

  private boolean pause;
  private Handler handler;
  private GLSurfaceView glView;
  private Runnable drawRunnable;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    GameContext.setMainThread();

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    handler = new Handler();
    Renderer renderer = new Renderer();

    // Initialize game context
    GameContext.setActivity(this);
    GameContext.factory.initialize();
    GameContext.gameProgress.initialize();
    GameContext.content = new Content();
    GameContext.renderer = renderer;

    // OpenGL init
    glView = new GLSurfaceView(this);
    glView.getHolder().setFormat(PixelFormat.RGBA_8888);
    glView.setEGLContextClientVersion(2);
    glView.setEGLConfigChooser(new ConfigChooser());
    glView.setRenderer(renderer);
    glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    // Bind listener
    glView.setOnTouchListener(this);

    // Set view
    setContentView(glView);

    // Set menu and world
    GameContext.content.setMenuAsync(new MainMenu());
    GameContext.content.postEvent(new Runnable()
    {
      @Override
      public void run()
      {
        SavedWorldEntity saved = GameContext.gameProgress.getSavedWorld(SavedWorld);
        if (saved == null)
          return;

        GameContext.mapManager.load(saved.mapName);
        GameContext.content.setWorld(saved.world);

        saved.world.disable();
      }
    });
  }

  public void postEvent(final Runnable r)
  {
    if (GameContext.isGLThread())
    {
      r.run();
      return;
    }

    glView.queueEvent(r);
  }

  @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
  public void sendEvent(final Runnable r)
  {
    if (GameContext.isMainThread())
      throw new IllegalStateException("can't stop main thread (use post)");

    if (GameContext.isGLThread())
    {
      r.run();
      return;
    }

    try
    {
      final Object waitObj = new Object();
      synchronized (waitObj)
      {
        glView.queueEvent(new Runnable()
        {
          @Override public void run()
          {
            r.run();
            synchronized (waitObj)
            {
              waitObj.notifyAll();
            }
          }
        });

        waitObj.wait();
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private void requestRenderer()
  {
    if (drawRunnable == null)
    {
      drawRunnable = new Runnable()
      {
        @Override
        public void run() { requestRenderer(); }
      };
    }

    handler.removeCallbacks(drawRunnable);
    if (!pause)
    {
      handler.postDelayed(drawRunnable, 1000 / FPS);
      glView.requestRender();
    }
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent)
  {
    IMenu menu = GameContext.content.getMenu();
    if (menu == null)
      return false;

    menu.processEvent(motionEvent);
    return true;
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    glView.onPause();
    pause = true;

    // get world info
    Map map = GameContext.mapManager.getMap();
    IWorld world = GameContext.content.getWorld();

    // stop the world
    GameContext.content.stop();
    GameContext.content.reset();

    // save game world if need
    if (world != null && world.needSave())
      GameContext.gameProgress.saveWorld(SavedWorld, map.name, world);
    else
      GameContext.gameProgress.deleteWorld(SavedWorld);

    // reset other managers
    GameContext.resources.clearCache();
    GameContext.gameProgress.close();
    GameContext.mapManager.reset();

    GameContext.setActivity(null);
    GameContext.content = null;
    GameContext.renderer = null;
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    glView.onResume();
    pause = false;
    requestRenderer();
  }

  @Override
  protected void onStop()
  {
    super.onStop();
    pause = true;
    this.finish();
  }
}
