package eg.edu.alexu.csd.filestructure.redblacktree;

public class Node<T extends Comparable<T>, V> implements INode {
    static final boolean BLACK = false;
    private INode parent = null;
    private INode leftChild = null;
    private INode rightChild = null;
    private boolean color = BLACK; //False = Black 🖤
    private T key = null;
    private V value =null;


    @Override
    public void setParent(INode parent) {
        this.parent = parent;
    }

    @Override
    public INode getParent() {
        return parent;
    }

    @Override
    public void setLeftChild(INode leftChild) {
//        newNode.setParent(tempNode.getParent());
//        this.leftChild = new Node();
        this.leftChild = leftChild;
//        this.leftChild.setParent(this);
//        System.out.println("************ "+leftChild.getRightChild().getKey());
    }

    @Override
    public INode getLeftChild() {
        return this.leftChild;
    }

    @Override
    public void setRightChild(INode rightChild) {
//        this.rightChild = new Node();
//        this.rightChild.setParent(new Node());
//        this.rightChild.setParent(this);
//        this.rightChild.setKey(rightChild.getKey());
//        this.rightChild.setValue(rightChild.getValue());
        this.rightChild = rightChild;
//        System.out.println("************ "+leftChild.getRightChild().getKey());
    }

    @Override
    public INode getRightChild() {
        return this.rightChild;
    }

    @Override
    public Comparable getKey() {
        return this.key;
    }

    @Override
    public void setKey(Comparable key) {
        this.key = (T) key;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (V) value;
    }

    @Override
    public boolean getColor() {
        return this.color;
    }

    @Override
    public void setColor(boolean color) {
        this.color = color;
    }

    @Override
    public boolean isNull() {
        return key==null;
    }
}

