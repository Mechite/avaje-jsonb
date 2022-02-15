package io.avaje.jsonb.diesel;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class JsonWriterTest {

  @Test
  void recycle() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    JsonGenerator generator = Recycle.generator(os);

    writeHello(generator, "hello");

    String asJson = os.toString(StandardCharsets.UTF_8);
    assertThat(asJson).isEqualTo("{\"one\":\"hello\"}");

    ByteArrayOutputStream os1 = new ByteArrayOutputStream();
    JsonGenerator generator1 = Recycle.generator(os1);

    writeHello(generator1, "hi");

    String asJson1 = os1.toString(StandardCharsets.UTF_8);
    assertThat(asJson1).isEqualTo("{\"one\":\"hi\"}");
  }

  @Test
  void recycle_toString() {

    JsonGenerator generator = Recycle.generator();
    writeHello(generator, "hello");
    assertThat(generator.toString()).isEqualTo("{\"one\":\"hello\"}");

    JsonGenerator generator1 = Recycle.generator();
    writeHello(generator1, "hi");
    assertThat(generator1.toString()).isEqualTo("{\"one\":\"hi\"}");
  }

  private void writeHello(JsonGenerator generator, String message) {
    JsonWriteAdapter fw = new JsonWriteAdapter(generator, true, true);

    fw.beginObject();
    fw.name("one");
    fw.value(message);
    fw.endObject();
    fw.close();
  }

  @Test
  void basic() {

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    JGenerator dJsonWriter = new JGenerator();
    dJsonWriter.prepare(os);

    JsonWriteAdapter fw = new JsonWriteAdapter(dJsonWriter, true, true);

    fw.beginArray();
    fw.beginObject();
    fw.name("one");
    fw.value("hello");
    fw.name("size");
    fw.value(43);
    fw.endObject();
    fw.beginObject();
    fw.name("one");
    fw.value("another");
    fw.name("active");
    fw.value(true);
    fw.name("flags");
    fw.beginArray();
    fw.value(42);
    fw.value(43);
    fw.endArray();
    fw.endObject();
    fw.endArray();
    fw.close();

    String asJson = os.toString(StandardCharsets.UTF_8);
    assertThat(asJson).isEqualTo("[{\"one\":\"hello\",\"size\":43},{\"one\":\"another\",\"active\":true,\"flags\":[42,43]}]");
  }


  @Test
  void using_names() {

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    JGenerator dJsonWriter = new JGenerator();
    dJsonWriter.prepare(os);

    JsonWriteAdapter fw = new JsonWriteAdapter(dJsonWriter, true, true);

    JsonNames names = JsonNames.of("one", "size", "active","flags");

    fw.beginArray();

    fw.beginObject();
    fw.names(names);
    fw.name(0);
    fw.value("hello");
    fw.name(1);
    fw.value(43);
    fw.endObject();

    fw.beginObject();
    fw.names(names);
    fw.name(0);
    fw.value("another");
    fw.name(2);
    fw.value(true);
    fw.name(3);
    fw.beginArray();
    fw.value(42);
    fw.value(43);
    fw.endArray();
    fw.endObject();
    fw.endArray();
    fw.close();

    String asJson = os.toString(StandardCharsets.UTF_8);
    assertThat(asJson).isEqualTo("[{\"one\":\"hello\",\"size\":43},{\"one\":\"another\",\"active\":true,\"flags\":[42,43]}]");

  }
}
