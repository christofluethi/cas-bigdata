package ch.shaped.dcrxml.json.serializer;

import ch.shaped.dcrxml.model.Reference;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by christof on 6/13/15.
 */
public class ReferenceSerializer implements JsonSerializer<Reference> {

    public static final String FIELD_ID = "id";
    public static final String FIELD_SCORE = "score";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_LINKSREF = "linksref";

    public static final String FIELD_TYPE = "type";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_SUBCATEGORY = "subcategory";
    public static final String FIELD_AREA = "area";



    @Override
    public JsonElement serialize(Reference reference, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty(FIELD_ID, reference.getId());
        obj.addProperty(FIELD_SCORE, reference.getScore());
        obj.addProperty(FIELD_NAME, reference.getName());
        obj.addProperty(FIELD_TYPE, reference.getType());
        obj.addProperty(FIELD_LINKSREF, reference.getLinksref());

        obj.addProperty(FIELD_CATEGORY, reference.getCategory());
        obj.addProperty(FIELD_SUBCATEGORY, reference.getSubcategory());
        obj.addProperty(FIELD_AREA, reference.getArea());

        return obj;
    }
}
