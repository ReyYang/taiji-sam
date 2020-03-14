import com.google.common.collect.Lists;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Demo BinaryTreeDemo
 *
 * @author ydy
 * @date 2020/3/12 19:52
 */
public class BinarySearchTree<E extends Comparable<E>> {

    private Node root;
    private int size;

    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(E e) {
        if (root == null) {
            root = new Node(e);
            size++;
            return;
        } else {
            add(root, e);
        }
    }

    private void add(Node node, E e) {
        if (e.compareTo(node.e) == 0) {
            // 终止条件
            // 每次递归，跟根节点的值比较
            return;
        } else if (e.compareTo(node.e) < 0 && node.left == null) {

        }
    }

    class Node {
        private E e;
        private Node left;
        private Node right;

        private Node(E e) {
            this.e = e;
            this.left = null;
            this.right = null;
        }
    }
}
