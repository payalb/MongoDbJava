package com.java;

import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

public class MyDemo {
	//MongoClient class represents your Java connection to the MongoDB database.
	static MongoDatabase db = new MongoClient("localhost", 27017).getDatabase("mycustomers");
	static MongoCollection<Document> collection = db.getCollection("customers");

	public static void main(String[] args) {

	//	insertDocument();
	//	fetchDocument();
	//	insertEmbeddedDocument();
	//	fetchAllDocumentsWithName();
	//	fetchNameNotPayal();
	//	printAlCollections();
	//	findDocumentUsingFilterAnd();
		findDocumentUsingGson();
	}

	private static void insertDocument() {
		Document obj = new Document();
		obj.put("name", "Payal");
		obj.put("age", 34);
		collection.insertOne(obj);
	}
	
	private static void insertEmbeddedDocument() {
		Document obj = new Document();
		obj.put("name", "Ritu");
		obj.put("age", 32);
		obj.put("address", new Document("hno", "10r"));
		collection.insertOne(obj);
	}
	
	private static void fetchDocument() {
		Document obj = new Document();
		obj.put("name", "Payal");
		obj.put("age", 34);
		FindIterable<Document> doc=collection.find(obj);
		System.out.println(doc.first());
	}
	
	private static void fetchAllDocumentsWithName() {
		Document obj = new Document();
		obj.put("name", Pattern.compile(Pattern.quote("ayal")));
		FindIterable<Document> doc=collection.find(obj);
		doc.iterator().forEachRemaining(x->System.out.println(x.toJson()));
		System.out.println(doc.first());
		MongoCursor<Document> c=doc.skip(1).limit(2).iterator();
		c.forEachRemaining(System.out::println);
		
	}
	
	private static void fetchNameNotPayal() {
		Document obj = new Document();
		obj.put("name",new Document("$ne", "payal" ));
		FindIterable<Document> doc=collection.find(obj);
		doc.iterator().forEachRemaining(x->System.out.println(x.toJson()));
		
		
	}
	
	private static void changeNameShweta() {
		Document obj = new Document();
		obj.put("_id",new ObjectId("5c17eab8031b2cc49cc55fe9"));
		Document obj1 = new Document();
		obj1.put("$set", new Document("name","shweta"));
		UpdateResult result=collection.updateOne(obj, obj1);
		System.out.println(result.getModifiedCount());
	}
	
	private static void printAlCollections() {
		db.listCollectionNames().iterator().forEachRemaining(System.out::println);
	}
	
	private static void dropCollection() {
		db.getCollection("car").drop();
	}
	

	private static void findDocumentUsingFilterEq() {
		db.getCollection("customers").find(Filters.eq("name", "Payal"))
		.iterator().forEachRemaining(x-> System.out.println(x.toJson()));
	}
	
	private static void findDocumentUsingFilterAnd() {
		db.getCollection("customers").find(Filters.and(Filters.eq("name", "Payal"), Filters.eq("age",34)))
		.iterator().forEachRemaining(x-> System.out.println(x.toJson()));
	}
	
	private static void findDocumentUsingGson() {
		Gson gson=  new Gson();
		Document d=db.getCollection("customers").find(Filters.and(Filters.eq("name", "Payal"), Filters.eq("age",34)))
		.first();
			Customer c=gson.fromJson(d.toJson(), Customer.class);
			System.out.println(c.name);
	}
}
