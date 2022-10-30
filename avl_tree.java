/*CPU Process Scheduler!
This program simulates a Linux CPU Scheduler
A AVL tree is created with 5000 integers inserted then they are remeved sequentially 
everytime a node is removed a new random node is added to another AVL tree
when the first tree is empty, the tree's swap and the process begins again
Author: James Gouveia Csc 130 T/Th
March 2020

Please see design sheet for an explanation of the program.  The design sheet file is named: CSC130_Assignment1_design_doc.pdf */

import java.util.Random;
import java.util.Scanner;
import java.time.ZonedDateTime;
import java.util.Date;

class Node 
{
	int id, height;
    Node left;
    Node right;

    //constructor with a key
    public Node(int key)
    {
        id = key; 
        height = 0;
        left = null;
        right = null;
    } 
}

class Tree
{
	Node root;

	public Tree()
	{
		root = null;
	}

	public Node insert(int newid, Node t)
	{
		if(t == null)
		{
			return new Node(newid);
		}
		if(newid < t.id)
		{
			t.left = insert(newid, t.left);
			if(height(t.left) - height(t.right) == 2)
			{
				if(t.left.id > newid)
				{
					t = rightRotate(t);
				}
				else if(t.left.id < newid)
				{
					t = leftRightRotate(t);
				}
			}
		}
		else if(newid > t.id)
		{
			t.right = insert(newid, t.right);
			if(height(t.right) - height(t.left) == 2)
			{
				if(t.right.id < newid)
				{
					t = leftRotate(t);
				}
				else if(t.right.id > newid)
				{
					t = rightLeftRotate(t);
				}
			}
		}
		else //id collision
		{

		}
		t.height = 1 + Math.max(height(t.left), height(t.right));
		return t;
	}

	public Node remove(int key, Node root)
	{

		
		//if the tree is empty
		if(root == null)
		{
			return null;
		}

		//found the node to remove and its now called root
		//3 cases to consider: no children, one child, two children
		if(root.id == key)
		{
			
			//if no children
			if(root.left == null && root.right == null)
			{
				//If there is no children return null which erases this node
				return null;
			}
			
			//one child
			if(root.left == null)
			{
				//If one child return the node to be deleted reset to the data from the children
				root.id = root.right.id;
				root.left = root.right.left;
				root.right = root.right.right;
				root.height = 1 + Math.max(height(root.left), height(root.right));
				return root;
			}
			else if(root.right == null)
			{
				//If one child return the node to be deleted reset to the data from the children
				Node nodeHolder = root;
				root.id = root.left.id;
				root.right = nodeHolder;
				root.left = root.left.left;
				root.right = root.left.right;
				root.height = 1 + Math.max(height(root.left), height(root.right));
				return root;
			
			}
			//2 children
			else if(root.left != null && root.right !=null)
			{
				Node nodeHolder = root;
				root = returnMin(root.right);
				remove(root.id, nodeHolder.right);
				root.left = nodeHolder.left;
				root.right = nodeHolder.right.id == root.id ? null : nodeHolder.right;
   				root.height = 1 + Math.max(height(root.left), height(root.right));
				return root;
			}			
    	}	

		if(key < root.id)
		{
			if(root.left != null)
			{
				root.left = remove(key, root.left);
			}
			else//node not found
			{

			}

			//Rebalance if necessary
			if(height(root.right) - height(root.left) > 1)
			{ 
				if(root.right.id > key)
				{
					root = leftRotate(root);
				}   
				else if(root.right.id < key)
				{
					root = rightLeftRotate(root);
				}
				
			}

		}
		else if(key > root.id)
		{
			if(root.right != null)
			{
				root.right = remove(key, root.right);
			}
			else//node not found
			{

			}
			//Rebalance if necessary
			if(height(root.left) - height(root.right) > 1)
			{        
				if(root.left.id < key)
				{
					root = rightRotate(root);
				}               
				else if(root.left.id > key)
				{
					root = leftRightRotate(root);
				}
			}
		}
		root.height = 1 + Math.max(height(root.left), height(root.right));
		return root;
	}
	public Node returnMin(Node t)
	{	
		if(t.left == null)
		{
			return t;
		}
		else
		{
			return returnMin(t.left);
		}
	}

	public int height(Node t)
    {
    	if(t != null)
    	{
    		return t.height;
    	}
    	else
    	{
    		return -1;
    	} 
    } 	

	public Node rightRotate(Node t)
	{
		Node k2 = t.left;
		t.left = k2.right;
		k2.right = t;
		t.height = 1 + Math.max(height(t.left), height(t.right));
		k2.height = 1 + Math.max(height(t), height(k2.left));
		
		return k2;
	}

	public Node leftRotate(Node t)
	{
		Node k2 = t.right;
		t.right = k2.left;
		k2.left = t;
		t.height = 1 + Math.max(height(t.left), height(t.right));
		k2.height = 1 + Math.max(height(t), height(k2.right));
		
		return k2;
	}

	public Node leftRightRotate(Node t)
	{
		t.left = leftRotate(t.left);
		return rightRotate(t);
	}

	public Node rightLeftRotate(Node t)
	{
		t.right = rightRotate(t.right);
		return leftRotate(t);
	}

	public int findMin(Node t)
	{
		if(t.left != null)
		{
			return findMin(t.left);
		}
					
		return t.id;
	}
    
    public static void main(String[] args)
    {
        Tree theTree = new Tree();
        Random rand = new Random();

        int[] processArray = new int[5000];

        for(int i = 0; i < 4999; i++)
        {
        	processArray[i] = rand.nextInt(4098);
        	theTree.root = theTree.insert(processArray[i], theTree.root);
        }
		
        int minValue;
        Tree theSecondTree = new Tree();
        long start = System.nanoTime();
        do
        {
        	minValue = theTree.findMin(theTree.root);
        	System.out.format("The process with a priority of %d is now scheduled to run!\n\n", minValue);
        	theTree.root = theTree.remove(minValue, theTree.root);
        	theSecondTree.root = theSecondTree.insert(rand.nextInt(4098), theSecondTree.root);
        	System.out.format("The process with a priority of %d has run out of it's timeslice!\n\n", minValue);

        	if(theTree.root == null)
        	{
        		long finish = System.nanoTime();
				long timeElapsed = finish - start;
		 		System.out.println("Program Duration: "+ timeElapsed/ 1000000);
        		System.out.println("Every process has got a chance to run; please press \"Enter\" to start the next round!\n");
        		Scanner scanner = new Scanner(System.in);
				String readString = scanner.nextLine(); 
				//Switch the two AVL trees
				theTree.root = theSecondTree.root;
				theSecondTree.root = null;
        	}
        }while(true);

    }
}