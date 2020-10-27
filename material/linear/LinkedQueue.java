package material.linear;

public class LinkedQueue<E> {
    private class Node<E>{
        private Node next;

        public Node getNext() {
            return this.next;
        }

        public void setNext(final Node next) {
            this.next = next;
        }

        public E getElem() {
            return this.elem;
        }

        public void setElem(final E elem) {
            this.elem = elem;
        }

        private E elem;

        public Node(Node n, E e){
            this.next = n;
            this.elem = e;
        }
    }
    private int size;
    private Node<E> first;
    private Node<E> last;



    /**
     * Returns the number of elements in the queue.
     * @return number of elements in the queue.
     */
    public int size(){
        return this.size;
    }

    /**
     * Returns whether the queue is empty or not.
     * @return true if the queue is empty, false otherwise.
     */
    boolean isEmpty(){
        return this.size == 0;
    }

    /**
     * Inspects the element at the front of the queue.
     * @return element at the front of the queue.
     */
    public E front(){
        if (this.size == 0){
            throw new RuntimeException("No front due to empty queue");
        }
        return this.first.getElem();
    }

    /**
     * Inserts an element at the rear of the queue.
     * @param element new element to be inserted.
     */
    public void enqueue(E element){
        Node<E> newNode;
        if(isEmpty()){
            newNode = new Node<>(null,element);
            this.first = newNode;
            this.last = newNode;
        }else{
            newNode = new Node<>(null,element);
            this.last.setNext(newNode); //hace que el anterior apunte al nuevo nodo
            this.last = newNode; //refrescamos el último

        }
        this.size++;

    }

    /**
     * Removes the element at the front of the queue.
     * @return element removed.
     */
    public E dequeue(){
        if (isEmpty())
            throw new RuntimeException("Empty queue");

        E elem = this.first.getElem();

        if (size == 1){
            first = null;
            last = null;
        }
        else{
            this.first = first.getNext();
        }
        this.size--;
        return elem;
    }

}
