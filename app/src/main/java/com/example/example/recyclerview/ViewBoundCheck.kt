package com.example.example.recyclerview

import android.view.View
import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

const val GT = 1 shl 0
const val EQ = 1 shl 1
const val LT = 1 shl 2
const val CVS_PVS_POS = 0
const val FLAG_CVS_GT_PVS = GT shl CVS_PVS_POS

const val FLAG_CVS_EQ_PVS = EQ shl CVS_PVS_POS
const val FLAG_CVS_LT_PVS = LT shl CVS_PVS_POS
const val CVS_PVE_POS = 4

const val FLAG_CVS_GT_PVE = GT shl CVS_PVE_POS

const val FLAG_CVS_EQ_PVE = EQ shl CVS_PVE_POS

const val FLAG_CVS_LT_PVE = LT shl CVS_PVE_POS
const val CVE_PVS_POS = 8
const val FLAG_CVE_GT_PVS = GT shl CVE_PVS_POS
const val FLAG_CVE_EQ_PVS = EQ shl CVE_PVS_POS
const val FLAG_CVE_LT_PVS = LT shl CVE_PVS_POS
const val CVE_PVE_POS = 12
const val FLAG_CVE_GT_PVE = GT shl CVE_PVE_POS
const val FLAG_CVE_EQ_PVE = EQ shl CVE_PVE_POS
const val FLAG_CVE_LT_PVE = LT shl CVE_PVE_POS
const val MASK = GT or EQ or LT

class ViewBoundCheck(val callback: Callback) {

    private var mBoundFlags:BoundFlags

    init {
        mBoundFlags = BoundFlags()
    }


    @IntDef(
        flag = true,
        value = [FLAG_CVS_GT_PVS, FLAG_CVS_EQ_PVS, FLAG_CVS_LT_PVS, FLAG_CVS_GT_PVE, FLAG_CVS_EQ_PVE, FLAG_CVS_LT_PVE, FLAG_CVE_GT_PVS, FLAG_CVE_EQ_PVS, FLAG_CVE_LT_PVS, FLAG_CVE_GT_PVE, FLAG_CVE_EQ_PVE, FLAG_CVE_LT_PVE]
    )
    @Retention(RetentionPolicy.SOURCE)
    annotation class ViewBounds

    class BoundFlags{
        var mBoundFlags =0
        var mRvStart: Int = 0
        var mRvEnd:Int = 0
        var mChildStart:Int = 0
        var mChildEnd:Int = 0

        fun setBounds(rvStart: Int, rvEnd: Int, childStart: Int, childEnd: Int) {
            mRvStart = rvStart
            mRvEnd = rvEnd
            mChildStart = childStart
            mChildEnd = childEnd
        }
        fun setFlags(@ViewBounds flags: Int, mask: Int) {
            mBoundFlags = mBoundFlags and mask.inv() or (flags and mask)
        }

        fun addFlags(@ViewBounds flags: Int) {
            mBoundFlags = mBoundFlags or flags
        }

        fun resetFlags() {
            mBoundFlags = 0
        }

        fun compare(x: Int, y: Int): Int {
            if (x > y) {
                return GT
            }
            return if (x == y) {
                EQ
            } else LT
        }

        fun boundsMatch(): Boolean {
            if (mBoundFlags and (MASK shl CVS_PVS_POS) != 0) {
                if (mBoundFlags and (compare(mChildStart, mRvStart) shl CVS_PVS_POS) == 0) {
                    return false
                }
            }

            if (mBoundFlags and (MASK shl CVS_PVE_POS) != 0) {
                if (mBoundFlags and (compare(mChildStart, mRvEnd) shl CVS_PVE_POS) == 0) {
                    return false
                }
            }

            if (mBoundFlags and (MASK shl CVE_PVS_POS) != 0) {
                if (mBoundFlags and (compare(mChildEnd, mRvStart) shl CVE_PVS_POS) == 0) {
                    return false
                }
            }

            if (mBoundFlags and (MASK shl CVE_PVE_POS) != 0) {
                if (mBoundFlags and (compare(mChildEnd, mRvEnd) shl CVE_PVE_POS) == 0) {
                    return false
                }
            }
            return true
        }
    }

    interface Callback {
        fun getChildCount(): Int
        fun getParent(): View
        fun getChildAt(index: Int): View
        fun getParentStart(): Int
        fun getParentEnd(): Int
        fun getChildStart(view: View): Int
        fun getChildEnd(view: View): Int
    }
}