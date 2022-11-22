package kulej.mainpackage;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Path {

    public LinkedList<Integer> nodeLeft;
    public LinkedHashSet<Integer> currentPath;

    public LinkedHashSet<Integer> getCurrentPath() {
        return currentPath;
    }

}
