package ch.shaped.dcrxml.json;

import ch.shaped.dcrxml.json.serializer.DCRDocSerializer;
import ch.shaped.dcrxml.json.serializer.KeywordSerializer;
import ch.shaped.dcrxml.json.serializer.ReferenceSerializer;
import ch.shaped.dcrxml.model.DCRDoc;
import ch.shaped.dcrxml.model.Keyword;
import ch.shaped.dcrxml.model.Reference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by christof on 6/13/15.
 */
public class DCRDocJsonSerializer {

    public String serialize(DCRDoc d) {
        Gson gson = DCRDocJsonSerializer.getGsonBuilder();
        String result = gson.toJson(d);

        return result;
    }

    public static final String DEFAULT_JSON_RETURN_VALUE = "[]";

    private static Gson getGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DCRDoc.class, new DCRDocSerializer());
        builder.registerTypeAdapter(Keyword.class, new KeywordSerializer());
        builder.registerTypeAdapter(Reference.class, new ReferenceSerializer());
       // builder.registerTypeAdapter(TextContent.class, new TextContentSerializer());
        builder.setPrettyPrinting();
        builder.serializeNulls();
        return builder.create();
    }
}
