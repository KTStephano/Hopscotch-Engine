package engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A QuadTree sections off the world into 4 quadrants. From here
 * you can add new objects to the tree, and when a certain density
 * level is hit in any one of its quadrants, it further splits that
 * quadrant into 4 new quadrants.
 *
 * The main purpose for doing this is that objects within similar regions
 * of space can be identified as a group very easily. For example, the PhysicsSimulation
 * component uses this to determine who is close enough to potentially
 * collide with each other while disregarding all other game objects.
 *
 * This is a common data structure used in video games (alongside Octrees and Binary Trees),
 * so it should be easy to find information online about it!
 *
 * @author Justin Hall
 *
 * @param <E> Type of objects stored in the tree (must extend Actor)
 */
public class QuadTree<E extends Actor> implements Iterable<E> {
    private class QuadNode {
        private int _startX;
        private int _startY;
        private int _widthHeight;
        private int _edgeX;
        private int _edgeY;
        private final int _loadFactor;
        private final int _threshold;
        private ArrayList<QuadNode> _children = null;
        private HashSet<E> _objects = new HashSet<>();

        /**
         * Primary constructor
         * @param startX starting x coordinate (based in world coordinates)
         * @param startY startiny y coordinate (based in world coordinates)
         * @param widthHeight width and height of the region of space this quad node represents
         * @param loadFactor how dense the node has to become before splitting
         * @param threshold threshold that falling below prevents further splitting
         *                  -> when widthHeight / 2 is lower than this number, splitting stops
         */
        public QuadNode(int startX, int startY, int widthHeight, int loadFactor, int threshold) {
            _startX = startX;
            _startY = startY;
            _widthHeight = widthHeight;
            _loadFactor = loadFactor;
            _threshold = threshold;
            _edgeX = _startX + _widthHeight;
            _edgeY = _startY + _widthHeight;
        }

        public boolean add(E a) {
            if (!intersects(a)) return false; // Actor not within this node's region
            if (!_objects.add(a)) return true; // Already added
            if (_children == null) {
                if (_objects.size() >= _loadFactor && (_widthHeight / 2) > _threshold) {
                    _split();
                }
            }
            else {
                for (QuadNode node : _children) node.add(a);
            }
            return true;
        }

        public boolean contains(E a) {
            return _objects.contains(a);
        }

        public boolean remove(E a) {
            boolean removed = _objects.remove(a);
            if (removed && _children != null) {
                for (QuadNode node : _children) node.remove(a);
            }
            return removed;
        }

        public int size() {
            return _objects.size();
        }

        public boolean intersects(E a) {
            // Check and see if the object should even be added here
            int width = (int)a.getWidth();
            int height = (int)a.getHeight();
            int x = (int)a.getLocationX();
            int y = (int)a.getLocationY();
            return intersects(x, y, width, height);
        }

        public boolean intersects(int x, int y, int width, int height) {
            // Check and see if the object should even be added here
            int widthHeight = width > height ? width : height;
            int edgeX = x + widthHeight;
            int edgeY = y + widthHeight;

            if ((_startX > edgeX) || (x > _edgeX) || (_startY > edgeY) || (y > _edgeY)) return false;
            return true;
        }

        public void clear() {
            _objects.clear();
            if (_children == null) return;
            for (QuadNode node : _children) {
                node.clear();
            }
        }

        public HashSet<E> getActors() {
            return _objects;
        }

        public ArrayList<QuadNode> getChildren() {
            return _children;
        }

        private void _split() {
            _children = new ArrayList<>();
            int newWidthHeight = _widthHeight / 2;
            // Add the 4 new children
            _children.add(new QuadNode(_startX, _startY, newWidthHeight, _loadFactor, _threshold));
            _children.add(new QuadNode(_startX + newWidthHeight, _startY, newWidthHeight, _loadFactor, _threshold));
            _children.add(new QuadNode(_startX, _startY + newWidthHeight, newWidthHeight, _loadFactor, _threshold));
            _children.add(new QuadNode(_startX + newWidthHeight, _startY + newWidthHeight, newWidthHeight,
                    _loadFactor, _threshold));
            for (E obj : _objects) {
                for (QuadNode node : _children) {
                    node.add(obj);
                }
            }
        }
    }

    public class LeafIterator implements Iterator<HashSet<E>> {
        private LinkedList<HashSet<E>> _lists = new LinkedList<>();

        private LeafIterator(QuadNode node) {
            _buildRecursive(node);
        }

        @Override
        public boolean hasNext() {
            return _lists.size() > 0;
        }

        @Override
        public HashSet<E> next() {
            return _lists.pollFirst();
        }

        private void _buildRecursive(QuadNode node) {
            ArrayList<QuadNode> children = node.getChildren();
            if (children == null) {
                _lists.add(node.getActors());
                return;
            }
            for (QuadNode childNode : children) _buildRecursive(childNode);
        }
    }

    private QuadNode _root;
    private final int _loadFactor;
    private final int _splitThreshold;

    /**
     * Note: This assigns default values for load factor and split threshold
     *
     * @param startX starting x coordinate for the tree's region of space (in world coordinates)
     * @param startY starting y coordinate for the tree's region of space (in world coordinates)
     * @param widthHeight width and height of the region of space this tree oversees
     */
    public QuadTree(int startX, int startY, int widthHeight) {
        _loadFactor = 10;
        _splitThreshold = 100;
        _root = new QuadNode(startX, startY, widthHeight, _loadFactor, _splitThreshold);
    }

    /**
     * Use this constructor to specify the load factor and split threshold
     *
     * @param startX starting x coordinate for the tree's region of space (in world coordinates)
     * @param startY starting y coordinate for the tree's region of space (in world coordinates)
     * @param widthHeight width and height of the region of space this tree oversees
     * @param loadFactor how many elements need to be added to a quadrant before it further splits
     * @param splitThreshold when widthHeight / 2 falls below this number, splitting stops permanently
     */
    public QuadTree(int startX, int startY, int widthHeight,
                    int loadFactor, int splitThreshold) {
        _loadFactor = loadFactor;
        _splitThreshold = splitThreshold;
        _root = new QuadNode(startX, startY, widthHeight, _loadFactor, _splitThreshold);
    }

    public boolean add(E a) {
        return _root.add(a);
    }

    public boolean contains(E a) {
        return _root.contains(a);
    }

    public boolean remove(E a) {
        return _root.remove(a);
    }

    public void clear() {
        _root.clear();
    }

    public int size() {
        return _root.size();
    }

    HashSet<E> getAllActors() {
        return _root.getActors();
    }

    HashSet<E> getActorsWithinArea(int x, int y, int width, int height) {
        HashSet<E> set = new HashSet<>(size());
        _getActorsWithinAreaRecursive(set, _root, x, y, width, height);
        return set;
    }

    public Iterator<HashSet<E>> getLeafIterator() {
        return new LeafIterator(_root);
    }

    @Override
    public Iterator<E> iterator() {
        return _root.getActors().iterator();
    }

    private void _getActorsWithinAreaRecursive(HashSet<E> set, QuadNode node, int x, int y, int width, int height) {
        ArrayList<QuadNode> childNodes = node.getChildren();
        boolean intersects = node.intersects(x, y, width, height);
        if (childNodes == null && intersects) set.addAll(node.getActors());
        else if (intersects) {
            for (QuadNode childNode : childNodes) {
                _getActorsWithinAreaRecursive(set, childNode, x, y, width, height);
            }
        }
    }
}
