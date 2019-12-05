1. JDK 在1.8中引入红黑树，这样底层数据结构由`数组+链表` 变成`数组+链表+红黑数`。
2. 红黑树是一种平衡的二叉查找树，是一种高效的查找树。
> 二叉树存储原理：设 x 是二叉搜索树中的一个节点。如果y是x左子树中的一个节点，那么y.key<=x.key。如果 y 是 x 右子树中的一个节点，那么y.key>=x.key。
3. 红黑树特性：
    - 每个节点或者是红色或者是黑色的；
    - 根节点为黑色的；
    - 每个叶子（Nil）节点都是黑色的；
    - 如果一个节点是红色的，那它的两个子节点是黑色的；
    - 从任一节点到其每个叶子节点的所有简单路径都包含相同数目的黑色节点（简称黑高）
4. 左旋是将某个节点旋转为其右孩子的左孩子，而右旋是节点旋转为其左孩子的右孩子。下面以右旋为例，左旋类似
右旋：
```java
 static <K, V> TreeNode<K, V> rotateRight(TreeNode<K, V> root, TreeNode<K, V> p) {
            TreeNode<K, V> l;
            TreeNode<K, V> pp;
            TreeNode<K, V> lr;
            if ((p != null) && (l = p.left) != null) {
                if ((lr = p.left = l.right) != null) { //1
                    lr.parent = p;
                }
                if ((pp = l.parent = p.parent) == null) {//2
                    (root = l).red = false;
                } else if (pp.right == p) {//3
                    pp.right = l;
                } else {
                    pp.left = l;
                }
                l.right = p;//4
                p.parent = l;
            }
            return root;
        }
```
1. P节点的左节点的引用指向其左节点的右节点;
2. 如果P节点的父节点为空则跳转到4;
3. 如果P的父节点不为空，且为父节点的右节点（pp.right ==p）,将其父节点的右节点引用指向P的左节点（pp.right =l），否则（pp.left = l）;
4. P节点的左节点的右节点引用指向P(l.right =p)，P节点的父节点指向其左节点（p.parent = l);
---
查找：