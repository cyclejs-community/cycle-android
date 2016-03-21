
package trikita.anvil;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class RenderableRecyclerViewAdapter
    extends RecyclerView.Adapter<RenderableRecyclerViewAdapter.MountHolder> {

  @Override
  public MountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    FrameLayout root = new FrameLayout(parent.getContext());
    root.setLayoutParams(new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    return new MountHolder(root);
  }

  @Override
  public void onBindViewHolder(final MountHolder h, int position) {
    if (h.mount == null) {
      h.mount = new Anvil.Mount(h.itemView, () -> RenderableRecyclerViewAdapter.this.view(h));
      Anvil.render(h.mount);
    } else {
      Anvil.render(h.mount);
    }
  }

  public static class MountHolder extends RecyclerView.ViewHolder {
    private Anvil.Mount mount;

    public MountHolder(ViewGroup itemView) {
      super(itemView);
    }
  }

  public abstract void view(RecyclerView.ViewHolder holder);
}