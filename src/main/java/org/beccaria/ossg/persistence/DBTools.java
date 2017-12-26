package org.beccaria.ossg.persistence;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class DBTools {
    final static Logger logger = Logger.getLogger("org.ossg");

    public static boolean save(String collectionName, Document doc) {
        if (doc == null){
            return false;
        }
        if(docAlreadyExists(collectionName, doc)){
            Document filter = new Document("id", doc.getString("id"));
            return update(collectionName,filter,doc);
        }
        try {
            MongoCollection<Document> collection = DBConfiguration.getInstance().getDatabase().getCollection(collectionName);
            collection.insertOne(doc);
            logger.info("Saved new user: " + doc.toJson());
            return true;
        } catch (Throwable throwable) {
            StringWriter trace = new StringWriter();
            throwable.printStackTrace(new PrintWriter(trace, true));
            logger.warning(trace.toString());
        }
        return false;
    }

    private static boolean docAlreadyExists(String collectionName, Document doc) {
        Document document = getById(collectionName,doc.getString("id"));
        return (document != null);
    }

    public static boolean update(String collectionName, Document filter, Document set) {

        long count = DBConfiguration.getInstance()
                .getDatabase()
                .getCollection(collectionName)
                .updateOne(filter,set)
                .getModifiedCount();
        if (count == 1){
            return true;
        }
        return false;
    }

    public static boolean delete(String collectionName, String id) {
        Document filter = new Document("id", id);
        long count = DBConfiguration.getInstance().getDatabase().getCollection(collectionName).deleteOne(filter).getDeletedCount();
        if (count == 1){
            return true;
        }
        return false;
    }

    public static boolean updateField(String collectionName, String id, String fieldName, String fieldValue) {

        Document filter = new Document("id", id);
        Document newValue = new Document(fieldName,fieldValue);
        Document set = new Document("$set", newValue);

        return update(collectionName, filter, set);
    }

    public static Document getById(String collectionName, String id) {
        return DBConfiguration.getInstance().getDatabase().getCollection(collectionName).find(eq("id", id)).first();
    }

    public static Document getByField(String collectionName, String fieldName, String value) {
        return DBConfiguration.getInstance().getDatabase().getCollection(collectionName).find(eq(fieldName, value)).first();
    }

    public static Iterator<Document> search(String collectionName, String field, String text) {
        logger.info("[searching] collection: " + collectionName + ", field: " + field + ", text: " + text);
        String filterString = "/" + text + "/";
        Document filter = new Document(field, Pattern.compile(text, Pattern.CASE_INSENSITIVE)); //"name",  java.util.regex.Pattern.compile(m)
        return DBConfiguration.getInstance().getDatabase().getCollection(collectionName).find(filter).iterator();
    }

    public static Iterator<Document> searchByFilter(String collectionName, Document filter) {
        return DBConfiguration.getInstance().getDatabase().getCollection(collectionName).find(filter).iterator();
    }
}
