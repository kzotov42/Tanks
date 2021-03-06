package com.ThirtyNineEighty.System;

import java.util.List;

public interface IBindable
  extends IEngineObject
{
  List<ISubprogram> getSubprograms();
  List<IRenderable> getRenderables();

  void bind(ISubprogram subprogram);
  void unbind(ISubprogram subprogram);

  void bind(IRenderable renderable);
  void unbind(IRenderable renderable);
}
