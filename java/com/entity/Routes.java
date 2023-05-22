package com.entity;

import java.util.ArrayList;

/**
 * An array of routes returned by this server.
 *
 * @author liz
 * @implNote The reason not using plain List is that somebody says Jackson JSON, the JSONify tool Spring use
 * doesn't support a list.
 * @since 25/3/2020
 */
public class Routes extends ArrayList<Route> {

}
