package com.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * A route that is returned to client in Routes class.
 *
 * @author liz
 * @implNote The getter and setter is mandatory for JSONifing it. Do NOT delete them.
 * @since 25/3/2020
 */
public class Route {
    public List<Vertex> vertices = new ArrayList<>();
    public double length;
    public double risk;

    public static class Vertex {
        double latitude;
        double longitude;

        @JsonCreator
        public Vertex(@JsonProperty("lat") double latitude, @JsonProperty("lon") double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public void addVertex(double lat, double lon) {
        vertices.add(new Vertex(lat, lon));
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setRisk(double risk) {
        this.risk = risk;
    }
}
