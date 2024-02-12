package utils;

import java.util.Objects;

/**
 * Clase que permite construir y manipular cadenas JSON.
 */
public class JsonStringBuilder {
	/**
	 * Constructor de la cadena JSON.
	 */
    private final StringBuilder jsonBuilder;

    /**
     * Constructor que inicializa el constructor de la cadena JSON.
     */
    public JsonStringBuilder() {
        this.jsonBuilder = new StringBuilder("{");
    }

    /**
     * Añade una clave y su valor al objeto JSON. 
     * El valor puede ser de tipo String, Integer, Double o cualquier otro objeto.
     *
     * @param key Clave del objeto JSON.
     * @param value Valor asociado a la clave.
     */
    public void append(String key, Object value) {
    	// Convertir el valor a tipos específicos (Integer, Double o String)
    	try {
        	value = Integer.parseInt(value.toString());
        }catch(Exception e1){
        	try {
        		value = Double.parseDouble(value.toString());
        	}catch(Exception e2) {
        		value = value.toString();
        	}
        }
    	
    	// Agregar la clave y el valor al constructor de la cadena JSON
        if (value != null) {
            if (value instanceof String) {
                appendString(key, (String) value);
            } else if (value instanceof Integer) {
                appendInt(key, (Integer) value);
            } else if (value instanceof Double) {
                appendDouble(key, (Double) value);
            } else {
                appendDefault(key, value);
            }
        }
    }

    /**
     * Construye la cadena JSON final.
     *
     * @return Cadena JSON construida.
     */
    public String build() {
        // Eliminar la coma adicional al final y cerrar el JSON
        if (jsonBuilder.length() > 1) { // Verificar si se agregaron campos al JSON
            jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length());
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
    
    /**
     * Añade una condición con una clave, operador y valor al objeto JSON.
     *
     * @param key Clave del objeto JSON.
     * @param operator Operador de comparación.
     * @param value Valor asociado a la clave.
     * @return Cadena JSON con la condición añadida.
     */
    public String appendCondition(String key, String operator, Object value) {
    	// Iniciar el constructor de la condición
        StringBuilder conditionBuilder = new StringBuilder("{ ");
        conditionBuilder.append("\"").append(key).append("\": { \"").append(operator).append("\": ");
        // Si el valor es de tipo String necesitamos envolverlo entre ""
        if (value instanceof String) {
            conditionBuilder.append("\"").append(value).append("\"");
        // Si el valor no es de tipo String lo añadimos directamente
        } else {
            conditionBuilder.append(value);
        }
        // Cerramos y devolvemos la condición
        conditionBuilder.append(" } }");
        return conditionBuilder.toString();
    }
    
    /**
     * Agrega una clave y un valor de tipo String al objeto JSON en construcción.
     *
     * @param key Clave del objeto JSON.
     * @param value Valor de tipo String asociado a la clave.
     */
    private void appendString(String key, String value) {
        jsonBuilder.append("\"").append(key).append("\": \"").append(value).append("\", ");
    }
    
    /**
     * Agrega una clave y un valor de tipo int al objeto JSON en construcción.
     *
     * @param key Clave del objeto JSON.
     * @param value Valor de tipo int asociado a la clave.
     */
    private void appendInt(String key, int value) {
        jsonBuilder.append("\"").append(key).append("\": ").append(value).append(", ");
    }

    /**
     * Agrega una clave y un valor de tipo double al objeto JSON en construcción.
     *
     * @param key Clave del objeto JSON.
     * @param value Valor de tipo double asociado a la clave.
     */
    private void appendDouble(String key, double value) {
        jsonBuilder.append("\"").append(key).append("\": ").append(value).append(", ");
    }

    /**
     * Agrega una clave y un valor al objeto JSON en construcción.
     *
     * @param key Clave del objeto JSON.
     * @param value Valor asociado a la clave.
     */
    private void appendDefault(String key, Object value) {
        jsonBuilder.append("\"").append(key).append("\": \"").append(Objects.toString(value)).append("\", ");
    }
}
