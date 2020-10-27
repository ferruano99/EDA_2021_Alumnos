package material.tree.narytree;

import material.Position;
import material.tree.iterators.BFSIterator;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * A linked class for a tree where nodes have an arbitrary number of children.
 *
 * @author Raul Cabido, Abraham Duarte, Jose Velez, Jesús Sánchez-Oro
 * @param <E> the elements stored in the tree
 */
public class LCRSTree<E> implements NAryTree<E> {


    private class TreeNode<E> implements Position<E>{

        private E element;
        private TreeNode<E> leftChild;


        public TreeNode<E> getParent() {
            return parent;
        }

        public void setParent(TreeNode<E> parent) {
            this.parent = parent;
        }


        private TreeNode<E> parent;
        private TreeNode<E> rightSibling;
        private LCRSTree<E> myTree;


        public TreeNode(E element, TreeNode<E> parent, TreeNode<E> leftChild, TreeNode<E> rightSibling, LCRSTree<E> myTree) {
            this.element = element;
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightSibling = rightSibling;
            this.myTree = myTree;
        }

        public void setElement(E element) {
            this.element = element;
        }

        public TreeNode<E> getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(TreeNode<E> leftChild) {
            this.leftChild = leftChild;
        }

        public TreeNode<E> getRightSibling() {
            return rightSibling;
        }

        public void setRightSibling(TreeNode<E> rightSibling) {
            this.rightSibling = rightSibling;
        }

        public LCRSTree<E> getMyTree() {
            return myTree;
        }

        public void setMyTree(LCRSTree<E> myTree) {
            this.myTree = myTree;
        }

        @Override
        public E getElement() {
            return element;
        }
    }


    private int size;
    private TreeNode<E> root;


