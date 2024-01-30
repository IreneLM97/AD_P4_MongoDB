package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;

public class Utils {
	public static String pretty(String json) {
		JsonElement je = JsonParser.parseString(json);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(je);
	}
	
	public static String formatJson(String jsonString) {
        // Parsear el JSON
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // Obtener las claves
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

        // Obtener el ancho de cada columna
        int[] columnWidths = getColumnWidths(entries, jsonObject);

        // Construir la tabla formateada
        StringBuilder formattedTable = new StringBuilder();

        // Construir las cabeceras
        formattedTable.append(StringUtils.repeat("-", getTotalWidth(columnWidths))).append("\n");
        formattedTable.append("| ").append(StringUtils.center("KEY", columnWidths[0])).append(" | ")
                .append(StringUtils.center("VALUE", columnWidths[1])).append(" |\n");
        formattedTable.append(StringUtils.repeat("-", getTotalWidth(columnWidths))).append("\n");

        // Construir las filas con los datos
        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            if (key.equals("_id")) {
                continue; // Ignorar el primer registro si es "_id"
            }
            JsonElement value = entry.getValue();
            String valueString = value.isJsonPrimitive() ? value.getAsString() : ""; // Manejar valores no primitivos
            formattedTable.append("| ").append(StringUtils.rightPad(key, columnWidths[0]))
                    .append(" | ").append(StringUtils.rightPad(valueString, columnWidths[1])).append(" |\n");
        }

        // Agregar línea final
        formattedTable.append(StringUtils.repeat("-", getTotalWidth(columnWidths)));

        return formattedTable.toString();
    }

    private static int[] getColumnWidths(Set<Map.Entry<String, JsonElement>> entries, JsonObject jsonObject) {
        int[] widths = new int[2];
        widths[0] = "KEY".length();
        widths[1] = "VALUE".length();

        for (Map.Entry<String, JsonElement> entry : entries) {
            String key = entry.getKey();
            if (key.equals("_id")) {
                continue; // Ignorar el primer registro si es "_id"
            }
            int keyLength = key.length();
            JsonElement value = entry.getValue();
            int valueLength = value.isJsonPrimitive() ? value.getAsString().length() : 0; // Manejar valores no primitivos
            if (keyLength > widths[0]) {
                widths[0] = keyLength;
            }
            if (valueLength > widths[1]) {
                widths[1] = valueLength;
            }
        }

        return widths;
    }

    private static int getTotalWidth(int[] columnWidths) {
        return columnWidths[0] + columnWidths[1] + 7;
    }
	

}

//METODO NO DINAMICO PARA MOSTRAR LOS DATOS
//	public static String formatJson(String jsonString) {
//        // Parsear el JSON
//        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
//
//        // Obtener las claves
//        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
//
//        // Construir la tabla formateada
//        StringBuilder formattedTable = new StringBuilder();
//
//        // Construir las cabeceras y la línea separadora
//        formattedTable.append("------------------------------------------------------------------------------------").append("\n");
//        formattedTable.append(String.format("%-5s %-35s %-5s %-35s |\n", "|", "CLAVE", "|", "VALOR"));
//        formattedTable.append("------------------------------------------------------------------------------------").append("\n");
//
//        // Construir las filas con los datos
//        for (Map.Entry<String, JsonElement> entry : entries) {
//            String key = entry.getKey();
//            if (key.equals("_id")) {
//                continue; // Ignorar el primer registro si es "_id"
//            }
//            JsonElement value = entry.getValue();
//            String valueString = value.isJsonPrimitive() ? value.getAsString() : ""; // Manejar valores no primitivos
//
//            formattedTable.append(String.format("%-5s %-35s %-5s %-35s |\n", "|", key, "|", valueString));
//        }
//        // Agregar línea final
//        formattedTable.append("-------------------------------------------------------------------------------------").append("\n");
//        return formattedTable.toString();
//    }