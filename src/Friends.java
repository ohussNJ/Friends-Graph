package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		int current=g.map.get(p1);
		boolean[] v=new boolean[g.members.length];
		ArrayList<String> tempList=new ArrayList<>();
		ArrayList<String> list;
		Queue<ArrayList<String>> path=new Queue<>();
		Queue<Person> q=new Queue<>();
		q.enqueue(g.members[current]);
		boolean empty=q.isEmpty();
		tempList.add(g.members[current].name);
		path.enqueue(tempList);
		while(empty==false) {
			if(q.isEmpty()==true)
				break;
			Person student=q.dequeue();
			int index=g.map.get(student.name);
			v[index]=true;
			list=path.dequeue();
			for(Friend temp=g.members[index].first; temp!=null; temp=temp.next) {
				if(v[temp.fnum]==false) {
					ArrayList<String> result=new ArrayList<>(list);
					String studentName=g.members[temp.fnum].name;
					result.add(studentName);
					if(p2.equals(studentName)==false){
						q.enqueue(g.members[temp.fnum]);
						path.enqueue(result);
					}
					else
						return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		ArrayList<ArrayList<String>> cliques=new ArrayList<ArrayList<String>>();
		boolean[] s=new boolean[g.members.length];
		Queue<Integer> q=new Queue<Integer>();
		Person[] name=g.members;
		for (Person member : name){
			ArrayList<String> temp=new ArrayList<String>();
			int i=g.map.get(member.name);
			if (s[i]==false && school.equals(member.school) && member.school!=null){
				s[i]=true;
				q.enqueue(i);
				temp.add(member.name);
				while (q.isEmpty()==false){
					for (Friend nbr=g.members[q.dequeue()].first; nbr!=null; nbr=nbr.next){
						Person ptr=g.members[nbr.fnum];
						if (s[nbr.fnum]==false && school.equals(ptr.school) && ptr.school!=null){
							s[nbr.fnum]=true;
							q.enqueue(nbr.fnum);
							temp.add(g.members[nbr.fnum].name);
						}

					}
				}
				cliques.add(temp);
			}	
		}
		return cliques;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> f=new ArrayList<>();
		boolean[] v=new boolean[g.members.length];
		int[] s=new int[g.members.length];
		int[] back=new int[g.members.length];
		boolean[] b=new boolean[g.members.length];
		for(int i=0; i<g.members.length; i++) {
			if(v[i]==false)
				connectorsDFS(g, i, v, s, back, i, f, b);
			else
				continue;
		}
		return f;
		
	}
	private static void connectorsDFS(Graph g, int v, boolean[] visited, int[] dfsnum, int back[],  int i, ArrayList<String> connectors, boolean[] nbr){
		
		visited[v]=true;
		dfsnum[v]=dfsnum[i] + 1;
		back[v]=dfsnum[v];
		for(Friend t=g.members[v].first; t!=null; t=t.next) {
			int w=t.fnum;
			if(visited[w]==false){
				connectorsDFS(g, t.fnum, visited, dfsnum, back, v, connectors, nbr);
				if((dfsnum[v]<=back[w]) && (connectors.contains(g.members[v].name)==false) && (v!=i || nbr[v])) {
					connectors.add(g.members[v].name);
				}
				if(dfsnum[v]>back[w]) {
					back[v]=Math.min(back[v], back[w]);
				} else {
					nbr[v]=true;
				}
			} else {
				back[v]=Math.min(back[v], dfsnum[w]);
			}	
		}	
	}
}

