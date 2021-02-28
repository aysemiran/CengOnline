package com.example.cengonline;

public class Stack {

        private Object[] elements;
        private int top;

        public Stack(int capacity)
        {
            elements=new Object[capacity];
            top=-1;
        }

        public boolean isFull()
        {
            if(elements.length==top+1)
                return true;
            else return false;
        }
        public boolean isEmpty()
        {
            if(top==-1)
                return true;
            else return false;
        }

        public int size()
        {
            return top+1;
        }
        public void push(Object data)
        {
            if(isFull())
            {

            }
            else
            {
                top++;
                elements[top]=data;
            }
        }

        public Object pop()
        {
            if(isEmpty())
            {

                return null;
            }
            else
            {
                Object temp=elements[top];
                top--;
                return temp;
            }
        }

        public Object peek()
        {
            if(isEmpty())
            {

                return null;
            }
            else
            {
                return elements[top];
            }
        }

}
