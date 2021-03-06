package org.psjava.algo.graph.shortestpath;

import org.psjava.ds.Collection;
import org.psjava.ds.graph.DirectedWeightedEdge;
import org.psjava.ds.graph.Graph;
import org.psjava.ds.graph.MutableDirectedWeightedGraph;
import org.psjava.ds.map.MutableMap;
import org.psjava.goods.GoodMutableMapFactory;
import org.psjava.javautil.ConvertedDataIterable;
import org.psjava.javautil.DataConverter;
import org.psjava.math.ns.AddableNumberSystem;

/**
 * Johnson's algorithm allows negative edge weight. but not negative cycles.
 * 
 * Faster than floyd-warshall's algorithm in sparse graph.
 */

public class Johnson implements AllPairShortestPath {

	private static final Object VIRTUAL_START = new Object();

	private BellmanFord bellmanFord;
	private Dijkstra dijkstra;

	public Johnson(BellmanFord bellmanFord, Dijkstra dijkstra) {
		this.bellmanFord = bellmanFord;
		this.dijkstra = dijkstra;
	}

	private static class ReweightedEdge<V, W, E extends DirectedWeightedEdge<V, W>> implements DirectedWeightedEdge<V, W> {
		private final W weight;
		private final E original;

		ReweightedEdge(W w, E original) {
			this.weight = w;
			this.original = original;
		}

		@Override
		public V from() {
			return original.from();
		}

		@Override
		public V to() {
			return original.to();
		}

		@Override
		public W weight() {
			return weight;
		}

		public E getOriginal() {
			return original;
		}
	}

	@Override
	public <V, W, E extends DirectedWeightedEdge<V, W>> AllPairShortestPathResult<V, W, E> calc(Graph<V, E> graph, AddableNumberSystem<W> ns) {
		Graph<Object, DirectedWeightedEdge<Object, W>> augmented = augment(graph, ns);
		SingleSourceShortestPathResult<Object, W, DirectedWeightedEdge<Object, W>> bellmanFordResult = bellmanFord.calc(augmented, VIRTUAL_START, ns);
		Graph<V, ReweightedEdge<V, W, E>> reweighted = reweight(graph, bellmanFordResult, ns);
		MutableMap<V, SingleSourceShortestPathResult<V, W, ReweightedEdge<V, W, E>>> dijsktraResult = GoodMutableMapFactory.getInstance().create();
		for (V v : graph.getVertices())
			dijsktraResult.put(v, dijkstra.calc(reweighted, v, ns));
		return createUnreweightedResult(bellmanFordResult, dijsktraResult, ns);
	}

	private static <V, W, E extends DirectedWeightedEdge<V, W>> Graph<Object, DirectedWeightedEdge<Object, W>> augment(Graph<V, E> original, final AddableNumberSystem<W> ns) {
		MutableDirectedWeightedGraph<Object, W> res = MutableDirectedWeightedGraph.create();
		res.insertVertex(VIRTUAL_START);
		for (Object v : original.getVertices()) {
			res.insertVertex(v);
			res.addEdge(VIRTUAL_START, v, ns.getZero());
		}
		for (E e : original.getEdges())
			res.addEdge(e.from(), e.to(), e.weight());
		return res;
	}

	// TODO use common code if SuccessiveShortestPathWithPotential can use this.
	private static <V, W, E extends DirectedWeightedEdge<V, W>> Graph<V, ReweightedEdge<V, W, E>> reweight(final Graph<V, E> original, final SingleSourceShortestPathResult<Object, W, DirectedWeightedEdge<Object, W>> bellmanFordResult, final AddableNumberSystem<W> ns) {
		return new Graph<V, ReweightedEdge<V, W, E>>() {
			@Override
			public Collection<V> getVertices() {
				return original.getVertices();
			}

			@Override
			public Iterable<ReweightedEdge<V, W, E>> getEdges() {
				return ConvertedDataIterable.create(original.getEdges(), new DataConverter<E, ReweightedEdge<V, W, E>>() {
					@Override
					public ReweightedEdge<V, W, E> convert(E e) {
						W adjust = ns.subtract(bellmanFordResult.getDistance(e.from()), bellmanFordResult.getDistance(e.to()));
						return new ReweightedEdge<V, W, E>(ns.add(e.weight(), adjust), e);
					}
				});
			}
		};
	}

	private static <V, W, E extends DirectedWeightedEdge<V, W>> AllPairShortestPathResult<V, W, E> createUnreweightedResult(final SingleSourceShortestPathResult<Object, W, DirectedWeightedEdge<Object, W>> bellmanFordResult,
			final MutableMap<V, SingleSourceShortestPathResult<V, W, ReweightedEdge<V, W, E>>> dijkstraResult, final AddableNumberSystem<W> ns) {
		return new AllPairShortestPathResult<V, W, E>() {
			@Override
			public W getDistance(V from, V to) {
				W adjust = ns.subtract(bellmanFordResult.getDistance(to), bellmanFordResult.getDistance(from));
				return ns.add(dijkstraResult.get(from).getDistance(to), adjust);
			}

			@Override
			public Iterable<E> getPath(final V from, V to) {
				return ConvertedDataIterable.create(dijkstraResult.get(from).getPath(to), new DataConverter<ReweightedEdge<V, W, E>, E>() {
					@Override
					public E convert(ReweightedEdge<V, W, E> v) {
						return v.getOriginal();
					}
				});
			}

			@Override
			public boolean isReachable(V from, V to) {
				return dijkstraResult.get(from).isReachable(to);
			}
		};
	}

}
