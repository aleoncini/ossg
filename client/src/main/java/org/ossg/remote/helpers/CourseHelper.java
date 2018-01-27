package org.ossg.remote.helpers;

import org.bson.Document;
import org.ossg.model.Course;
import org.ossg.remote.client.Connection;
import org.ossg.remote.client.ConnectionFactory;

import javax.ws.rs.core.Response;
import java.util.logging.Logger;

public class CourseHelper {
    private Logger logger = Logger.getLogger("org.ossg");
    Connection connection;

    public CourseHelper() {
        connection = ConnectionFactory.getConnection();
    }

    public Course saveCourse(Course course){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        Response response = connection.POST("/rs/courses/add", course.toString());
        if (response.getStatus() == 200){
            String entity = response.readEntity(String.class);
            Document document = Document.parse(entity);
            String id = document.getString("id");
            course.setId(id);
            return course;
        }
        return null;
    }

    public Course getCourse(String id){
        if (connection == null){
            throw new IllegalStateException("Connection Factory not initialized. Use ConnectionFactory.getInstance(<Configuration>).");
        }
        Response response = connection.GET("/rs/courses/course/" + id);
        String entity = response.readEntity(String.class);
        Document document = Document.parse(entity);
        return new Course().build(document);
    }
}