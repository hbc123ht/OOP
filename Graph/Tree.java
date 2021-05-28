package Graph;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tree {

	private int v;
	
	public List<Integer> FastestPath = new ArrayList<Integer>();
	
    private ArrayList<Integer>[] adjList;

    public int[] coloredPath;
    
    private int ava = 0;

    public Tree(int vertices)
    {
  
        // initialise vertex count
        this.v = vertices;
        this.coloredPath = new int[this.v + 1];
        // initialise adjacency list
        initAdjList();
    }
  
    @SuppressWarnings("unchecked")
    private void initAdjList()
    {
        adjList = new ArrayList[this.v + 1];
        for (int i = 1; i <= this.v; i++) {
            adjList[i] = new ArrayList<>();
        }
    }
  
    // add edge from u to v
    public void addEdge(int u, int v)
    {
        // Add v to u's list.
        adjList[u].add(v);
    }
  
    public void addAllEdge(LinkedList<Integer> [] Edge ) {
        for(int i = 1; i <= this.v; i++) {
            for (int j = 0;j < Edge[i].size(); j++){
				addEdge(i, Edge[i].get(j));
			}
		}
    }

    // Prints all paths from's' to 'd'
    public void printAllPaths(int s, int d)
    {
        boolean[] isVisited = new boolean[this.v + 1];
        ArrayList<Integer> pathList = new ArrayList<>();
  
        // add source to path[]
        pathList.add(s);
        
        // Call recursive utility
        this.ava = 0;
        DFS(s, d, isVisited, pathList);

        if (this.ava == 1){
            System.out.println("Shortest path");
            for(int i = 0; i < this.FastestPath.size(); i++)
                System.out.print((this.FastestPath.get(i)) + " ");
            Arrays.fill(coloredPath, 0);
            
            // for(int i = 1; i < this.FastestPath.size(); i++) 
            //     coloredPath[this.FastestPath.get(i)] = this.FastestPath.get(i-1);
        }
        else {
            System.out.println("No existing path from " + s + " to " + d);
        }
    }
    

    // A recursive function to print all paths from 'u' to 'd'.
    // isVisited[] keeps track of vertices in current path.
    // localPathList<> stores actual vertices in the current path
    private void DFS(Integer s, Integer d, boolean[] isVisited, List<Integer> localPathList)
    {
        if (s.equals(d)) {
            if (this.ava == 0){
                System.out.println("Different paths from "+ 1 + " to " + d);
            }   
            this.ava = 1;
            for(int i = 0; i < localPathList.size(); i++) {
            	System.out.print((localPathList.get(i)) + " ");
            }
        	System.out.println();
            if(this.FastestPath.size() > localPathList.size() || this.FastestPath.size() == 0) {
            	this.FastestPath.clear();
            	this.FastestPath.addAll(localPathList);
            }
            
            // if match found then no need to traverse more till depth
            return;
        }
  
        // Mark the current node
        isVisited[s] = true;
  
        // Recur for all the vertices
        // adjacent to current vertex
        for (Integer i : adjList[s]) {
            if (!isVisited[i]) {
                // store current node in path[]
                localPathList.add(i);
                DFS(i, d, isVisited, localPathList);
  
                // remove current node in path[]
                localPathList.remove(i);
            }
        }
  
        // Mark the current node
        isVisited[s] = false;
    }
    
}
//   