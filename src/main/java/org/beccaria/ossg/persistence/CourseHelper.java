package org.beccaria.ossg.persistence;

import org.beccaria.ossg.model.Course;
import org.beccaria.ossg.model.Player;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class CourseHelper {
    public static String COLLECTION_NAME = "courses";

    public boolean save(Course course){
        if (course.getId() == null || course.getId().length() == 0){
            course.setId(UUID.randomUUID().toString().replace("-", ""));
        }
        return DBTools.save(COLLECTION_NAME, course.getDocument());
    }

    public Course getById(String id){
        Document courseDocument = DBTools.getById(COLLECTION_NAME,id);
        if (courseDocument == null){
            return null;
        }
        return new Course().build(courseDocument);
    }

    public Collection<Course> searchByName(String name){
        Collection<Course> courses = new ArrayList<Course>();
        Iterator<Document> docs = DBTools.search(COLLECTION_NAME,"name",name);
        while (docs.hasNext()){
            courses.add(new Course().build(docs.next()));
        }
        return courses;
    }

    public Collection<Course> list(){
        Collection<Course> courses = new ArrayList<Course>();
        Iterator<Document> docs = DBTools.getAll(COLLECTION_NAME);
        while (docs.hasNext()){
            courses.add(new Course().build(docs.next()));
        }
        return courses;
    }

}
