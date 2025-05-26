import java.io.*;
import java.util.*;

/*
* Name: Meet Sagar
* Student ID: 7925852
 */
public class VirusTree {

    public static class VirusNode {
        String name;
        VirusNode firstChild;
        VirusNode nextSibling;
        boolean visited;

        public VirusNode(String name) {
            this.name = name;
            this.firstChild = null;
            this.nextSibling = null;
        }


        public void display() {
            System.out.println("Virus Name: " + name);
        }
    }

    private VirusNode root;
    private BufferedWriter writer;


    public VirusTree() throws IOException {
        root = null;
        FileWriter ofile = new FileWriter("outputVirus.txt");  //opening a File Writer to store the reading from the input file
        writer = new BufferedWriter(ofile);
    }


    public void create(String fileName) throws FileNotFoundException, IOException {  //Method to read the input and creating the binary tree of the viruses
        File ifile = new File(fileName);
        if (!ifile.exists()) {
            throw new FileNotFoundException("Input file not found: " + fileName); // If file does not exist, gives an error FileNotFoundException
        }

        Scanner sc = new Scanner(ifile);  // Scanner to read from the file

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();   // Trimming the line
            if (line.isEmpty()) continue;

            StringTokenizer tokenizer = new StringTokenizer(line, ","); // Tokenizer to split the line into tokens based on " , '
            if (!tokenizer.hasMoreTokens()) continue;

            String parentName = tokenizer.nextToken().trim(); //Creates a string called parentName which in this case is "Virus"
            if (parentName.isEmpty()) continue;

            VirusNode parentNode = createNode(parentName);  //Creates a Node to store the parentName

            VirusNode Child = null;  //Setting Child node to null

            while (tokenizer.hasMoreTokens()) {     //Inner loop for adding the children
                String childName = tokenizer.nextToken().trim();    //Now trimming the string childName
                if (childName.isEmpty()) continue;

                VirusNode childNode = createNode(childName);       // Creating a Node to store the childName

                if (Child == null) {
                    parentNode.firstChild = childNode;  //If child is null, then we linked the firstChild (DNA Virus) to the parentNode
                } else {
                   Child.nextSibling = childNode; // Child's nextSibling will be set to the childNode which in this case is firstChild's nextSibling(RNA Virus)

                }
               Child = childNode; //Update the Child to childNode which would be RNA virus in this case
            }


            System.out.print("Parent: " + parentName + " -> ");  // Prints the parent name
            VirusNode child = parentNode.firstChild; //Retrives the first child of the parentNode
            StringBuilder b = new StringBuilder(); // Used to build the string of child Nodes, holds sequence of child names for printing and writing to file
            while (child != null) { // Iterates through all the children of parentNode
                b.append(child.name); //appends current child's name to stringbuilder, first "DNA Virus" will append to b
                if (child.nextSibling != null) { //checks if there is another sibling after current child
                    b.append(" -> ");
                }
                child = child.nextSibling;  // Moves to next Sibling of the current child untill there are no more siblings
            }
            System.out.println(b.toString());  // prints complete parent-child relationship


            writer.write("Parent: " + parentName + " -> " + b.toString());  //writes the same output to a file
            writer.newLine();  //adds new line
        }

        sc.close();
        writer.close();
    }
    public VirusNode createNode(String name) { // This method creates a virusNode if it doesn't exist yet in a tree, if it already exists, it returns exisiting node
        if (root == null) {           //checks if root is null, tree is empty and creates root node with given name and return it
            root = new VirusNode(name); // example; first time createNode(Virus) is called, it creates the root node"virus"
            return root;
        }

        VirusNode existing = find(root, name);  //calls find method to check if a node with the given name already exists, if it does, returns existing node
        if (existing != null) { //if node already exists, it simply returns the existing node
            return existing;
        } else {

            VirusNode newNode = new VirusNode(name);
            VirusNode sibling = root.firstChild;
            if (sibling == null) {
                root.firstChild = newNode;
            } else {
                while (sibling.nextSibling != null) {
                    sibling = sibling.nextSibling;
                }
                sibling.nextSibling = newNode;
            }
            return newNode;
        }
    }
    public VirusNode find(VirusNode node, String name) { //Method searches for a node with given name in tree, starting from node
        if (node == null) {
            return null;
        }
        if (node.name.equals(name)) { //current node's name matches with the given name, it returns current node
            return node;
        }
        VirusNode found = find(node.firstChild, name); //Recursively calls find to search through first child of current node
        if (found!= null) { //if node is found in child subtree, returns found node
            return found;
        }
        return find(node.nextSibling, name); // if it dosen't, it recursively searches the nextSibling of current Node
    }
    // Recursive method to calculate height
     public int height(VirusNode node) {
        if (node == null) {
            return -1;
        }
        int childHeight = height(node.firstChild);
        return 1 + childHeight;
    }
