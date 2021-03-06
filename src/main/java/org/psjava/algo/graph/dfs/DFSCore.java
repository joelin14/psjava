package org.psjava.algo.graph.dfs;

import java.util.Iterator;

import org.psjava.ds.graph.DirectedEdge;
import org.psjava.ds.graph.AdjacencyListOfDirectedGraph;
import org.psjava.ds.graph.Graph;
import org.psjava.ds.map.MutableMap;
import org.psjava.ds.stack.GoodStackFactory;
import org.psjava.ds.stack.Stack;
import org.psjava.ds.stack.StackFactory;
import org.psjava.goods.GoodMutableMapFactory;

/**
 * Implementation of DFS (Depth First Search)
 */

public class DFSCore {

	private static final StackFactory STACK_FACTORY = GoodStackFactory.getInstance();

	private static class StackItem<V, E> {
		public V v;
		public E e;
		public int depth;
		public Iterator<E> iter;

		public StackItem(V v, E e, int d, Iterator<E> iter) {
			this.v = v;
			this.e = e;
			this.depth = d;
			this.iter = iter;
		}
	}

	public static <V, E extends DirectedEdge<V>> MutableMap<V, DFSStatus> createInitialStatus(Graph<V, E> graph) {
		MutableMap<V, DFSStatus> r = GoodMutableMapFactory.getInstance().create();
		for (V v : graph.getVertices())
			r.put(v, DFSStatus.NOT_DISCOVERED);
		return r;
	}

	public static <V, E extends DirectedEdge<V>> void traverse(AdjacencyListOfDirectedGraph<V, E> adj, MutableMap<V, DFSStatus> status, V start, DFSVisitor<V, E> visitor) {
		Stack<StackItem<V, E>> stack = STACK_FACTORY.create();
		status.put(start, DFSStatus.DISCOVERED);
		visitor.onDiscovered(start, 0);
		Iterator<E> iterator = adj.getEdges(start).iterator();
		stack.push(new StackItem<V, E>(start, null, 0, iterator));

		while (!stack.isEmpty()) {
			StackItem<V, E> item = stack.top();

			if (item.iter.hasNext()) {
				E edge = item.iter.next();
				V nextv = edge.to();
				DFSStatus nextc = status.get(nextv);
				if (nextc == DFSStatus.NOT_DISCOVERED) {
					visitor.onWalkDown(edge);
					status.put(item.v, DFSStatus.DISCOVERED);
					visitor.onDiscovered(nextv, item.depth + 1);
					stack.push(new StackItem<V, E>(nextv, edge, item.depth + 1, adj.getEdges(nextv).iterator()));
				} else if (nextc == DFSStatus.DISCOVERED) {
					visitor.onBackEdgeFound(edge);
				}
			} else {
				stack.pop();
				status.put(item.v, DFSStatus.EXPLORED);
				visitor.onFinish(item.v, item.depth);
				if (item.e != null)
					visitor.onWalkUp(item.e);
			}
		}
	}

}
