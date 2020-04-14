package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TreeMap<T extends Comparable<T>, V> implements ITreeMap {

    private IRedBlackTree tree = new RedBlackTree();
    private Set<Comparable> keySet = new HashSet<>();
    private Set<Map.Entry> entrySet = new LinkedHashSet<>();
    private int size;

    @Override
    public Map.Entry ceilingEntry(Comparable key) {
        if (key == null) {
            throw new RuntimeErrorException(null);
        }
        Iterator<Map.Entry> it = entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<T, V> entry = it.next();
            if (entry.getKey().compareTo((T) key) > 0 || entry.getKey().compareTo((T) key) == 0) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public Comparable ceilingKey(Comparable key) {
        if (key == null) {
            throw new RuntimeErrorException(null);
        }
        Map.Entry<T, V> entry = ceilingEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean containsKey(Comparable key) {
        if (key == null) throw new RuntimeErrorException(null);
        return tree.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) throw new RuntimeErrorException(null);
        Collection s = values();
        if (s.contains(value)) return true;
        return false;
    }

    @Override
    public Set<Map.Entry> entrySet() {
        entrySet = new LinkedHashSet<>();
        keySet = new LinkedHashSet<>();
        inOrder(tree.getRoot());
        return entrySet;
    }

    @Override
    public Map.Entry firstEntry() {
        INode tempNode = tree.getRoot();
        System.out.println(tree.getRoot().getKey());
        if(tempNode.isNull()) return null;
        while (tempNode.getLeftChild()!=null&&!tempNode.getLeftChild().isNull())
            tempNode = tempNode.getLeftChild();
        Map.Entry<Comparable,Object> m = new AbstractMap.SimpleEntry<>(tempNode.getKey(),tempNode.getValue());
        return m;
    }

    @Override
    public Comparable firstKey() {
        INode tempNode = tree.getRoot();
        while(tempNode.getLeftChild()!=null &&!tempNode.getLeftChild().isNull())
            tempNode = tempNode.getLeftChild();
        return tempNode.getKey();
    }

    @Override
    public Map.Entry floorEntry(Comparable key) {
        if (key == null) {
            throw new RuntimeErrorException(null);
        }
        if (tree.isEmpty()) {
            return null;
        }

        Iterator<Map.Entry> it = entrySet().iterator();
        Map.Entry<T, V> entry = null;
        while (it.hasNext()) {
            Map.Entry<T, V> entry2 = it.next();
            if (entry2.getKey().compareTo((T) key) > 0)
                break;
            entry = new AbstractMap.SimpleEntry<T, V>(entry2.getKey(), entry2.getValue());
        }
        return entry;
    }

    @Override
    public Comparable floorKey(Comparable key) {
        if (key == null) {
            throw new RuntimeErrorException(null);
        }
        Map.Entry<T, V> entry = floorEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    @Override
    public Object get(Comparable key) {
        return tree.search(key);
    }

    @Override
    public ArrayList<Map.Entry> headMap(Comparable toKey) {
        return headMap(toKey, false);
    }

    @Override
    public ArrayList<Map.Entry> headMap(Comparable toKey, boolean inclusive) {
        entrySet();
        Iterator<Map.Entry> it = entrySet.iterator();
        ArrayList<Map.Entry> m = new ArrayList<>();
        while (it.hasNext()) {
            Comparable k = (Comparable) it.next().getKey();
            if (inclusive) {
                if (k.compareTo(toKey) <= 0) {
                    m.add(new AbstractMap.SimpleEntry<>(k, get(k)));
                }
            } else {
                if (k.compareTo(toKey) < 0) {
                    m.add(new AbstractMap.SimpleEntry<>(k, get(k)));
                }
            }
        }
        return m;
    }

    @Override
    public Set keySet() {
        entrySet();
        return keySet;
    }

    @Override
    public Map.Entry lastEntry() {
        INode tempNode = tree.getRoot();
        while (tempNode.getRightChild()!=null&&!tempNode.getRightChild().isNull())
            tempNode = tempNode.getRightChild();
        INode finalTempNode = tempNode;
        if(tempNode.isNull()) return null;
        Map.Entry<Comparable,Object> m = new AbstractMap.SimpleEntry<>(tempNode.getKey(),tempNode.getValue());
        return m;
    }

    @Override
    public Comparable lastKey() {
        INode tempNode = tree.getRoot();
        while(tempNode.getRightChild()!=null &&!tempNode.getRightChild().isNull())
            tempNode = tempNode.getRightChild();
        return tempNode.getKey();
    }

    @Override
    public Map.Entry pollFirstEntry() {
        Map.Entry mE = firstEntry();
        if(mE == null) return null;
        System.out.println(firstEntry());
        tree.delete((Comparable) mE.getKey());
        return mE;
    }

    @Override
    public Map.Entry pollLastEntry() {
        Map.Entry mE = lastEntry();
        if (mE == null) return null;
        tree.delete((Comparable) mE.getKey());
        return mE;
    }

    @Override
    public void put(Comparable key, Object value) {
        tree.insert(key,value);
    }

    @Override
    public void putAll(Map map) {
        if(map == null) throw new RuntimeErrorException(null);
        Set s = map.keySet();
        for(Object it: s)
            tree.insert((Comparable) it, map.get(it));
    }

    @Override
    public boolean remove(Comparable key) {
        return tree.delete(key);
    }

    @Override
    public int size() {
        size = 0;
        inOrder(tree.getRoot());
        return size;
    }

    @Override
    public Collection values() {
        entrySet = new HashSet<Map.Entry>();
        Collection<Object> vColl = new ArrayList();
        inOrder(tree.getRoot());
        ArrayList<Map.Entry> a = new ArrayList(entrySet);
        for(int i=0;i<entrySet.toArray().length;i++)
            vColl.add(a.get(i).getValue());
        return vColl;
    }

        private void inOrder(INode tempNode){
        if (!tempNode.isNull()){
            inOrder(tempNode.getLeftChild());
            entrySet.add(new AbstractMap.SimpleEntry<>(tempNode.getKey(), tempNode.getValue()));
            size++;
            keySet.add(tempNode.getKey());
            inOrder(tempNode.getRightChild());
        }
    }
}
