package org.example;

import io.avaje.jsonb.JsonAdapter;
import io.avaje.jsonb.JsonReader;
import io.avaje.jsonb.JsonWriter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.spi.PropertyNames;
import io.avaje.jsonb.spi.ViewBuilder;
import io.avaje.jsonb.spi.ViewBuilderAware;

import java.lang.invoke.MethodHandle;

public class AddressJsonAdapter extends JsonAdapter<Address> implements ViewBuilderAware {

  private final JsonAdapter<String> stringAdapter;
  private final PropertyNames names;

  public AddressJsonAdapter(Jsonb jsonb) {
    stringAdapter = jsonb.adapter(String.class);
    names = jsonb.properties("street", "suburb", "city");
  }

  @Override
  public boolean isViewBuilderAware() {
    return true;
  }

  @Override
  public ViewBuilderAware viewBuild() {
    return this;
  }

  @Override
  public void build(ViewBuilder builder, String name, MethodHandle mh) {
    builder.beginObject(name, mh);
    builder.add("street", stringAdapter, builder.method(Address.class, "street", String.class));
    builder.add("suburb", stringAdapter, builder.method(Address.class, "suburb", String.class));
    builder.add("city", stringAdapter, builder.method(Address.class, "city", String.class));
    builder.endObject();
  }

  @Override
  public void toJson(JsonWriter writer, Address address) {
    writer.beginObject();
    writer.names(names);
    writer.name(0);
    stringAdapter.toJson(writer, address.street());
    writer.name(1);
    stringAdapter.toJson(writer, address.suburb());
    writer.name(2);
    stringAdapter.toJson(writer, address.city());
    writer.endObject();
  }

  @Override
  public Address fromJson(JsonReader reader) {

    Address address = new Address();

    reader.beginObject();
    while (reader.hasNextField()) {
      String fieldName = reader.nextField();
      switch (fieldName) {
        case "street": {
          address.street(stringAdapter.fromJson(reader));
          break;
        }
        case "suburb": {
          address.suburb(stringAdapter.fromJson(reader));
          break;
        }
        case "city": {
          address.city(stringAdapter.fromJson(reader));
          break;
        }
        default: {
          throw new IllegalStateException("fieldName " + fieldName + " not found ");
        }
      }
    }
    reader.endObject();
    return address;
  }

}
