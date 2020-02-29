package org.beccaria.ossg.rest;

import org.beccaria.ossg.model.Course;
import org.beccaria.ossg.persistence.CourseHelper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Logger;

@Path("/course")
public class CourseResource {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG Course REST service";
    }

    @GET
    @Produces("application/json")
    @Path("{id}")
    public Response getCourse(@PathParam("id") String id) {
        Response.ResponseBuilder responseBuilder;
        Course course = new CourseHelper().getById(id);
        if (course == null){
            responseBuilder = Response.status(404);
        } else {
            responseBuilder = Response.ok(course.toString());
        }
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
    }

    @GET
    @Produces("application/json")
    @Path("/list")
    public Response list() {
        Collection<Course> courses = new CourseHelper().list();
        Response.ResponseBuilder responseBuilder = Response.ok(formatCourseList(courses));
        return responseBuilder.header("Access-Control-Allow-Origin", "http://www.zingarotour.org").build();
    }

    private String formatCourseList(Collection<Course> courses) {
        if (courses.isEmpty()){
            return "{\"courses\": [] }";
        }
        StringBuffer buffer = new StringBuffer("{\"courses\": [ ");
        boolean isFirstElement = true;
        for (Course course: courses) {
            if (isFirstElement){
                isFirstElement = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(course.toString());
        }
        buffer.append(" ] }");
        return buffer.toString();
    }

}