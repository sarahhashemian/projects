package gitlet;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * A class for Gitlet, the tiny stupid version-control system.
 *
 * @author Sarah Hashemian
 */

public class Commit implements Serializable {

    /** Represents a tree of blobs of the commit. */
    protected TreeMap<String, String> _blobs;


}





