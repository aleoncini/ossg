package org.beccaria.ossg.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.logging.Logger;

@Path("/rs/courses")
public class Courses {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG Course REST service";
    }

}
