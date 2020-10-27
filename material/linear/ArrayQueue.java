package material.linear;




public class ArrayQueue<E> {


    private int size;
    private E[] queue;
    private int first = -1;
    private int last = -1;

    public ArrayQueue(int maxsize){
        this.size = 0;
        this.queue = (E[]) new Object[maxsize];
    }
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
    public boolean isEmpty(){
        return this.size == 0;
    }

    /**
     * Inspects the element at the front of the queue.
     * @return element at the front of the queue.
     */
    public E front(){
        if (this.size == 0){
            throw new RuntimeException("Error! Empty queue");
        }else{
            return queue[first];
        }
    }

    /**
     * Inserts an element at the rear of the queue.
     * @param element new element to be inserted.
     */
    public void enqueue(E element){
        if(this.size == 0){
            queue[0] = element;
            first = 0;
            last = 0;
            //Cuando está llena, se redimensiona y mete el elemento en el else de debajo. Puede mejorar el código
        }else if(this.size == queue.length){
            E[] newQueue = (E[]) new Object[queue.length*2];
            for (int i = first; i<=size; i++){
                newQueue[i % newQueue.length] = queue[i % queue.length];
            }
            queue = newQueue;
        }else{
            last = (last + 1) % queue.length;
            queue[last] = element;
        }
        this.size++;
    }

    /**
     * Removes the element at the front of the queue.
     * @return element removed.
     */
    public E dequeue() {
        if (isEmpty())
            throw new RuntimeException("Void queue");
        E elem = (E) queue[first];
        if (size == 1) {
            first = -1;
            last = -1;
            this.size--;
        } else {
            first = (first + 1) % queue.length;
            this.size--;
        }
        return elem;
    }
}
