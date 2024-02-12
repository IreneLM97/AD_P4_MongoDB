package controller;

import java.io.InputStream;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import mongoDB.MongoDB;
import repositories.products.ProductsRepository;

/**
 * Clase que será el controlador del inventario.
 */
public class MongoController {
	// Inicialización de la conexión a la base de datos y acceso a la colección
	private static MongoClient mongoClient = MongoDB.getClient();
	private static MongoDatabase database = mongoClient.getDatabase("inventorydb");
	static MongoCollection<Document> collection = database.getCollection("products");
	
	// Dependencia del repositorio
	private ProductsRepository pr;
	
	/**
     * Constructor de la clase MongoController.
     *
     * @param pr Repositorio de productos para la interacción con la base de datos.
     */
    public MongoController(ProductsRepository pr) {
        this.pr = pr;
    }

    /**
     * Recupera todos los documentos de la colección.
     *
     * @return Lista de documentos de la colección.
     */
    public List<Document> findAll() {
        return pr.findAll(collection);
    }

    /**
     * Recupera todos los documentos de la colección con proyecciones en los campos especificados.
     *
     * @param fieldsToProject Lista de campos en los que se desea realizar la proyección.
     * @return Lista de documentos de la colección con las proyecciones aplicadas.
     */
    public List<Document> findAllWithProjections(List<String> fieldsToProject) {
        return pr.findAllWithProjections(fieldsToProject, collection);
    }

    /**
     * Recupera documentos de la colección según los criterios de búsqueda y realiza proyecciones en los campos especificados.
     *
     * @param jsonCriteria Criterios de búsqueda en formato JSON.
     * @param fieldsToProject Lista de campos en los que se desea realizar la proyección.
     * @return Lista de documentos de la colección que cumplen con los criterios de búsqueda y las proyecciones aplicadas.
     */
    public List<Document> findByFieldsWithProjection(String jsonCriteria, List<String> fieldsToProject) {
    	return pr.findByFieldsWithProjection(jsonCriteria, fieldsToProject, collection);
    }

    /**
     * Elimina todos los documentos de la colección.
     */
    public void deleteAll() {
    	pr.deleteAll(collection);
    }

    /**
     * Inserta un documento en la colección a partir de un JSON.
     *
     * @param json Documento en formato JSON a insertar en la colección.
     */
    public void insertOne(String json) {
    	pr.insertOne(json, collection);
    }

    /**
     * Elimina un documento de la colección por su ID.
     *
     * @param id ID del documento a eliminar.
     */
    public void deleteOneById(String id) {
    	pr.deleteOneById(id, collection);
    }

    /**
     * Elimina múltiples documentos de la colección según los criterios de búsqueda.
     *
     * @param jsonCriteria Criterios de búsqueda en formato JSON.
     */
    public void deleteManyByCriteria(String jsonCriteria) {
    	pr.deleteManyByCriteria(jsonCriteria, collection);
    }

    /**
     * Reemplaza un documento de la colección por su ID.
     *
     * @param id ID del documento a reemplazar.
     * @param jsonUpdate Documento en formato JSON que reemplazará al documento existente.
     */
    public void replaceOneById(String id, String jsonUpdate) {
    	pr.replaceOneById(id, jsonUpdate, collection);
    }

    /**
     * Inserta datos JSON desde un flujo de entrada en la colección.
     *
     * @param ruta Flujo de entrada que contiene los datos JSON a insertar en la colección.
     */
    public void insertJsonData(InputStream ruta) {
    	pr.insertJsonData(ruta, collection);
    }
}
