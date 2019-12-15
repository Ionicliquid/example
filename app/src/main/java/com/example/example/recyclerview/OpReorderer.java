package com.example.example.recyclerview;

public class OpReorderer {

    final Callback mCallBack;

    public OpReorderer(Callback mCallBack) {
        this.mCallBack = mCallBack;
    }



    interface  Callback{
        AdapterHelper.UpdateOp obtainUpdateOp(int cmd,int startPostion,int itemCount,Object payload);

        void recycleUpdate(AdapterHelper.UpdateOp op);
    }
}
