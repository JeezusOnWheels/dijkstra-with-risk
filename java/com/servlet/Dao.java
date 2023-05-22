package com.servlet;

import com.entity.Edge;
import com.entity.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Dao {
    private final JdbcTemplate jdbcTemplate;
    private static final Map<Long, Vertex> vertices = new HashMap<>(100000);
    private static List<Edge> edges = new ArrayList<>(100000);
    private Logger logger = LoggerFactory.getLogger(Dao.class);

    public Dao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static Map<Long, Vertex> getVertices() {
        return vertices;
    }

    public static List<Edge> getEdges() {
        return edges;
    }

    /**
     * load vertices and edges from the database.
     *
     * @implNote if the initialisation has been completed before, return immediately.
     */
    public void init() {
        if (vertices.size() != 0) return;
        loadVertices();
        logger.debug("Place size " + vertices.size());
        loadEdge();
        logger.debug("edge num:" + edges.size());
    }

    /**
     * Load vertices from the database and add it into the Map.
     *
     * @implNote the lambda defines how each element is processed in implicit loop.
     * @see <a href= "https://docs.spring.io/spring/docs/3.0.x/reference/jdbc.html"></a></a>
     */
    private void loadVertices() {
        List<Vertex> verticesList = jdbcTemplate.query("select * from \"vertex\" ", (resultSet, i) ->
                new Vertex(resultSet.getLong("id"),
                        resultSet.getDouble("latitude"),
                        resultSet.getDouble("longitude")));
        for (Vertex vertex :
                verticesList) {
            vertices.put(vertex.getId(), vertex);
        }
    }

    /**
     * Load edges from the database and add it into the List. Incident information is added into
     * the corresponding adjacent vertices.
     */
    private void loadEdge() {
        edges = jdbcTemplate.query("select * from \"edge\" ", (resultSet, i) -> {
            double length = resultSet.getDouble("length");
            long startVertexId = resultSet.getLong("startVertex");
            long endVertexId = resultSet.getLong("endVertex");
            double risk = resultSet.getDouble("risk");
            resultSet.getInt("id");

            Vertex startVertex = vertices.get(startVertexId);
            Vertex endVertex = vertices.get(endVertexId);

            Edge edge = new Edge(length, startVertex, endVertex, risk);
            startVertex.addIncidentRoad(edge);
            endVertex.addIncidentRoad(edge);
            return edge;
        });
    }

}
