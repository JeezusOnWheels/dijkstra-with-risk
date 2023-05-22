package com.servlet;

import com.entity.Routes;
import com.entity.Vertex;
import com.findNode.FindCloseNode;
import com.findNode.RoadMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Spring Controller class.
 *
 * @author liz
 * @see <a href="http://spring.io/guides/gs/rest-service/"> Spring RESTful Web service tutorial</a>
 * @see <a href="https://github.com/GoogleCloudPlatform/java-docs-samples/blob/master/appengine-java11/springboot-helloworld/src/main/java/com/example/appengine/springboot/SpringbootApplication.java"> Sample from Google</a>
 * @since 25/3/2020
 */

//Tell app to use auto-configuration, component scan and be able to define extra configuration on their "application class".
@SpringBootApplication
//This annotation describes an endpoint that should be made available over the web.
@RestController
public class RouteController {
    private JdbcTemplate jdbcTemplate;
    Logger logger = LoggerFactory.getLogger(RouteController.class);

    public final Dao dao;

    public RouteController(Dao dao, JdbcTemplate jdbcTemplate) {
        this.dao = dao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(RouteController.class, args);
    }

    /**
     * Calculate routes based on the origin and destination received.
     *
     * @param originLatitude the latitude of origin, should in singed degrees format.
     * @return It return a array with three routes: the first one is in shortest length, the second is in minimised risk,
     * the third is a trade-off choice between length and risk. A route is represented by all its intermediate vertices.
     * @author liz
     * @since 25/3/2020
     */
    //This annotation specifies that all get requests sent to /route is handled by this function.
    @GetMapping("/route")
    public Routes routeCalculator(
            //RequestParam specifies the para name in the hyperlink.
            @RequestParam(value = "o_lat") double originLatitude,
            @RequestParam(value = "o_lon") double originLongitude,
            @RequestParam(value = "d_lat") double destinationLatitude,
            @RequestParam(value = "d_lon") double destinationLongitude
    ) {
        logger.info("A route request is received, from" + originLatitude + originLongitude + " to " + destinationLatitude + destinationLongitude);
        dao.init();
        Routes routes = new Routes();

        Vertex nodeStart = FindCloseNode.closestNode(originLatitude, originLongitude, Dao.getVertices());
        Vertex nodeEnd = FindCloseNode.closestNode(destinationLatitude, destinationLongitude, Dao.getVertices());
        logger.debug("Closest node found: " + nodeStart.getId() + " " + nodeEnd.getId());

        RoadMap roadMap = new RoadMap(Dao.getEdges(), Dao.getVertices());
        Routes route = roadMap.routeCalculation(nodeStart, nodeEnd);
        return route;

    }

    @RequestMapping("/")
    public @ResponseBody
    String greeting() {
        dao.init();
        return "Peace & Love";
    }

}
