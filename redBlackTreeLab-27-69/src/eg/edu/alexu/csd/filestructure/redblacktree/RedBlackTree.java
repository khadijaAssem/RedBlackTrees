package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T,V>{
    private INode root = new Node();
    static final boolean RED   = true;
    static final boolean BLACK = false;

    @Override
    public INode getRoot() {

        return this.root;
    }

    @Override
    public boolean isEmpty() {
        return root.isNull()||root==null;
    }

    @Override
    public void clear() {
        root = new Node();
    }

    @Override
    public Object search(Comparable key) {
        if (key != null){
            INode tempNode = root;
            while(tempNode!=null&&!tempNode.isNull()){
                if(tempNode.getKey().compareTo(key)==0)
                    return tempNode.getValue();
                else if(tempNode.getKey().compareTo(key)>0)
                    tempNode = tempNode.getLeftChild();
                else
                    tempNode = tempNode.getRightChild();
            }
            return null;
        }
        throw new RuntimeErrorException(null);
    }

    @Override
    public boolean contains(Comparable key) {
        return this.search(key)!=null;
    }

    @Override
    public void insert(Comparable key, Object value) {
        if(key == null||value == null){
            throw new RuntimeErrorException(null);
        }
        if (root.isNull()){
            root.setParent(new Node());
            root.setColor(BLACK);
            root.setKey(key);
            root.setValue(value);
            root.setRightChild(new Node());
            root.getRightChild().setParent(root);
            root.setLeftChild(new Node());
            root.getLeftChild().setParent(root);
            return;
        }
        INode tempNode = root;

        while (!tempNode.isNull()) {
            if (tempNode.getKey().compareTo(key) > 0)
                tempNode = tempNode.getLeftChild();
            else if (tempNode.getKey().compareTo(key)<0)
                tempNode = tempNode.getRightChild();
            else {
                tempNode.setValue(value);
                return;
            }
        }
        tempNode.setKey(key);
        tempNode.setValue(value);
        tempNode.setRightChild(new Node());
        tempNode.getRightChild().setParent(tempNode);
        tempNode.setLeftChild(new Node());
        tempNode.getLeftChild().setParent(tempNode);
        tempNode.setColor(RED);

        tempNode = insertDefectedNode(tempNode);
        root = resetRoot(tempNode);

        root.setColor(BLACK);
    }

    private INode resetRoot(INode tempNode){
        while(!tempNode.getParent().isNull()) {
            tempNode = tempNode.getParent();
        }
        return tempNode;
    }

    public boolean delete(Comparable key) {
        if (key == null) {
            throw new RuntimeErrorException(null);
        }
        if (root.isNull())
            return false;
        INode node =searchNode(root,key);
        if(node == null||node.isNull())
            return false;
        else if(node.getParent().isNull()&&node.getRightChild().isNull()&&node.getLeftChild().isNull()){
            root=new Node();
            return true;
        }else if(node.getRightChild().isNull()){
            INode  nodeD = deleteLeftChild(node);
            if(node.getColor()==BLACK)
                fixColor(nodeD);
            return true;
        }else{
            INode replacedNode=Min(node.getRightChild());
            node.setKey((T) replacedNode.getKey());
            node.setValue((V) replacedNode.getValue());
            node=replacedNode;
            INode  nodeD = deleteLeftChild(node);
            if(node.getColor()==BLACK)
                fixColor(nodeD);
            return true;
        }

    }


    private INode Min(INode nodeM) {
        while(!nodeM.getLeftChild().isNull())
            nodeM = nodeM.getLeftChild();
        return nodeM;
    }

    private void fixColor(INode node) {
        while(node.getColor() == BLACK && node != root) {
            if(node.getParent().getLeftChild() == node)
                node = fixLeft(node);
            else
                node = fixRight(node);
        }
        node.setColor(BLACK);
    }
    private INode fixLeft(INode node) {
        INode w = node.getParent().getRightChild();
        if(w.getColor() == RED) {
            w.setColor(BLACK);
            node.getParent().setColor(RED);
            rotateLeftD(node.getParent());
            w = node.getParent().getRightChild();
        }
        if(w.getLeftChild().getColor() == BLACK && w.getRightChild().getColor() == BLACK) {
            w.setColor(RED);
            node = node.getParent();
        }else {
            if(w.getRightChild().getColor() == BLACK) {
                w.getLeftChild().setColor(BLACK);
                w.setColor(RED);
                rotateRightD(w);
                w = node.getParent().getRightChild();
            }
            w.setColor(w.getParent().getColor());
            node.getParent().setColor(BLACK);
            w.getRightChild().setColor(BLACK);
            rotateLeftD(node.getParent());
            node = root;

        }
        return node;
    }


    private INode fixRight(INode node) {
        INode w = node.getParent().getLeftChild();
        if(w.getColor() == RED) {
            w.setColor(BLACK);
            node.getParent().setColor(RED);
            rotateRightD(node.getParent());
            w = node.getParent().getLeftChild();
        }
        if(w.getLeftChild().getColor() == BLACK && w.getRightChild().getColor() == BLACK) {
            w.setColor(RED);
            node = node.getParent();
        }else {
            if(w.getLeftChild().getColor() == BLACK) {
                w.getRightChild().setColor(BLACK);
                w.setColor(RED);
                rotateLeftD(w);
                w = node.getParent().getLeftChild();
            }
            w.setColor(w.getParent().getColor());
            node.getParent().setColor(BLACK);
            w.getLeftChild().setColor(BLACK);
            rotateRightD(node.getParent());
            node = root;

        }
        return node;
    }

    private INode<T,V> deleteLeftChild(INode<T,V> node) {
        if(node.getParent().isNull()){
            root=root.getLeftChild();
            return root;
        }
        INode  parentNode =node.getParent();
        INode  temp = new Node();
        if(!node.getLeftChild().isNull())
            temp=node.getLeftChild();
        else
            temp=node.getRightChild();
        if(parentNode.getLeftChild()==node)
            parentNode.setLeftChild(temp);
        else
            parentNode.setRightChild(temp);
        temp.setParent(parentNode);
        return temp;
    }

    private INode searchNode(INode node, Comparable key) {
        while (!node.isNull()) {
            if (node.getKey().compareTo(key) == 0) {
                return node;
            }
            if (key.compareTo(node.getKey()) > 0) {
                node = node.getRightChild();
            } else {
                node = node.getLeftChild();
            }
        }
        return null;

    }
    private INode insertDefectedNode(INode newNode){

        while (!newNode.getParent().isNull()&&newNode.getParent().getColor()==RED&&newNode.getColor()==RED) {
            boolean isLeft = false;
            INode parentNode = newNode.getParent();
            INode grandNode = parentNode.getParent();
            INode uncleNode;
            if (!(grandNode.getLeftChild().isNull()) && (parentNode.getKey().compareTo(grandNode.getLeftChild().getKey()) == 0))
                uncleNode = grandNode.getRightChild();
            else
                uncleNode = grandNode.getLeftChild();

            if (newNode.getKey().compareTo(parentNode.getKey()) < 0)
                isLeft = true;

            if ((uncleNode != null || !uncleNode.isNull()) && uncleNode.getColor() == RED) {
                uncleNode.setColor(BLACK);
                parentNode.setColor(BLACK);
                grandNode.setColor(RED);
                newNode = grandNode;
            } else {
                if (isLeft) {//el node left
                    if (parentNode.getKey().compareTo(grandNode.getKey()) < 0) {
                        rotateRight(grandNode);
                        boolean tempNode = parentNode.getColor();
                        parentNode.setColor(grandNode.getColor());
                        grandNode.setColor(tempNode);
                    } else {
                        INode tempNode1 = rotateRight(parentNode);
                        boolean tempNode = tempNode1.getColor();
                        parentNode = tempNode1;
                        grandNode = tempNode1.getParent();
                        parentNode.setColor(grandNode.getColor());
                        grandNode.setColor(tempNode);
                        parentNode = rotateLeft(parentNode.getParent());
                    }
                } else {
                    if (parentNode.getKey().compareTo(grandNode.getKey()) < 0) {//parent left
                        INode tempNode1 = rotateLeft(parentNode);
                        parentNode = tempNode1;
                        grandNode = tempNode1.getParent();
                        boolean tempNode = parentNode.getColor();
                        parentNode.setColor(grandNode.getColor());
                        grandNode.setColor(tempNode);
                        parentNode = rotateRight(grandNode);
                    } else {
                        parentNode = rotateLeft(grandNode);
                        boolean tempNode = parentNode.getColor();
                        parentNode.setColor(grandNode.getColor());
                        grandNode.setColor(tempNode);
                    }
                }
                newNode = parentNode;
            }
        }
        return newNode;
    }
    private INode rotateLeft(INode toRotate){
        INode rightNode = toRotate.getRightChild();
        toRotate.setRightChild(rightNode.getLeftChild());
        rightNode.getLeftChild().setParent(toRotate);
        rightNode.setLeftChild(toRotate);
        rightNode.setParent(toRotate.getParent());
        if (toRotate.getParent().isNull()) {
            toRotate.getParent().setParent(new Node());
            toRotate.getParent().setLeftChild(rightNode);
        }
        else if (rightNode.getKey().compareTo(toRotate.getParent().getKey())<0)
            toRotate.getParent().setLeftChild(rightNode);
        else toRotate.getParent().setRightChild(rightNode);
        toRotate.setParent(rightNode);
        return rightNode;
    }
    private INode rotateRight(INode toRotate){
        INode leftNode = toRotate.getLeftChild();
        toRotate.setLeftChild(leftNode.getRightChild());
        leftNode.getRightChild().setParent(toRotate);
        leftNode.setRightChild(toRotate);
        leftNode.setParent(toRotate.getParent());
        if (toRotate.getParent().isNull()) {
            toRotate.getParent().setParent(new Node());
            toRotate.getParent().setLeftChild(leftNode);
        }
        else if (leftNode.getKey().compareTo(toRotate.getParent().getKey())<0)
            toRotate.getParent().setLeftChild(leftNode);
        else toRotate.getParent().setRightChild(leftNode);
        toRotate.setParent(leftNode);
        return leftNode;
    }
    private void rotateRightD(INode<T, V> node) {
        INode<T, V> l = node.getLeftChild();
        node.setLeftChild(l.getRightChild());
        if (!l.getRightChild().isNull()) {
            l.getRightChild().setParent(node);
        }
        l.setParent(node.getParent());
        if (node.getParent() .isNull()) {
            root = l;
        } else if (node == node.getParent().getLeftChild()) {
            node.getParent().setLeftChild(l);
        } else {
            node.getParent().setRightChild(l);
        }

        l.setRightChild(node);
        node.setParent(l);
    }

    private void rotateLeftD(INode<T, V> node) {
        INode<T, V> r = node.getRightChild();
        node.setRightChild(r.getLeftChild());
        if (!r.getLeftChild().isNull()) {
            r.getLeftChild().setParent(node);
        }
        r.setParent(node.getParent());
        if (node.getParent().isNull()) {
            root = r;
        } else if (node == node.getParent().getLeftChild()) {
            node.getParent().setLeftChild(r);
        } else {
            node.getParent().setRightChild(r);
        }

        r.setLeftChild(node);
        node.setParent(r);
    }

    private void printTree(INode tempNode){
        if (!tempNode.isNull()){
            printTree(tempNode.getLeftChild());
            System.out.println("k,v,C "+tempNode.getKey()+" , "+tempNode.getValue()+" "+tempNode.getColor());
            printTree(tempNode.getRightChild());
        }
    }
}