package repositories;

import java.io.InputStream;
import java.util.List;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

/**
 * Interfaz que define métodos para interactuar con una colección MongoDB.
 */
public interface MongoRepository {
    /**
     * Recupera todos los documentos de la colección.
     *
     * @param collection Colección MongoDB de la que se desean recuperar los documentos.
     * @return Lista de documentos de la colección.
     */
    List<Document> findAll(MongoCollection<Document> collection);

    /**
     * Recupera todos los documentos de la colección con proyecciones en los campos especificados.
     *
     * @param fieldsToProject Lista de campos en los que se desea realizar la proyección.
     * @param collection Colección MongoDB de la que se desean recuperar los documentos.
     * @return Lista de documentos de la colección con las proyecciones aplicadas.
     */
    List<Document> findAllWithProjections(List<String> fieldsToProject, MongoCollection<Document> collection);

    /**
     * Recupera documentos de la colección según los criterios de búsqueda y realiza proyecciones en los campos especificados.
     *
     * @param jsonCriteria Criterios de búsqueda en formato JSON.
     * @param fieldsToProject Lista de campos en los que se desea realizar la proyección.
     * @param collection Colección MongoDB de la que se desean recuperar los documentos.
     * @return Lista de documentos de la colección que cumplen con los criterios de búsqueda y las proyecciones aplicadas.
     */
    List<Document> findByFieldsWithProjection(String jsonCriteria, List<String> fieldsToProject, MongoCollection<Document> collection);

    /**
     * Elimina todos los documentos de la colección.
     *
     * @param collection Colección MongoDB de la que se desean eliminar los documentos.
     */
    void deleteAll(MongoCollection<Document> collection);

    /**
     * Inserta un documento en la colección a partir de un JSON.
     *
     * @param json Documento en formato JSON a insertar en la colección.
     * @param collection Colección MongoDB en la que se desea insertar el documento.
     */
    void insertOne(String json, MongoCollection<Document> collection);

    /**
     * Elimina un documento de la colección por su ID.
     *
     * @param id ID del documento a eliminar.
     * @param collection Colección MongoDB de la que se desea eliminar el documento.
     */
    void deleteOneById(String id, MongoCollection<Document> collection);

    /**
     * Elimina múltiples documentos de la colección según los criterios de búsqueda.
     *
     * @param jsonCriteria Criterios de búsqueda en formato JSON.
     * @param collection Colección MongoDB de la que se desean eliminar los documentos.
     */
    void deleteManyByCriteria(String jsonCriteria, MongoCollection<Document> collection);

    /**
     * Reemplaza un documento de la colección por su ID.
     *
     * @param id ID del documento a reemplazar.
     * @param jsonUpdate Documento en formato JSON que reemplazará al documento existente.
     * @param collection Colección MongoDB en la que se desea reemplazar el documento.
     */
    void replaceOneById(String id, String jsonUpdate, MongoCollection<Document> collection);

    /**
     * Inserta datos JSON desde un flujo de entrada en la colección.
     *
     * @param ruta Flujo de entrada que contiene los datos JSON a insertar en la colección.
     * @param collection Colección MongoDB en la que se desea insertar los datos.
     */
    void insertJsonData(InputStream ruta, MongoCollection<Document> collection);
}
