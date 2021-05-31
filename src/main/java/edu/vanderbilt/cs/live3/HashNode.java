package edu.vanderbilt.cs.live3;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class HashNode {
	// non leaf nodes have hashes with a character pointing to the next character 
	private HashMap<Character, HashNode> nodeHash;
	
	// leaf nodes have hashes with a character (the last character in a geohash string)
	// pointing to a list of entries associated with this geohash string
	private HashMap<Character, ArrayList<LatLon>> listHash;  // change arraylist to hashmap
	//FIXME:
	private HashMap<Character, HashMap<String, LatLon>> listHashMoreEfficient;
	
	private boolean leaf;
	
	private int level; // level in the tree
	
	public HashNode(int level) {
		this.nodeHash = new HashMap<Character, HashNode>();
		this.listHash = new HashMap<Character, ArrayList<LatLon>>(); 
		this.level = level;
	}
	
	public HashNode(String geoHashString, LatLon coords, int level) {
		this.nodeHash = new HashMap<Character, HashNode>();
		this.listHash = new HashMap<Character, ArrayList<LatLon>>();
		this.level = level;
		// use first character of the string argument as the key 
		// first character gets removed for each recursive call until empty
		char key = geoHashString.charAt(0); 
		this.leaf = geoHashString.length() == 1;
		
		
		// Cache all the geohashes with matching prefixes up to this point 
		// in node-local arraylist 
		if (this.listHash.containsKey(key)) {
			ArrayList<LatLon> list = this.listHash.get(key);
			if (!list.contains(coords)) {
				list.add(coords);
			}
		} else {
			// create & put new array list with the geoHashObject 
			// as sole element
			ArrayList<LatLon> list = new ArrayList<LatLon>(
					Arrays.asList(coords)); 
			this.listHash.put(key, list);
		}
		
		if (!this.leaf) {
			// if not a leaf, also point to next node 
			HashNode nextHashNode = new HashNode(
					geoHashString.substring(1), coords, this.level + 1); 
			this.nodeHash.put(key, nextHashNode); 
		}
	} 
	
	/* 
	 * Returns a count of all geohashes stored in subtree where 
	 * current node is root; luckily, any time a geohash is added to tree,
	 * it is added to the listHash of each node that stores a character 
	 * for the geohash, where the character stored on the node is the key
	 * and the value is a list of geohashes associated with matching 
	 * prefixes up to this point (this level of the tree)
	 */
	public int count() {
		int count = 0;
		for (List value: this.listHash.values()) {
			count += value.size();
		}
		return count;
	}
	
	/*
	 * Insert method to insert new geohash strings into the tree.
	 * Method defined on the node because it recursively defines a path 
	 * of pointers from the root node to the leaf node (last character in hash)
	 * Always first invoked on root as root.insertGeoHash(String geoHash);
	 * @param geoHashString
	 */
	
	public void insertGeoHash(String geoHashString, LatLon coords) {
		if (geoHashString.trim().length() > 0){
			// only handle if not empty 
			char key = geoHashString.charAt(0);   
			if (this.listHash.containsKey(key)) {
				ArrayList<LatLon> list = this.listHash.get(key);
				if (!list.contains(coords)) {
					list.add(coords);
				}
			} else {
				ArrayList<LatLon> list = new ArrayList<LatLon>(
						Arrays.asList(coords)); 
				this.listHash.put(key, list);
			}
			if (!this.leaf) {
				// not a leaf, handle node pointers
				if (nodeHash.containsKey(key)){
					// take off the first character and call insertGeoHash on 
					// the node this existing key points at 
					HashNode nextHashNode = nodeHash.get(key);
					nextHashNode.insertGeoHash(geoHashString.substring(1), coords);
				} else {
					// not stored yet, create new node
					HashNode nextHashNode = new HashNode(
							geoHashString.substring(1), coords, this.level + 1);
					nodeHash.put(key, nextHashNode);
				}
			} 
		}
		else {
			System.out.println("Should never reach this.");
		} 
	} 

	/* 
	 * method to remove a geohash from database; only delete items from 
	 * array lists 
	 */
	public boolean removeGeoHash(String geoHashString, LatLon coords) {
		boolean deleted = false;
		if (geoHashString.trim().length() > 0){ 
			char key = geoHashString.charAt(0);  
			ArrayList<LatLon> coordList; 
			if (this.listHash.containsKey(key)) { 
				// will return false if nothing to remove; true if removed
				// will not return but will remove from all node array lists
				// in subtree containing coords (since all nodes in subtree
				// cache coords locally) 
				deleted = this.listHash.get(key).remove(coords); 
				if (this.listHash.get(key).size() == 0) {
					// if nothing else left in this listhash key's list, remove key 
					// for both nodeHash and listHash. There are no more geohashes in 
					// the subtree. 
					this.listHash.remove(key);
					this.nodeHash.remove(key); 
				}
			}
			if (!this.leaf) {
				// not a leaf, handle node pointer
				if (this.nodeHash.containsKey(key)) {
					// Delete the key? NO. 
					// That would remove full subtrees instead of specific items. 
					// get node it points to, call delete on that one (remove first char)
					HashNode nextHashNode = this.nodeHash.get(key);
					nextHashNode.removeGeoHash(geoHashString.substring(1), coords);
				}
			}
		}
		return deleted;
	}
	
	public List<double[]> getAllLatLonsInSubtree(){
		final List<double[]> neighbors = new ArrayList<double[]>(); 
		this.listHash.forEach((character, list) -> {
			list.forEach((latLonObject)->{
				neighbors.add(new double[] {
						latLonObject.getLat(), 
						latLonObject.getLon()});
			});
		});
		return neighbors;
	}
	
	public List<double[]> removeAllMatchingUpToNChars(String geoHash, int upTo) {
		if (upTo == 0) {
			// delete the subtree starting from this root.
			this.nodeHash = new HashMap<Character, HashNode>();
			
			List<double[]> deletedCoords = this.getAllLatLonsInSubtree();
			// set listHash to null.
			// Now subtree below this node is gone, but node remains.
			this.listHash = new HashMap<Character, ArrayList<LatLon>>();
			return deletedCoords;
		} else {
			if (geoHash.trim().length() > 0) {
				char key = geoHash.charAt(0);
				if (!this.leaf && this.nodeHash.containsKey(key)){
					// get the next HashNode down pointed at by key  
					HashNode nextHashNode = this.nodeHash.get(key);
					// call method on next one down, decrement upTo, remove first char
					return nextHashNode.removeAllMatchingUpToNChars(
							geoHash.substring(1), upTo - 1);
				} else {
					/* either this is a leaf (and nothing was long enough 
					 * to match geoHash up to upTo characters) OR key does not 
					 * exist and nothing matches geoHash up to upTo characters
					 */
					return new ArrayList<double[]>();
				}
			} else {
				// nothing deleted, geoHash was length 0 and upTo was not
				System.out.println("Cannot delete hashes that match " + 
				"empty geoHash '' up to " + upTo + " characters");
				return new ArrayList<double[]>();
			}
		}
	}
	
	public boolean containsUpToNChars(String geoHash, LatLon coords, int upTo) {
		if (upTo == 0) {
			System.out.println("Level: " + this.level + ", upTo=0");
			return true;
		} else {
			System.out.println("Level->" + this.level);
			if (geoHash.trim().length() > 0 && geoHash.length() >= upTo) {
				char key = geoHash.charAt(0); 
				if (!this.leaf && this.nodeHash.containsKey(key)) {
					HashNode nextHashNode = this.nodeHash.get(key);
					System.out.println("Not a leaf, key " + key + " in nodeHash, recursing.");
					 
					return nextHashNode.containsUpToNChars(
							geoHash.substring(1), coords, upTo - 1
							); 
				} 
				if (this.leaf && this.listHash.containsKey(key)) {
					System.out.println("Leaf's listHash contains key " + key + "!");
					return true;
					/* FIXME: THIS IS FOR CHECKING IF SPECIFIC GEOHASHES 
					 * ARE IN TREE; WRITE ANOTHER METHOD FOR THIS. For
					 * contains up to N chars, return true in this if body.
					 
					if (this.listHash.get(key).contains(coords)) {
						System.out.println("Coords " + coords.toString() + 
								" are in listHash!");
						return true;
					} else {
						System.out.println("Coords " + coords.toString() +
								" not in listHash :(");
						return false;
					}  */
				} else {	
					System.out.println("Key " + key + " not found :(");
					return false;
				} 
					
			} else {
				// nothing deleted, geoHash was length 0 and upTo was not
				// System.out.println("Cannot find hashes that match " + 
				// "empty geoHash '' up to " + upTo + " characters");
				return false; 
			}
		}
	}
	
	public List<double[]> neighborsUpToNChars(String geoHash, int upTo){
		if (upTo == 0) {
			return this.getAllLatLonsInSubtree();
		} else {
			// System.out.println("Level->" + this.level);
			if (geoHash.trim().length() > 0 && geoHash.length() >= upTo) {
				char key = geoHash.charAt(0); 
				if (!this.leaf && this.nodeHash.containsKey(key)) {
					HashNode nextHashNode = this.nodeHash.get(key);
					// System.out.println("Not a leaf, key " + key + " in nodeHash, recursing.");
					return nextHashNode.neighborsUpToNChars(
							geoHash.substring(1), upTo - 1
							);
				} 
				if (this.leaf && this.listHash.containsKey(key)) {
					return this.getAllLatLonsInSubtree();
				} else {	
					// System.out.println("Key " + key + " not found :(");
					return new ArrayList<double[]>();
				} 
					
			} else {
				// nothing deleted, geoHash was length 0 and upTo was not
				// System.out.println("Cannot find hashes that match " + 
				// "empty geoHash '' up to " + upTo + " characters");
				return new ArrayList<double[]>();
			}
		}
	}
	
	
	public String latLonListToString(ArrayList<LatLon> list) {
		final List<String> output = new ArrayList<String>();
		for (LatLon item : list) {
			output.add(item.toString() + " ");
		}
		String out = "";
		for (String s : output) {
			out += s;
		}
		return out;
	}
	
	public String toString() {
		final List<String> output = new ArrayList<String>();
		output.add("(Leaf ? " + leaf + ", Level-> " + this.level + ") "); 
		if (this.listHash != null) {
			this.listHash.forEach((key, list) -> {
				output.add("Key: " + key + "->List: " + latLonListToString(list));
			});  
		} else {
			output.add("List Hash->Null");
		} 
		if (!this.leaf) { 
			if (this.nodeHash != null) {
				this.nodeHash.forEach((key, node) -> {
					output.add("Key: " + key + "->Node: \n" + node.toString());
				}); 
			} else {
				output.add("Node Hash->Null");
			}
			
		} 
		String out = ""; 
		for (String s : output) {
			out += s;
		}
		return out;
		
	}
	
	public static void main(String[] args) {
		
	}
}
