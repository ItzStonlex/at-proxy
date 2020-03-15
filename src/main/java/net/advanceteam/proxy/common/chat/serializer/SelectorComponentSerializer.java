package net.advanceteam.proxy.common.chat.serializer;

import com.google.gson.*;
import net.advanceteam.proxy.common.chat.component.SelectorComponent;

import java.lang.reflect.Type;

public class SelectorComponentSerializer extends BaseComponentSerializer implements JsonSerializer<SelectorComponent>, JsonDeserializer<SelectorComponent>
{

    @Override
    public SelectorComponent deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject object = element.getAsJsonObject();
        SelectorComponent component = new SelectorComponent( object.get( "selector" ).getAsString() );
        deserialize( object, component, context );
        return component;
    }

    @Override
    public JsonElement serialize(SelectorComponent component, Type type, JsonSerializationContext context)
    {
        JsonObject object = new JsonObject();
        serialize( object, component, context );
        object.addProperty( "selector", component.getSelector() );
        return object;
    }
}