    public LCRSTree(int size, TreeNode<E> root) {
        this.size = size;
        this.root = root;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Position<E> root() throws RuntimeException {
        return this.root;
    }

    private TreeNode<E> checkPosition(Position<E> p) throws RuntimeException{
        if (!(p instanceof TreeNode)){
            throw new RuntimeException("Invalid position");
        }
        TreeNode<E> aux = (TreeNode<E>) p;
        if((aux.getMyTree() != this) || (this.size == 0)){
            throw new RuntimeException("Node does not belong to the tree");
        }

        return aux;
    }

    @Override
    public Position<E> parent(Position<E> v) throws RuntimeException {
        TreeNode<E> node = checkPosition(v);
        TreeNode<E> parentPos = node.getParent();

        if(parentPos == null){
            throw new RuntimeException("No parent");
        }
        return parentPos;
    }

    @Override
    public Iterable<? extends Position<E>> children(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        //Creamos un array para devolver la lsita de hijos
        ArrayDeque<Position<E>> childrenQueue = new ArrayDeque<>();
        if(isInternal(node)){
            //creamos un auxiliar para ir recorriendo los nodos del padre
            TreeNode<E> aux = node.getLeftChild();
            do{
                //Añadimos por el porincipio
                childrenQueue.addFirst(aux);
                //vamos al hermano
                aux = aux.getRightSibling();
            //Hasta q sea distinto de null, seguimos recorriendo la lista de hermanos
            }while(aux != null);
        }

        return childrenQueue;
    }

    @Override
    public boolean isInternal(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return node.getLeftChild() != null;
    }

    @Override
    public boolean isLeaf(Position<E> v) throws RuntimeException {
        TreeNode<E> node = checkPosition(v);
        return node.getLeftChild() == null;
    }

    @Override
    public boolean isRoot(Position<E> v) {
        TreeNode<E> node = checkPosition(v);
        return (node == this.root);
    }

    @Override
    public Position<E> addRoot(E e) throws RuntimeException {
        if (!isEmpty()){
            throw new RuntimeException("Root already exists");
        }
        size = 1;
        TreeNode<E> root = new TreeNode<>(e,null,null,null,this);

        return root;
    }

    @Override
    public Iterator<Position<E>> iterator() {
        return new BFSIterator<>(this);
    }

    @Override
    public E replace(Position<E> p, E e) {
        TreeNode<E> node = checkPosition(p);
        E temp = p.getElement();
        node.setElement(e);
        return temp;

    }

    @Override
    public void swapElements(Position<E> p1, Position<E> p2) {
        TreeNode<E> node1 = checkPosition(p1);
        TreeNode<E> node2 = checkPosition(p2);
        E temp = p2.getElement();
        node2.setElement(p1.getElement());
        node1.setElement(temp);
    }

    @Override
    public Position<E> add(E element, Position<E> p) {
        TreeNode<E> parent = checkPosition(p);
        TreeNode<E> newNode = new TreeNode<E>(element,parent,null,null,this);
        if(parent.getLeftChild() == null){
            parent.setLeftChild(newNode);
        }else{
            newNode.setRightSibling(parent.getLeftChild());
            parent.setLeftChild(newNode);
        }
        return newNode;
    }

    @Override
    public void remove(Position<E> p) {
        TreeNode<E> node = checkPosition(p);
        TreeNode<E> nodeParent = node.getParent();
        //Caso de la raíz
        if(isRoot(node)){
            root = null;
            //Cualquier otro caso
        }else{
            //Caso de hermanos
            //Cogemos el padre
            TreeNode<E> aux = nodeParent.getLeftChild();
            //Caso de que sea exactamente el hijo izquierdo del padre
            if(aux == node){
                //Hacemos que el padre apunte al hermano de nuestr nodo
                nodeParent.setLeftChild(node.getRightSibling());
            //Caso de que sea un hermano de por el medio
            }else{
                //Hay q ir avanzando el auxiliar hasta que sea el anterior
                while(aux.getRightSibling() != node){
                    aux = aux.getRightSibling();
                }
                //hacemos q el anterior apunte al sigueinte y desreferenciamos nuestro nodo
                aux.setRightSibling(node.getRightSibling());

            }
            //Reconstrimos el size
            //Iterador que empiece desde nuestr nodo y creamos tb un contador
            BFSIterator<E> it = new BFSIterator<>(this,node);
            int count = 0;
            while(it.hasNext()){
                count++;
                //Creamos un TreeNode para desreferenenciar el mytree
                TreeNode<E> desreference = (TreeNode<E>) it.next();
                desreference.setMyTree(null);
            }
            this.size -= count;
        }
    }

    @Override
    public void moveSubtree(Position<E> pOrig, Position<E> pDest) throws RuntimeException {
        //caso que los dos sean iguales
        if(pOrig == pDest){
            throw new RuntimeException("Same nodes");
        }

        TreeNode<E> nodeOrig = checkPosition(pOrig);
        TreeNode<E> nodeDest = checkPosition(pDest);
        if(isAncestor(nodeOrig,nodeDest)){
            throw new RuntimeException("Ancestors");
        }
        if(pOrig == root){
            throw new RuntimeException("Origin is root");
        }
        TreeNode<E> parentOrigin = nodeOrig.getParent();

        //Nodo padre del origen: cogemos el hermano del nodo origen derecho como hijo izquierdo
        parentOrigin.setLeftChild(nodeOrig.getRightSibling());

        //El hermano derecho del nodo origen pasa a ser el hijo izqierdo del nodo destino
        nodeOrig.setRightSibling(nodeDest.getLeftChild());
        //El nodo origen está en nodo destino
        nodeDest.setLeftChild(nodeOrig);
    }

    public boolean isAncestor(TreeNode<E> n1, TreeNode<E> n2){
        boolean toReturn = false;
        TreeNode<E> aux = n2;


        while(aux.getParent() != root){
            aux = aux.getParent();
            if(aux == n1){
                toReturn = true;
            }
        }
        return toReturn;
    }
}
