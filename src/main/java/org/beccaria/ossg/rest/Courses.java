package org.beccaria.ossg.rest;

import org.beccaria.ossg.persistence.CourseHelper;
import org.ossg.model.Course;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Logger;

@Path("/rs/courses")
public class Courses {
    private static final Logger logger = Logger.getLogger("org.ossg");

    @GET
    public String getInfo() {
        return "OSSG Course REST service";
    }

    @GET
    @Produces("application/json")
    @Path("/course/{id}")
    public Response getCourse(@PathParam("id") String id) {
        Course course = new CourseHelper().getById(id);
        if (course == null){
            return Response.status(404).build();
        } else {
            return Response.status(200).entity(course.toString()).build();
        }
    }

    @POST
    @Consumes("application/json")
    @Path("/ads")
    public Response addCourseString(String jsonString){
        Course course = new Course().build(jsonString);
        return this.addCourse(course);
    }

    @POST
    @Consumes("application/json")
    @Path("/add")
    public Response addCourse(Course course){
        if (new CourseHelper().save(course)){
            return Response.status(200).entity("{\"result\":\"success\", \"courseId\":\"" + course.getId() + "\", \"message\":\"OK\"}").build();
        } else {
            return Response.status(200).entity("{\"result\":\"ERROR\", \"courseId\":\"" + course.getId() + "\", \"message\":\"Unable to save course\"}").build();
        }
    }

    @GET
    @Produces("application/json")
    @Path("/search")
    public Response search(@QueryParam("name") String name,
                           @QueryParam("positionX") int px,
                           @QueryParam("positionY") int py) {
        if ((name != null) && (name.length() > 0)){
            Collection<Course> courses = new CourseHelper().searchByName(name);
            return Response.status(200).entity(formatCourses(courses)).build();
        }

        // TO DO
        // implementation of searching by coordinates here

        logger.info("======================> if you see this check that course actually doesn't exist");
        return Response.status(404).build();
    }

    private String formatCourses(Collection<Course> courses) {

        if (courses.isEmpty()){
            return "{\"courses\": [] }";
        }

        logger.info("[Search Course] found " + courses.size() + " courses.");

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
