package utils;

import java.util.Objects;

public class JsonStringBuilder {
    private final StringBuilder jsonBuilder;

    public JsonStringBuilder() {
        this.jsonBuilder = new StringBuilder("{");
    }

    public void append(String key, Object value) {
    	try {
        	value = Integer.parseInt(value.toString());
        }catch(Exception e1){
        	try {
        		value = Double.parseDouble(value.toString());
        	}catch(Exception e2) {
        		value = value.toString();
        	}
        }
    	
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

    private void appendString(String key, String value) {
        jsonBuilder.append("\"").append(key).append("\": \"").append(value).append("\", ");
    }

    private void appendInt(String key, int value) {
        jsonBuilder.append("\"").append(key).append("\": ").append(value).append(", ");
    }

    private void appendDouble(String key, double value) {
        jsonBuilder.append("\"").append(key).append("\": ").append(value).append(", ");
    }

    private void appendDefault(String key, Object value) {
        jsonBuilder.append("\"").append(key).append("\": \"").append(Objects.toString(value)).append("\", ");
    }

    public String build() {
        // Eliminar la coma adicional al final y cerrar el JSON
        if (jsonBuilder.length() > 1) { // Verificar si se agregaron campos al JSON
            jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length());
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
    
    public String appendCondition(String key, String operator, Object value) {
        StringBuilder conditionBuilder = new StringBuilder("{ ");
        conditionBuilder.append("\"").append(key).append("\": { \"").append(operator).append("\": ");
        if (value instanceof String) {
            conditionBuilder.append("\"").append(value).append("\"");
        } else {
            conditionBuilder.append(value);
        }
        conditionBuilder.append(" } }");
        return conditionBuilder.toString();
    }
}
