package ch.shaped.dcrxml.json.serializer;

import ch.shaped.dcrxml.model.DCRDoc;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by christof on 6/13/15.
 */
public class DCRDocSerializer implements JsonSerializer<DCRDoc> {

    public static final String FIELD_KEYWORDS = "keyword";
    public static final String FIELD_TEXT = "text";
    public static final String FIELD_REFERENCES = "reference";
    public static final String FIELD_METAINFO = "metainfo";


    @Override
    public JsonElement serialize(DCRDoc dcrdoc, Type type, JsonSerializationContext jsonSerializationContext) {

        JsonObject obj = new JsonObject();

        JsonObject metainfo = new JsonObject();
        for (Map.Entry<String, List<String>> mi : dcrdoc.getMetainfo().entrySet()) {
            StringBuffer sb = new StringBuffer();
            for (String s : mi.getValue()) {
                if(sb.length() > 0) {
                    sb.append("\t");
                }
                sb.append(s);
            }
            metainfo.addProperty(mi.getKey(), sb.toString());
        }

        obj.add(FIELD_METAINFO, metainfo);
        obj.add(FIELD_KEYWORDS, jsonSerializationContext.serialize(dcrdoc.getKeywords()));
        obj.add(FIELD_REFERENCES, jsonSerializationContext.serialize(dcrdoc.getReferences()));
        try {
            obj.addProperty(FIELD_TEXT, dcrdoc.getContent().getOriginalContent());
        } catch(InstantiationException e) {
            /* FIXME: log ... do nothing */
        }

        return obj;
    }
}
