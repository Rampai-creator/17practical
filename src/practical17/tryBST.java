public class tryBST {

  
    //tNode: represents a single node in the binary search tree.
    //Each node stores an integer key, and has a left and right child.
  
    static class tNode {
        int key; //the integer value stored in this node
        tNode left; //left child (values smaller than key)
        tNode right; //right child (values larger than key)

        //Constructor: create a new node with a given key, no children yet
        tNode(int key) {
            this.key = key;
            this.left = null;
            this.right = null;
        }
    }

    
    //BST class: an object-oriented binary search tree.
    //The tree is accessed through its root node.
    
    static class BST {
        tNode root; //the top of the tree (null if tree is empty)

        //Constructor: start with an empty tree
        BST() {
            root = null;
        }

       
        //insert: adds a new integer key into the BST.
        //Follows the BST rule: smaller keys go left, larger go right.
        //Duplicate keys are ignored.
        
        void insert(int key) {
            root = insertRec(root, key);
        }

        //Recursive helper for insert.
        //Returns the (possibly new) root of the subtree.
        private tNode insertRec(tNode node, int key) {
            //Base case: we've reached an empty spot 
            if (node == null) return new tNode(key);

            if (key < node.key)
                //Key is smallermgo left
                node.left = insertRec(node.left, key);
            else if (key > node.key)
                // Key is larger go right
                node.right = insertRec(node.right, key);
            //If key == node.key, it's a duplicate, do nothing

            return node;
        }

        
        //delete: removes a node with the given key from the BST.
        //Handles three cases:
        //1. Node has no children just remove it
        //2. Node has one child replace node with that child
        //3. Node has two children replace with in-order successor
        //(smallest node in right subtree), then delete that successor
       
        void delete(int key) {
            root = deleteRec(root, key);
        }

        //Recursive helper for delete.
        private tNode deleteRec(tNode node, int key) {
            //Base case: key not found in tree
            if (node == null) return null;

            if (key < node.key) {
                //Key is in the left subtree
                node.left = deleteRec(node.left, key);
            } else if (key > node.key) {
                //Key is in the right subtree
                node.right = deleteRec(node.right, key);
            } else {
                //Found the node to delete!

                //Case 1 & 2: zero or one child
                if (node.left == null) return node.right;
                if (node.right == null) return node.left;

                //Case 3: two children
                //Find in-order successor: smallest node in right subtree
                tNode successor = findMin(node.right);
                //Replace current node's key with successor's key
                node.key = successor.key;
                //Delete the successor from the right subtree
                node.right = deleteRec(node.right, successor.key);
            }
            return node;
        }

        //findMin: returns the node with the smallest key in a subtree.
        //Used by delete to find the in order successor.
        private tNode findMin(tNode node) {
            //Keep going left until there is no left child
            while (node.left != null) node = node.left;
            return node;
        }

      
      
        //deleteAllEvens: removes every node whose key is even.
        //We use a postorder traversal so children are handled before
        //their parents this avoids missing nodes during deletion.
       
        void deleteAllEvens() {
            root = deleteEvensRec(root);
        }

        //Recursive helper: visits left subtree, right subtree, then
        //checks the current node. Returns the cleaned subtree root.
        private tNode deleteEvensRec(tNode node) {
            if (node == null) return null;

            //First clean the children
            node.left = deleteEvensRec(node.left);
            node.right = deleteEvensRec(node.right);

            //Then check this node
            if (node.key % 2 == 0) {
                //This node is even delete it using the same BST delete logic
                //(reuse deleteRec so we handle all three cases correctly)
                return deleteRec(node, node.key);
            }
            return node;
        }
       
        //isBST: checks whether the tree satisfies BST properties.
        //Every node must have:
        //   all left descendants strictly less than node.key
        //   all right descendants strictly greater than node.key
        //We pass min/max bounds down the tree to enforce this globally.
        
        boolean isBST() {
            return isBSTRec(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        //Recursive helper: checks BST property with valid range [min, max]
        private boolean isBSTRec(tNode node, int min, int max) {
            if (node == null) return true; //empty tree/subtree is valid

            //Current node's key must be strictly within (min, max)
            if (node.key <= min || node.key >= max) return false;

            //Left subtree: all keys must be < node.key (max = node.key)
            //Right subtree: all keys must be > node.key (min = node.key)
            return isBSTRec(node.left, min, node.key)
                && isBSTRec(node.right, node.key, max);
        }

       
        //countNodes: counts how many nodes are in the tree.
        //Useful for verifying the tree size after deletion.
        
        int countNodes() {
            return countRec(root);
        }

        private int countRec(tNode node) {
            if (node == null) return 0;
            return 1 + countRec(node.left) + countRec(node.right);
        }

        
        //clear: empties the entire tree (sets root to null).
        //Used between timing repetitions to start fresh each time.
      
        void clear() {
            root = null;
        }

        
        //printInOrder: prints all keys in sorted order (left-root-right).
        //Used for small trees (n <= 4) to visually verify correctness.
        
        void printInOrder() {
            printInOrderRec(root);
            System.out.println();
        }

        private void printInOrderRec(tNode node) {
            if (node == null) return;
            printInOrderRec(node.left);
            System.out.print(node.key + " ");
            printInOrderRec(node.right);
        }
    }
   
    //populateTree: inserts integers [1 .. 2^n - 1] into the BST
    //in BREADTH-FIRST order so the result is a perfectly balanced BST
    //Strategy:
    //   Find the middle of the current range → insert it.
    //   Recurse on the left half [low .. middle-1]
    //   Recurse on the right half [middle+1 .. high]
    
    //balanced BST when the total count is exactly 2^n - 1.

    static void populateTree(BST tree, int low, int high) {
        if (low > high) return; //base case: empty range

        int middle = (low + high) / 2; //midpoint becomes the root of this subtree
        tree.insert(middle); //insert it first (breadth first order)

        //Recurse on left and right halves
        populateTree(tree, low, middle - 1);
        populateTree(tree, middle + 1, high);
    }

    
    // Main: small correctness test, then large timing runs.
  
    public static void main(String[] args) {

        //SMALL TEST: n = 4 (tree has 2^4 - 1 = 15 nodes)
        
        System.out.println(" SMALL TEST n = 4 (15 nodes)");
        

        BST small = new BST();
        populateTree(small, 1, 15);

        System.out.println("Is BST? " + small.isBST());
        System.out.println("Node count: " + small.countNodes());
        System.out.print("In-order: ");
        small.printInOrder(); //should print 1 2 3 ... 15

        small.deleteAllEvens();
        System.out.println("After deleting evens:");
        System.out.println("Is BST? " + small.isBST());
        System.out.println("Node count: " + small.countNodes()); // should be 8
        System.out.print("In-order: ");
        small.printInOrder(); //should print only odd numbers: 1 3 5 7 9 11 13 15

        //MEDIUM TEST: n = 7 (tree has 2^7 - 1 = 127 nodes)
        System.out.println();
        
        System.out.println(" MEDIUM TEST n = 7 (127 nodes)");
     

        BST medium = new BST();
        populateTree(medium, 1, 127);
        System.out.println("Is BST? " + medium.isBST());
        System.out.println("Node count: " + medium.countNodes());
        medium.deleteAllEvens();
        System.out.println("After deleting evens:");
        System.out.println("Is BST? " + medium.isBST());
        System.out.println("Node count: " + medium.countNodes());

        //LARGE TIMING TEST: n = 20 (2^20 - 1 = 1,048,575 nodes)
        //The practical sheet says timings must exceed 1000ms.
        // n=20 gives over 1 million nodes which achieves this.
        System.out.println();
        System.out.println(" LARGE TIMING TEST n = 20");
        System.out.println(" (2^20 - 1 = 1,048,575 nodes)");
      

        int n = 20;
        int total = (1 << n) - 1; //2^n - 1 using bit shift 
        int REPS = 30; //30 repetitions

        //Arrays to store each run's timing (in milliseconds)
        double[] populateTimes = new double[REPS];
        double[] deleteTimes = new double[REPS];

        BST tree = new BST();

        System.out.println("Running " + REPS + " repetitions — please wait...");
        System.out.println();

        for (int r = 0; r < REPS; r++) {
            tree.clear(); //start with an empty tree each repetition

            //Time the populate step
            long start = System.nanoTime();
            populateTree(tree, 1, total);
            populateTimes[r] = (System.nanoTime() - start) / 1_000_000.0; //ms

            //Time the delete-evens step
            start = System.nanoTime();
            tree.deleteAllEvens();
            deleteTimes[r] = (System.nanoTime() - start) / 1_000_000.0; //ms
        }

  
  


