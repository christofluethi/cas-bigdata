package ch.shaped.dcrxml.json.serializer;

import ch.shaped.dcrxml.model.Keyword;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by christof on 6/13/15.
 */
public class KeywordSerializer implements JsonSerializer<Keyword> {

    public static final String FIELD_ID = "id";
    public static final String FIELD_SCORE = "score";
    public static final String FIELD_LANGS = "language";

    @Override
    public JsonElement serialize(Keyword keyword, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty(FIELD_ID, keyword.getId());
        obj.addProperty(FIELD_SCORE, keyword.getScore());

        JsonObject langs = new JsonObject();
        for(Map.Entry<String, String> entry : keyword.getLangs().entrySet()) {
            langs.addProperty(entry.getKey(), entry.getValue());
        }

        obj.add(FIELD_LANGS, langs);

        return obj;
    }
}
