package com.example.example.recyclerview;

import android.content.Context;
import android.database.Observable;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GapWorker;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ViewBoundsCheck;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MyRecyclerView extends ViewGroup implements NestedScrollingChild2, ScrollingView {


    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    //<editor-fold desc="方向">
    public static final int VERTICAL = LinearLayout.VERTICAL;
    public static final int NO_POSITION = -1;
    public static final long NO_ID = -1;
    //</editor-fold>
    public static final int INVALID_TYPE = -1;
    public static final boolean DEBUG = false;
    static final int DEAFAULT_ORIENTATION = VERTICAL;
    private static final String TAG = MyRecyclerView.class.getSimpleName();
    AdapterHelper mAdapterHelper;
    ChildHelper mChildHelper;
    private int mTouchSlop;
    private boolean mItemAddedOrRemoved = false;
    private State mState = new State();

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScrollContainer(true);
        setFocusableInTouchMode(true);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        initAdapterManager();
        initChildHelper();

    }

    private void initAdapterManager() {
        mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() {
            @Override
            public ViewHolder findViewHolder(int position) {
                final ViewHolder vh = findViewHolderForPosition(position, true);
                if (vh == null) {
                    return null;
                }
                if (mChildHelper.isHidden(vh.itemView)) {
                    if (DEBUG) {
                        Log.d(TAG, "assuming view holder cannot be find because it is hidden");
                    }
                    return null;
                }
                return vh;
            }

            @Override
            public void offsetPositionsForRemovingInvisible(int positionStart, int itemCount) {
                offsetPositionRecordsForRemove(positionStart, itemCount, true);
                mItemAddedOrRemoved = true;
                mState.mDeletedInvisibleItemCountSincePreviousLayout += itemCount;

            }

            @Override
            public void offsetPositionForRemovingLaidOutNewView(int positionStart, int itemCount) {
                offsetPositionRecordsForRemove(positionStart, itemCount, false);
                mItemAddedOrRemoved = true;
            }

            @Override
            public void markViewHolderUpdated(int positonStart, int itemCount, Object payloads) {

            }

            @Override
            public void onDispatchFirstPass(AdapterHelper.UpdateOp updateOp) {

            }

            @Override
            public void onDispatchSecondPass(AdapterHelper.UpdateOp updateOp) {

            }

            @Override
            public void offsetPositionForAdd(int positionStart, int itemCount) {

            }

            @Override
            public void offsetPositionForMove(int from, int to) {

            }
        });
    }

    private void initChildHelper() {
        mChildHelper = new ChildHelper(new ChildHelper.Callback() {
            @Override
            public int getChildCount() {
                return MyRecyclerView.this.getChildCount();
            }

            @Override
            public void addView(View child, int index) {
                MyRecyclerView.this.addView(child, index);
            }

            @Override
            public int indexOfChild(View view) {
                return 0;
            }

            @Override
            public void removeView(int index) {

            }

            @Override
            public void removeViewAt(int index) {

            }

            @Override
            public View getChildAt(int offset) {
                return null;
            }

            @Override
            public void removeAllViews() {

            }

            @Override
            public ViewHolder getChildViewHolder(View view) {
                return null;
            }

            @Override
            public void attachViewToParent(View child, int index, ViewGroup.LayoutParams layoutParams) {

            }

            @Override
            public void detachViewFromParent(int offset) {

            }

            @Override
            public void onEnteredHiddenState(View child) {

            }

            @Override
            public void onLeftHiddenState(View child) {

            }

        });
    }

    @Nullable
    ViewHolder findViewHolderForPosition(int position, boolean checkNewPositon) {
        final int childCount = mChildHelper.getUnfilteredChildCount();
        ViewHolder hidden = null;
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.isRemoved()) {
                if (checkNewPositon) {
                    if (holder.mPosition != position) {
                        continue;
                    }
                } else if (holder.getLayoutPosition() != position) {
                    continue;
                }
                if (mChildHelper.isHidden(holder.itemView)) {
                    hidden = holder;
                } else {
                    return holder;
                }
            }
        }
        return hidden;
    }

    void offsetPositionRecordsForRemove(int positionStart, int itemCount,
                                        boolean applyToPreLayout) {

        final int positionEnd = positionStart + itemCount;
        final int childCount = mChildHelper.getUnfilteredChildCount();
        for (int i = 0; i < childCount; i++) {
            final ViewHolder holder = getChildViewHolderInt(mChildHelper.getUnfilteredChildAt(i));
            if (holder != null && !holder.shouldIgnore()) {
                if (holder.mPosition >= positionEnd) {

                }
                holder.offsetPosition(-itemCount, applyToPreLayout);
            }
        }


    }

    static ViewHolder getChildViewHolderInt(View child) {
        if (child == null) {
            return null;
        }
        return ((LayoutParams) child.getLayoutParams()).mViewHolder;

    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    static void clearNestedRecyclerViewIfNotNested(@NonNull ViewHolder holder) {
        if (holder.mNestedRecyclerView != null) {
            View item = holder.mNestedRecyclerView.get();
            while (item != null) {
                if (item == holder.itemView) {
                    return;
                }
                ViewParent parent = item.getParent();
                if (parent instanceof View) {
                    item = (View) parent;
                } else {
                    item = null;
                }
            }
            holder.mNestedRecyclerView = null;
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public int computeHorizontalScrollRange() {
        return 0;
    }

    @Override
    public int computeHorizontalScrollOffset() {
        return 0;
    }

    @Override
    public int computeHorizontalScrollExtent() {
        return 0;
    }

    @Override
    public int computeVerticalScrollRange() {
        return 0;
    }

    @Override
    public int computeVerticalScrollOffset() {
        return 0;
    }

    @Override
    public int computeVerticalScrollExtent() {
        return 0;
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return false;
    }

    @Override
    public void stopNestedScroll(int type) {

    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return false;
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return false;
    }

    private String getClassName(Context context, String className) {
        if (className.charAt(0) == '.') {
            return context.getPackageName() + className;
        }
        if (className.contains(".")) {
            return className;
        }
        return MyRecyclerView.class.getPackage().getName() + "." + className;

    }

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {

    }

    public abstract static class ViewHolder {

        static final int FLAG_BOUND = 1 << 0;
        static final int FLAG_UPDATE = 1 << 1;
        static final int FLAG_INVALID = 1 << 2;
        static final int FLAG_REMOVED = 1 << 3;
        static final int FLAG_NOT_RECYCLABLE = 1 << 4;
        static final int FLAG_RETURNED_FROM_SCRAP = 1 << 5;
        static final int FLAG_IGNORE = 1 << 7;
        static final int FLAG_TMP_DETACHED = 1 << 8;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 1 << 9;
        static final int FLAG_ADAPTER_FULLUPDATE = 1 << 10;
        static final int FLAG_MOVED = 1 << 11;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 1 << 12;
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 1 << 13;
        static final int FLAG_SET_A11Y_ITEM_DELEGATE = 1 << 14;
        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.emptyList();
        @NonNull
        public final View itemView;
        WeakReference<MyRecyclerView> mNestedRecyclerView;
        int mPosition = NO_POSITION;
        int mOldPosition = NO_POSITION;
        long mItemId = NO_ID;
        int mItemViewType = INVALID_TYPE;
        int mPreLayoutPosition = NO_POSITION;
        ViewHolder mShadowedHolder = null;
        ViewHolder mShadowingHolder = null;
        int mFlags;
        List<Object> mPayloads = null;
        List<Object> mUnmodifiedPayloads = null;
        MyRecyclerView mOwnerRecyclerView;
        @VisibleForTesting
        int mPendingAccessibilityState = PENDING_ACCESSIBILITY_STATE_NOT_SET;
        private int mIsRecyclableCount = 0;
        private int mWasImportantForAccessibilityBeforeHidden =
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;


        public ViewHolder(@NonNull View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView must no be null");
            }
            this.itemView = itemView;
        }

        void flagRemovedAndOffsetPosition(int mNewPosition, int offset, boolean applyToPreLayout) {
            addFlags(ViewHolder.FLAG_REMOVED);
            offsetPosition(offset, applyToPreLayout);
            mPosition = mNewPosition;
        }

        void addFlags(int flags) {
            mFlags |= flags;
        }

        void offsetPosition(int offset, boolean applyToPreLayout) {
            if (mOldPosition == NO_POSITION) {
                mOldPosition = mPosition;
            }
            if (mPreLayoutPosition == NO_POSITION) {
                mPreLayoutPosition = mPosition;
            }
            if (applyToPreLayout) {
                mPreLayoutPosition += offset;
            }
            mPosition += offset;
            if (itemView.getLayoutParams() != null) {
                ((LayoutParams) itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }

        void clearOldPosition() {
            mOldPosition = NO_POSITION;
            mPreLayoutPosition = NO_POSITION;
        }

        void saveOldPosition() {
            if (mOldPosition == NO_POSITION) {
                mOldPosition = mPosition;
            }
        }

        public final int getLayoutPosition() {
            return mPreLayoutPosition == NO_POSITION ? mPosition : mPreLayoutPosition;
        }

        public final int getAdapterPosition() {
            if (mOwnerRecyclerView == null) {
                return NO_POSITION;
            }
            return mOwnerRecyclerView.getAdapterPositionFor(this);
        }

        public final int getOldPosition() {
            return mOldPosition;
        }

        public final long getItemId() {
            return mItemId;
        }

        public final int getItemViewType() {
            return mItemViewType;
        }

        void unScrap() {
            mScrapContainer.unscrapView(this);
        }

        boolean wasReturnedFromScrap() {
            return (mFlags & FLAG_RETURNED_FROM_SCRAP) != 0;
        }

        void clearReturnedFromScrapFlag() {
            mFlags = mFlags & ~FLAG_RETURNED_FROM_SCRAP;
        }

        void clearTmpDetachFlag() {
            mFlags = mFlags & ~FLAG_TMP_DETACHED;
        }

        void stopIgnoring() {
            mFlags = mFlags & ~FLAG_IGNORE;
        }

        void setScrapContainer(Recycler recycler, boolean isChangeScrap) {
            mScrapContainer = recycler;
            mInChangeScrap = isChangeScrap;
        }

        boolean hasAnyOfTheFlags(int flags) {
            return (mFlags & flags) != 0;
        }

        void setFlags(int flags, int mask) {
            mFlags = (mFlags & ~mask) | (flags & mask);
        }

        void addChangePayload(Object payload) {
            if (payload == null) {
                addFlags(FLAG_ADAPTER_FULLUPDATE);
            } else if ((mFlags & FLAG_ADAPTER_FULLUPDATE) == 0) {
                createPayloadsIfNeeded();
                mPayloads.add(payload);
            }
        }

        private void createPayloadsIfNeeded() {
            if (mPayloads == null) {
                mPayloads = new ArrayList<Object>();
                mUnmodifiedPayloads = Collections.unmodifiableList(mPayloads);
            }
        }

        List<Object> getUnmodifiedPayloads() {
            if ((mFlags & FLAG_ADAPTER_FULLUPDATE) == 0) {
                if (mPayloads == null || mPayloads.size() == 0) {
                    // Initial state,  no update being called.
                    return FULLUPDATE_PAYLOADS;
                }
                // there are none-null payloads
                return mUnmodifiedPayloads;
            } else {
                // a full update has been called.
                return FULLUPDATE_PAYLOADS;
            }
        }

        void resetInternal() {
            mFlags = 0;
            mPosition = NO_POSITION;
            mOldPosition = NO_POSITION;
            mItemId = NO_ID;
            mPreLayoutPosition = NO_POSITION;
            mIsRecyclableCount = 0;
            mShadowedHolder = null;
            mShadowingHolder = null;
            clearPayload();
            mWasImportantForAccessibilityBeforeHidden = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;
            mPendingAccessibilityState = PENDING_ACCESSIBILITY_STATE_NOT_SET;
            clearNestedRecyclerViewIfNotNested(this);
        }

        void clearPayload() {
            if (mPayloads != null) {
                mPayloads.clear();
            }
            mFlags = mFlags & ~FLAG_ADAPTER_FULLUPDATE;
        }

        void onEnteredHiddenState(MyRecyclerView parent) {
            // While the view item is in hidden state, make it invisible for the accessibility.
            if (mPendingAccessibilityState != PENDING_ACCESSIBILITY_STATE_NOT_SET) {
                mWasImportantForAccessibilityBeforeHidden = mPendingAccessibilityState;
            } else {
                mWasImportantForAccessibilityBeforeHidden =
                        ViewCompat.getImportantForAccessibility(itemView);
            }
            parent.setChildImportantForAccessibilityInternal(this,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
        }

        void onLeftHiddenState(MyRecyclerView parent) {
            parent.setChildImportantForAccessibilityInternal(this,
                    mWasImportantForAccessibilityBeforeHidden);
            mWasImportantForAccessibilityBeforeHidden = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ViewHolder{"
                    + Integer.toHexString(hashCode()) + " position=" + mPosition + " id=" + mItemId
                    + ", oldPos=" + mOldPosition + ", pLpos:" + mPreLayoutPosition);
            if (isScrap()) {
                sb.append(" scrap ")
                        .append(mInChangeScrap ? "[changeScrap]" : "[attachedScrap]");
            }
            if (isInvalid()) sb.append(" invalid");
            if (!isBound()) sb.append(" unbound");
            if (needsUpdate()) sb.append(" update");
            if (isRemoved()) sb.append(" removed");
            if (shouldIgnore()) sb.append(" ignored");
            if (isTmpDetached()) sb.append(" tmpDetached");
            if (!isRecyclable()) sb.append(" not recyclable(" + mIsRecyclableCount + ")");
            if (isAdapterPositionUnknown()) sb.append(" undefined adapter position");

            if (itemView.getParent() == null) sb.append(" no parent");
            sb.append("}");
            return sb.toString();
        }

        boolean isScrap() {
            return mScrapContainer != null;
        }

        boolean isInvalid() {
            return (mFlags & FLAG_INVALID) != 0;
        }

        boolean isBound() {
            return (mFlags & FLAG_BOUND) != 0;
        }

        boolean needsUpdate() {
            return (mFlags & FLAG_UPDATE) != 0;
        }

        boolean isRemoved() {
            return (mFlags & FLAG_REMOVED) != 0;
        }

        boolean shouldIgnore() {
            return (mFlags & FLAG_IGNORE) != 0;
        }

        boolean isTmpDetached() {
            return (mFlags & FLAG_TMP_DETACHED) != 0;
        }

        public final boolean isRecyclable() {
            return (mFlags & FLAG_NOT_RECYCLABLE) == 0
                    && !ViewCompat.hasTransientState(itemView);
        }

        boolean isAdapterPositionUnknown() {
            return (mFlags & FLAG_ADAPTER_POSITION_UNKNOWN) != 0 || isInvalid();
        }

        public final void setIsRecyclable(boolean recyclable) {
            mIsRecyclableCount = recyclable ? mIsRecyclableCount - 1 : mIsRecyclableCount + 1;
            if (mIsRecyclableCount < 0) {
                mIsRecyclableCount = 0;
                if (DEBUG) {
                    throw new RuntimeException("isRecyclable decremented below 0: "
                            + "unmatched pair of setIsRecyable() calls for " + this);
                }
                Log.e(VIEW_LOG_TAG, "isRecyclable decremented below 0: "
                        + "unmatched pair of setIsRecyable() calls for " + this);
            } else if (!recyclable && mIsRecyclableCount == 1) {
                mFlags |= FLAG_NOT_RECYCLABLE;
            } else if (recyclable && mIsRecyclableCount == 0) {
                mFlags &= ~FLAG_NOT_RECYCLABLE;
            }
            if (DEBUG) {
                Log.d(TAG, "setIsRecyclable val:" + recyclable + ":" + this);
            }
        }

        boolean shouldBeKeptAsChild() {
            return (mFlags & FLAG_NOT_RECYCLABLE) != 0;
        }

        boolean doesTransientStatePreventRecycling() {
            return (mFlags & FLAG_NOT_RECYCLABLE) == 0 && ViewCompat.hasTransientState(itemView);
        }

        boolean isUpdated() {
            return (mFlags & FLAG_UPDATE) != 0;
        }
    }

    public abstract static class ItemDecoration {

    }

    public static class RecycledViewPool {

    }

    public abstract static class ViewCacheExtension {

        public abstract View getViewForPositionAndType(@NonNull Recycler recycler, int position, int type);
    }

    public static class LayoutParams extends MarginLayoutParams {

        final Rect mDecorInsets = new Rect();
        ViewHolder mViewHolder;
        boolean mInsetsDirty = true;
        boolean mPendingInvalidate = false;


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public boolean viewNeedsUpdate() {

            return mViewHolder.needsUpdate();
        }

        public boolean isViewInvalid() {
            return mViewHolder.isInvalid();
        }
    }

    public static class State {
        static final int STEP_START = 1; //1
        static final int STEP_LAYOUT = 1 << 1;//2
        static final int STEP_ANIMATIONS = 1 << 2;//4
        int mLayoutStep = STEP_START;
        int mItemCount = 0;
        boolean mStructureChanged = false;
        boolean mInPreLayout = false;
        boolean mTrackOldChangeHolders = false;
        boolean mIsMeasuring = false;
        boolean mRunSimpleAnimations = false;
        boolean mRunPredictiveAnimations = false;
        int mPreviousLayoutItemCount = 0;
        int mDeletedInvisibleItemCountSincePreviousLayout = 0;
        int mFocusedItemPosition;
        long mFocusedItemId;
        int mFocusedSubChildId;
        int mRemainingScrollHorizontal;
        int mRemainingScrollVertical;
        int mTargetPosition = RecyclerView.NO_POSITION;
        private SparseArray<Object> mData;

        void assertLayoutStep(int accepted) {
            if ((accepted & mLayoutStep) == 0) {
                throw new IllegalStateException("Layout state should be one of "
                        + Integer.toBinaryString(accepted) + " but it is "
                        + Integer.toBinaryString(mLayoutStep));
            }
        }

        State reset() {
            mTargetPosition = RecyclerView.NO_POSITION;
            if (mData != null) {
                mData.clear();
            }
            mItemCount = 0;
            mStructureChanged = false;
            mIsMeasuring = false;
            return this;
        }

        void prepareForNestedPrefetch(Adapter adapter) {
            mLayoutStep = STEP_START;
            mItemCount = adapter.getItemCount();
            mInPreLayout = false;
            mTrackOldChangeHolders = false;
            mIsMeasuring = false;
        }

        public boolean isMeasuring() {
            return mIsMeasuring;
        }

        public boolean isPreLayout() {
            return mInPreLayout;
        }

        public boolean willRunPredictiveAnimations() {

            return mRunPredictiveAnimations;
        }

        public boolean willRunSimpleAnimations() {

            return mRunSimpleAnimations;
        }

        public void remove(int resourceId) {
            if (mData == null) {
                return;
            }
            mData.remove(resourceId);
        }

        public <T> T get(int resourceId) {
            if (mData == null) {
                return null;
            }
            return (T) mData.get(resourceId);
        }

        public void put(int resourceId, Object data) {
            if (mData == null) {
                mData = new SparseArray<Object>();
            }
            mData.put(resourceId, data);
        }

        public int getTargetScrollPosition() {
            return mTargetPosition;
        }

        public boolean hasTargetScrollPosition() {
            return mTargetPosition != RecyclerView.NO_POSITION;
        }

        public boolean didStructureChange() {
            return mStructureChanged;
        }

        public int getItemCount() {
            return mInPreLayout
                    ? (mPreviousLayoutItemCount - mDeletedInvisibleItemCountSincePreviousLayout)
                    : mItemCount;
        }

        public int getRemainingScrollHorizontal() {
            return mRemainingScrollHorizontal;
        }

        public int getRemainingScrollVertical() {
            return mRemainingScrollVertical;
        }

        @Override
        public String toString() {
            return "State{"
                    + "mTargetPosition=" + mTargetPosition
                    + ", mData=" + mData
                    + ", mItemCount=" + mItemCount
                    + ", mIsMeasuring=" + mIsMeasuring
                    + ", mPreviousLayoutItemCount=" + mPreviousLayoutItemCount
                    + ", mDeletedInvisibleItemCountSincePreviousLayout="
                    + mDeletedInvisibleItemCountSincePreviousLayout
                    + ", mStructureChanged=" + mStructureChanged
                    + ", mInPreLayout=" + mInPreLayout
                    + ", mRunSimpleAnimations=" + mRunSimpleAnimations
                    + ", mRunPredictiveAnimations=" + mRunPredictiveAnimations
                    + '}';
        }


        @IntDef(flag = true, value = {
                STEP_START, STEP_LAYOUT, STEP_ANIMATIONS
        })
        @Retention(RetentionPolicy.SOURCE)
        @interface LayoutState {
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {


        private final AdapterDataObservable mObservable = new AdapterDataObservable();
        private boolean mHasStableIds = false;

        @NonNull
        public final VH createViewHolder(@NonNull ViewGroup parent, int viewType) {
            try {

                final VH holder = onCreateViewHolder(parent, viewType);
                if (holder.itemView.getParent() != null) {
                    throw new IllegalStateException("ViewHolder views must not be attached when"
                            + " created. Ensure that you are not passing 'true' to the attachToRoot"
                            + " parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
                }
                holder.mItemViewType = viewType;
                return holder;
            } finally {

            }
        }

        @NonNull
        public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

        public final void bindViewHolder(@NonNull VH holder, int position) {
            holder.mPosition = position;
            if (hasStableIds()) {
                holder.mItemId = getItemId(position);
            }
            holder.setFlags(ViewHolder.FLAG_BOUND,
                    ViewHolder.FLAG_BOUND | ViewHolder.FLAG_UPDATE | ViewHolder.FLAG_INVALID
                            | ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN);
            onBindViewHolder(holder, position, holder.getUnmodifiedPayloads());
            holder.clearPayload();
            final ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof RecyclerView.LayoutParams) {
                ((LayoutParams) layoutParams).mInsetsDirty = true;
            }

        }

        public final boolean hasStableIds() {
            return mHasStableIds;
        }

        public long getItemId(int position) {


            return NO_ID;
        }

        public void onBindViewHolder(@NonNull VH holder, int position,
                                     @NonNull List<Object> payloads) {
            onBindViewHolder(holder, position);
        }

        public abstract void onBindViewHolder(@NonNull VH holder, int position);

        public int getItemViewType(int position) {


            return 0;
        }

        public void setHasStableIds(boolean hasStableIds) {
            if (hasObservers()) {
                throw new IllegalStateException("Cannot change whether this adapter has "
                        + "stable IDs while the adapter has registered observers.");
            }
            mHasStableIds = hasStableIds;
        }

        public final boolean hasObservers() {
            return mObservable.hasObservers();
        }

        public abstract int getItemCount();

        public void onViewRecycled(@NonNull VH holder) {
        }

        public boolean onFailedToRecycleView(@NonNull VH holder) {
            return false;
        }

        public void onViewAttachedToWindow(@NonNull VH holder) {
        }

        public void onViewDetachedFromWindow(@NonNull VH holder) {
        }

        public void registerAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            mObservable.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            mObservable.unregisterObserver(observer);
        }

        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        }

        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        }

        public final void notifyDataSetChanged() {
            mObservable.notifyChanged();
        }

        public final void notifyItemChanged(int position) {
            mObservable.notifyItemRangeChanged(position, 1);
        }


        public final void notifyItemChanged(int position, @Nullable Object payload) {
            mObservable.notifyItemRangeChanged(position, 1, payload);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount) {
            mObservable.notifyItemRangeChanged(positionStart, itemCount);
        }

        public final void notifyItemRangeChanged(int positionStart, int itemCount,
                                                 @Nullable Object payload) {
            mObservable.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        public final void notifyItemInserted(int position) {
            mObservable.notifyItemRangeInserted(position, 1);
        }

        public final void notifyItemMoved(int fromPosition, int toPosition) {
            mObservable.notifyItemMoved(fromPosition, toPosition);
        }

        public final void notifyItemRangeInserted(int positionStart, int itemCount) {
            mObservable.notifyItemRangeInserted(positionStart, itemCount);
        }

        public final void notifyItemRemoved(int position) {
            mObservable.notifyItemRangeRemoved(position, 1);
        }

        public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
            mObservable.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }

    static class AdapterDataObservable extends Observable<AdapterDataObserver> {


        public boolean hasObservers() {
            return !mObservers.isEmpty();
        }

        public void notifyChanged() {
            // since onChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount, null);
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount,
                                           @Nullable Object payload) {
            // since onItemRangeChanged() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeChanged(positionStart, itemCount, payload);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            // since onItemRangeInserted() is implemented by the app, it could do anything,
            // including removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            // since onItemRangeRemoved() is implemented by the app, it could do anything, including
            // removing itself from {@link mObservers} - and that could cause problems if
            // an iterator is used on the ArrayList {@link mObservers}.
            // to avoid such problems, just march thru the list in the reverse order.
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onItemRangeMoved(fromPosition, toPosition, 1);
            }
        }


    }

    public abstract static class AdapterDataObserver {
        public void onChanged() {
            // Do nothing
        }

        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            // fallback to onItemRangeChanged(positionStart, itemCount) if app
            // does not override this method.
            onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            // do nothing
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            // do nothing
        }
    }

    static final boolean ALLOW_THREAD_GAP_WORK = Build.VERSION.SDK_INT >= 21;

    GapWorker mGapWorker;
    GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry =
            ALLOW_THREAD_GAP_WORK ? new GapWorker.LayoutPrefetchRegistryImpl() : null;

    public final class Recycler {

        static final int DEFAULT_CACHE_SIZE = 2;

        final ArrayList<ViewHolder> mAttachedScrap = new ArrayList<>();

        final ArrayList<ViewHolder> mCachedViews = new ArrayList<>();

        private final List<ViewHolder> mUnmodifiableAttachedScrap = Collections.unmodifiableList(mAttachedScrap);
        ArrayList<ViewHolder> mChangedScrap = null;
        int mViewCacheMax = DEFAULT_CACHE_SIZE;


        RecycledViewPool mRecyclerPool;
        private int mRequestCacheMax = DEFAULT_CACHE_SIZE;
        private ViewCacheExtension mViewCacheExtension;

        public void clear() {
            mAttachedScrap.clear();
            recycleAndClearCachedViews();

        }

        void recycleAndClearCachedViews() {
            final int count = mCachedViews.size();
            for (int i = count - 1; i >= 0; i--) {

            }
            mCachedViews.clear();

        }

        void recycleCachedViewAt(int cachedViewIndex) {
            ViewHolder viewHolder = mCachedViews.get(cachedViewIndex);
            mCachedViews.remove(cachedViewIndex);
        }

        void addViewHolderToRecycledViewPool(@NonNull ViewHolder viewHolder, boolean dispatchRecycled) {
            clearNestedRecyclerViewIfNotNested(viewHolder);

        }
    }

    public abstract static class LayoutManager{
        ChildHelper mChildHelper;
        MyRecyclerView mRecyclerView;


        private final ViewBoundCheck.Callback mHorizontalBoundCheckCallBack = new ViewBoundCheck.Callback() {
            @Override
            public int getChildCount() {
                return 0;
            }

            @NotNull
            @Override
            public View getParent() {
                return null;
            }

            @NotNull
            @Override
            public View getChildAt(int index) {
                return null;
            }

            @Override
            public int getParentStart() {
                return 0;
            }

            @Override
            public int getParentEnd() {
                return 0;
            }

            @Override
            public int getChildStart(@NotNull View view) {
                return 0;
            }

            @Override
            public int getChildEnd(@NotNull View view) {
                return 0;
            }
        };

    }
}
