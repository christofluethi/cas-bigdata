package ch.shaped.dcrxml.json.serializer;

import ch.shaped.dcrxml.model.TextContent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by christof on 6/13/15.
 */
public class TextContentSerializer implements JsonSerializer<TextContent> {

    public static final String FIELD_TEXT = "text";

    @Override
    public JsonElement serialize(TextContent textContent, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty(FIELD_TEXT, textContent.getPlainText());

        return obj;
    }
}