/*The time complexity of the height method is O(n) because of the following reasons:
1. The method needs to visit every node in the tree in order to find the height.
* 2. Each node is processed once with constant time operation and total number of nodes is directly proportional to total number
* of operations*/

    public void breadthFirst() {
        if (root == null) {
            return;
        }

        VirusNode[] queue = new VirusNode[1000000]; // Assuming this is large enough
        int front = 0;
        int rear = 0;

        queue[rear++] = root;
        root.visited = true;

        while (front < rear) {
            VirusNode current = queue[front];
            front++;
            System.out.print(current.name + " ");

            VirusNode child = current.firstChild;
            while (child != null) {
                if (!child.visited) {
                    queue[rear] = child;
                    rear++;
                    child.visited = true;
                }
                child = child.nextSibling;
            }
        }
        System.out.println();

        // Reset visited flags
        resetVisited(root);
    }

    private void resetVisited(VirusNode node) {
        if (node == null) {
            return;
        }
        node.visited = false;
        resetVisited(node.firstChild);
        resetVisited(node.nextSibling);
    }
    public void preOrder(VirusNode root){
        if(root == null){
            return;
        }
        System.out.println(root.name + " ");
        VirusNode child =  root.firstChild;
        while(child!=null) {
            if(!child.visited) {
                preOrder(child);
                child.visited = true;
            }
            child = child.nextSibling;
        }

    }
    public void postOrder(VirusNode root) {
        if (root == null) {
            return;
        }

        Stack<VirusNode> stack = new Stack<>();
        root.visited = true;

        stack.push(root);

        while (!stack.isEmpty()) {
            VirusNode current = stack.peek();

            if (current.firstChild != null && !current.firstChild.visited) {
                // Visit left child
                current.firstChild.visited = true;
                stack.push(current.firstChild);
            } else if (current.nextSibling != null && !current.nextSibling.visited) {
                // Visit right sibling
                current.nextSibling.visited = true;
                stack.push(current.nextSibling);
            } else {
                // All children visited, process current node
                stack.pop();
                System.out.println(current.name + " ");
            }
        }
    }

public void Distance(String name1, String name2){
        VirusNode node1= find(root, name1);
        VirusNode node2 = find(root, name2);

        if(node1==null || node2 == null){
            System.out.println("Does not exist");
            return;
        }

    }

    public static void main(String[] args) {
       try {
           VirusTree virusTree = new VirusTree();
           virusTree.create("tree_of_virus_input.txt");
           //Created a button to efficiently organize and run the program
            while(true) {
                System.out.println("1. Height \t 2. Breadth-First Traversal \t 3. Preorder Traversal \t 4.Postorder Traversal \t 5. Distance \t 6.Exit");
                Scanner sc = new Scanner(System.in);
                int button = sc.nextInt();
                switch (button) {
                    case 1:
                        System.out.println("Height of the tree is: " + virusTree.height(virusTree.root));
                        break;
                    case 2:
                        System.out.println("Breadth-First Traversal: ");
                        virusTree.breadthFirst();
                        break;
                    case 3:
                        System.out.println("PreOrder Traversal: ");
                        //virusTree.preOrder(virusTree.root);
                        break;
                    case 4:
                        System.out.println("PostOrder Traversal: ");
                        virusTree.postOrder(virusTree.root);   //To execute this program, comment " virusTree.preOrder(virusTree.root);" line
                        break;
                    case 5:
                        virusTree.Distance("“HCoV-OC43", "Hcov-229E");
                        virusTree.Distance("SARS-CoV", "Zika virus");
                        break;
                    case 6:
                        System.out.println("Exiting.....");System.exit(0);
                    default:
                        System.out.println("Error");

                }

            }
       }

       catch(FileNotFoundException e){
           System.out.println("Error Reading file"+ e.getMessage());
        }
       catch (IOException e){
           throw new RuntimeException(e);
       }
    }
}
