package com.example.example.recyclerview;

public class AdapterHelper implements OpReorderer.Callback
{


    final Callback callback;

    public AdapterHelper(Callback callback) {
        this.callback = callback;
    }

    @Override
    public UpdateOp obtainUpdateOp(int cmd, int startPostion, int itemCount, Object payload) {
        return null;
    }

    @Override
    public void recycleUpdate(UpdateOp op) {

    }


    interface Callback{

        MyRecyclerView.ViewHolder  findViewHolder(int positon);

        void offsetPositionForRemovingInvisible(int positionStart, int itemCount);

        void offsetPositionForRemovingLaidOutNewView(int positionStart, int itemCount);

        void markViewHolderUpdated(int positonStart,int itemCount,Object payloads);

        void onDispatchFirstPass(UpdateOp updateOp);

        void onDispatchSecondPass(UpdateOp updateOp);

        void offsetPostionForAdd(int positionStart,int itemCount);

        void offsetPostionForMove(int from,int to);



    }

    static class UpdateOp{

        static final int ADD = 1;

        static final int REMOVE = 1 << 1;

        static final int UPDATE = 1 << 2;

        static final int MOVE = 1 << 3;

        static final int POOL_SIZE = 30;

        int cmd;

        int positionStart;

        Object payload;

        int itemCount;

        public UpdateOp(int cmd, int positionStart, Object payload, int itemCount) {
            this.cmd = cmd;
            this.positionStart = positionStart;
            this.payload = payload;
            this.itemCount = itemCount;
        }

        String cmdToString() {
            switch (cmd) {
                case ADD:
                    return "add";
                case REMOVE:
                    return "rm";
                case UPDATE:
                    return "up";
                case MOVE:
                    return "mv";
            }
            return "??";
        }


        @Override
        public String toString() {
            return Integer.toHexString(System.identityHashCode(this))
                    + "[" + cmdToString() + ",s:" + positionStart + "c:" + itemCount
                    + ",p:" + payload + "]";
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            UpdateOp op = (UpdateOp) o;

            if (cmd != op.cmd) {
                return false;
            }
            if (cmd == MOVE && Math.abs(itemCount - positionStart) == 1) {
                // reverse of this is also true
                if (itemCount == op.positionStart && positionStart == op.itemCount) {
                    return true;
                }
            }
            if (itemCount != op.itemCount) {
                return false;
            }
            if (positionStart != op.positionStart) {
                return false;
            }
            if (payload != null) {
                if (!payload.equals(op.payload)) {
                    return false;
                }
            } else if (op.payload != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = cmd;
            result = 31 * result + positionStart;
            result = 31 * result + itemCount;
            return result;
        }

    }
}
