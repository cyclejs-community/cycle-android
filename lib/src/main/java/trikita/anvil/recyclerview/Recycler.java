package trikita.anvil.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import trikita.anvil.Anvil;
import trikita.anvil.DSL;
import trikita.anvil.RenderableRecyclerViewAdapter;

public final class Recycler {
  private Recycler() {}

  public static void view(Anvil.Renderable r) {
    DSL.v(RecyclerView.class, r);
  }

  public static Void adapter(RecyclerView.Adapter a) {
    return adapter(a, false);
  }

  public static Void adapter(RecyclerView.Adapter a, boolean recycle) {
    return DSL.attr(AdapterFunc.instance, new AdapterFunc.Param(a, recycle));
  }

  public static Void layoutManager(RecyclerView.LayoutManager layout) {
    return DSL.attr(LayoutManagerFunc.instance, layout);
  }

  public static Void hasFixedSize(boolean hasFixedSize) {
    return DSL.attr(HasFixedSizeFunc.instance, hasFixedSize);
  }

  public static Void itemAnimator(RecyclerView.ItemAnimator itemAnimator) {
    return DSL.attr(ItemAnimatorFunc.instance, itemAnimator);
  }

  private static final class AdapterFunc implements Anvil.AttrFunc<AdapterFunc.Param> {
    static final class Param {
      public final RecyclerView.Adapter adapter;
      public final Boolean recycle;
      private Param(RecyclerView.Adapter adapter, Boolean recycle) {
        this.adapter = adapter;
        this.recycle = recycle;
      }
      public int hashCode() {
        int result = adapter != null ? adapter.hashCode() : 0;
        result = 31 * result + (recycle != null ? recycle.hashCode() : 0);
        return result;
      }
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Param param = (Param) o;
        return !(adapter != null ? !adapter.equals(param.adapter) : param.adapter != null) &&
            !(recycle != null ? !recycle.equals(param.recycle) : param.recycle != null);
      }
    }
    public static final AdapterFunc instance = new AdapterFunc();
    public void apply(View view, AdapterFunc.Param param, AdapterFunc.Param oldParam) {
      if (view instanceof RecyclerView) {
        if (oldParam == null) {
          ((RecyclerView) view).setAdapter(param.adapter);
        } else {
          ((RecyclerView) view).swapAdapter(param.adapter, param.recycle);
        }
      }
    }
  }
  private static final class LayoutManagerFunc implements Anvil.AttrFunc<RecyclerView.LayoutManager> {
    public static final LayoutManagerFunc instance = new LayoutManagerFunc();
    public void apply(View view, RecyclerView.LayoutManager layoutManager, RecyclerView.LayoutManager t1) {
      if (view instanceof RecyclerView) {
        ((RecyclerView) view).setLayoutManager(layoutManager);
      }
    }
  }

  private static final class HasFixedSizeFunc implements Anvil.AttrFunc<Boolean> {
    public static final HasFixedSizeFunc instance = new HasFixedSizeFunc();
    public void apply(View view, Boolean fixedSize, Boolean b) {
      if (view instanceof RecyclerView) {
        ((RecyclerView) view).setHasFixedSize(fixedSize);
      }
    }
  }

  private static final class ItemAnimatorFunc implements Anvil.AttrFunc<RecyclerView.ItemAnimator> {
    public static final ItemAnimatorFunc instance = new ItemAnimatorFunc();
    public void apply(View view, RecyclerView.ItemAnimator itemAnimator, RecyclerView.ItemAnimator oldItemAnimator) {
      if (view instanceof RecyclerView) {
        ((RecyclerView) view).setItemAnimator(itemAnimator);
      }
    }
  }


  //
  // TODO: these are the candidates for attribute setters:
  //
  //    setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate accessibilityDelegate)
  //    childDrawingOrderCallback(RecyclerView.ChildDrawingOrderCallback childDrawingOrderCallback)
  //    clipToPadding(boolean clipToPadding)
  //    itemViewCacheSize(int size)
  //    layoutFrozen(boolean frozen)
  //    nestedScrollingEnabled(boolean enabled)
  //    onScrollListener(RecyclerView.OnScrollListener listener)
  //    recycledViewPool(RecyclerView.RecycledViewPool pool)
  //    recyclerListener(RecyclerView.RecyclerListener listener)
  //    scrollingTouchSlop(int slopConstant)
  //    viewCacheExtension(RecyclerView.ViewCacheExtension extension)

  public static abstract class Adapter extends RenderableRecyclerViewAdapter {
    public static Adapter simple(final List items, final Recycler.AdapterRenderable r) {
      Adapter a = new Adapter() {
        public int getItemCount() {
          return items.size();
        }
        public long getItemId(int pos) {
          return pos;
        }
        public int getItemViewType(int pos) {
          Object item = items.get(pos);
          return item == null ? 0 : item.getClass().hashCode();
        }
        public void view(RecyclerView.ViewHolder holder) {
          r.view(holder);
        }
      };
      a.setHasStableIds(false);
      return a;
    }
  }

  public interface AdapterRenderable {
    void view(RecyclerView.ViewHolder vh);
  }
}