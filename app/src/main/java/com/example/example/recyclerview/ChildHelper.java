package com.example.example.recyclerview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChildHelper {


    public static final boolean Debug = false;

    private static final String tag = ChildHelper.class.getSimpleName();

    final Callback mCallback;

    final Bucket mBucket;

    final List<View> mHiddenViews;

    public ChildHelper(Callback mCallback) {
        this.mCallback = mCallback;
        mBucket = new Bucket();
        mHiddenViews = new ArrayList<>();

    }

    void removeView(View view) {
        int index = mCallback.indexOfChild(view);
        if (index < 0) {
            return;
        }
        if (mBucket.remove(index)) {
            unhideViewInternal(view);
        }
        mCallback.removeViewAt(index);
    }

    private boolean unhideViewInternal(View child) {
        if (mHiddenViews.remove(child)) {
            mCallback.onLeftHiddenState(child);
            return true;
        } else {
            return false;
        }
    }

    void removeViewAt(int index) {
        final int offset = getOffset(index);
        final View view = mCallback.getChildAt(offset);
        if (view == null) {
            return;
        }
        if (mBucket.remove(offset)) {
            unhideViewInternal(view);
        }
        mCallback.removeViewAt(offset);

    }

    private int getOffset(int index) {
        if (index < 0) {
            return -1; //anything below 0 won't work as diff will be undefined.
        }
        final int limit = mCallback.getChildCount();
        int offset = index;
        while (offset < limit) {
            final int removedBefore = mBucket.countOnesBefore(offset);
            final int diff = index - (offset - removedBefore);
            if (diff == 0) {
                while (mBucket.get(offset)) { // ensure this offset is not hidden
                    offset++;
                }
                return offset;
            } else {
                offset += diff;
            }
        }
        return -1;
    }

    View getChildAt(int index) {
        final int offset = getOffset(index);
        return mCallback.getChildAt(offset);
    }

    View findHiddenNonRemovedView(int position) {
        final int count = mHiddenViews.size();
        for (int i = 0; i < count; i++) {
            final View view = mHiddenViews.get(i);
            MyRecyclerView.ViewHolder holder = mCallback.getChildViewHolder(view);
            if (holder.getLayoutPosition() == position
                    && !holder.isInvalid()
                    && !holder.isRemoved()) {
                return view;
            }
        }
        return null;
    }

    void attachViewToParent(View child, int index, ViewGroup.LayoutParams layoutParams,
                            boolean hidden) {
        final int offset;
        if (index < 0) {
            offset = mCallback.getChildCount();
        } else {
            offset = getOffset(index);
        }
        mBucket.insert(offset, hidden);
        if (hidden) {
            hideViewInternal(child);
        }
        mCallback.attachViewToParent(child, offset, layoutParams);

    }

    private void hideViewInternal(View child) {
        mHiddenViews.add(child);
        mCallback.onEnteredHiddenState(child);
    }

    View getUnfilteredChildAt(int index) {
        return mCallback.getChildAt(index);
    }

    int getChildCount() {
        return mCallback.getChildCount() - mHiddenViews.size();
    }

    void detachViewFromParent(int index) {
        final int offset = getOffset(index);
        mBucket.remove(offset);
        mCallback.detachViewFromParent(offset);
    }

    int indexOfChild(View child) {
        final int index = mCallback.indexOfChild(child);
        if (index == -1) {
            return -1;
        }
        if (mBucket.get(index)) {
            return -1;
        }
        // reverse the index
        return index - mBucket.countOnesBefore(index);
    }

    boolean isHidden(View view) {
        return mHiddenViews.contains(view);
    }


    void hide(View view) {
        final int offset = mCallback.indexOfChild(view);
        if (offset < 0) {
            throw new IllegalArgumentException("view is not a child, cannot hide " + view);
        }

        mBucket.set(offset);
        hideViewInternal(view);

    }

    void unhide(View view) {
        final int offset = mCallback.indexOfChild(view);
        if (offset < 0) {
            throw new IllegalArgumentException("view is not a child, cannot hide " + view);
        }
        if (!mBucket.get(offset)) {
            throw new RuntimeException("trying to unhide a view that was not hidden" + view);
        }
        mBucket.clear(offset);
        unhideViewInternal(view);
    }


    boolean removeViewIfHidden(View view) {
        final int index = mCallback.indexOfChild(view);
        if (index == -1) {
            return true;
        }
        if (mBucket.get(index)) {
            mBucket.remove(index);

            mCallback.removeViewAt(index);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return mBucket.toString() + ", hidden list:" + mHiddenViews.size();
    }

    int getUnfilteredChildCount() {
        return mCallback.getChildCount();
    }

    void removeAllViewsUnfiltered() {
        mBucket.reset();
        for (int i = mHiddenViews.size() - 1; i >= 0; i--) {
            mCallback.onLeftHiddenState(mHiddenViews.get(i));
            mHiddenViews.remove(i);
        }
        mCallback.removeAllViews();

    }


    void addView(View child, boolean hidden) {
        addView(child, -1, hidden);
    }

    void addView(View child, int index, boolean hidden) {
        final int offset;
        if (index < 0) {
            offset = mCallback.getChildCount();
        } else {
            offset = getOffset(index);
        }
        mBucket.insert(offset, hidden);
        if (hidden) {
            hideViewInternal(child);
        }
        mCallback.addView(child, offset);
    }


    interface Callback {
        int getChildCount();

        void addView(View child, int index);

        int indexOfChild(View view);

        void removeView(int index);

        void removeViewAt(int index);

        View getChildAt(int offset);

        void removeAllViews();

        MyRecyclerView.ViewHolder getChildViewHolder(View view);

        void attachViewToParent(View child, int index, ViewGroup.LayoutParams layoutParams);

        void detachViewFromParent(int offset);

        void onEnteredHiddenState(View child);

        void onLeftHiddenState(View child);

    }

    static class Bucket {
        static final int BITS_PER_WORD = Long.SIZE;
        static final long LAST_BIT = 1L << (Long.SIZE - 1);
        long mData = 0;
        Bucket mNext;

        void set(int index) {
            if (index >= BITS_PER_WORD) {
                ensureNext();
                mNext.set(index - BITS_PER_WORD);
            } else {
                mData |= 1L << index;
            }
        }


        private void ensureNext() {
            if (mNext == null) {
                mNext = new Bucket();
            }
        }

        void clear(int index) {

            if (index >= BITS_PER_WORD) {
                if (mNext != null) {
                    mNext.clear(index - BITS_PER_WORD);
                }
            } else {
                mData &= ~(1L << index);
            }

        }

        boolean get(int index) {
            if (index >= BITS_PER_WORD) {
                ensureNext();
                return mNext.get(index - BITS_PER_WORD);
            } else {
                return (mData & (1L << index)) != 0;
            }
        }

        void reset() {
            mData = 0;
            if (mNext != null) {
                mNext.reset();
            }
        }

        void insert(int index, boolean value) {
            if (index >= BITS_PER_WORD) {
                ensureNext();
                mNext.insert(index - BITS_PER_WORD, value);
            } else {
                final boolean lastBit = (mData & LAST_BIT) != 0;
                long mask = (1L << index) - 1;
                final long before = mData & mask;
                final long after = ((mData & ~mask)) << 1;
                mData = before | after;
                if (value) {
                    set(index);
                } else {
                    clear(index);
                }
                if (lastBit || mNext != null) {
                    ensureNext();
                    mNext.insert(0, lastBit);
                }
            }
        }

        boolean remove(int index) {
            if (index >= BITS_PER_WORD) {
                ensureNext();
                return mNext.remove(index - BITS_PER_WORD);
            } else {
                long mask = (1L << index);
                final boolean value = (mData & mask) != 0;
                mData &= ~mask;
                mask = mask - 1;
                final long before = mData & mask;
                // cannot use >> because it adds one.
                final long after = Long.rotateRight(mData & ~mask, 1);
                mData = before | after;
                if (mNext != null) {
                    if (mNext.get(0)) {
                        set(BITS_PER_WORD - 1);
                    }
                    mNext.remove(0);
                }
                return value;
            }
        }

        int countOnesBefore(int index) {
            if (mNext == null) {
                if (index >= BITS_PER_WORD) {
                    return Long.bitCount(mData);
                }
                return Long.bitCount(mData & ((1L << index) - 1));
            }
            if (index < BITS_PER_WORD) {
                return Long.bitCount(mData & ((1L << index) - 1));
            } else {
                return mNext.countOnesBefore(index - BITS_PER_WORD) + Long.bitCount(mData);
            }
        }


        @Override
        public String toString() {
            return mNext == null ? Long.toBinaryString(mData)
                    : mNext.toString() + "xx" + Long.toBinaryString(mData);
        }

    }
}
