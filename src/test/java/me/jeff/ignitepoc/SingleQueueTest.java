package me.jeff.ignitepoc;

import com.google.protobuf.InvalidProtocolBufferException;
import me.jeff.ignitepoc.grpc.PersonEntity;
import me.jeff.ignitepoc.queue.TradeDataQueue;
import org.junit.Test;

public class SingleQueueTest {

    @Test
    public void test_write_data() {
        TradeDataQueue queue = new TradeDataQueue();
        queue.init();
        PersonEntity.Person.Builder personBuilder = PersonEntity.Person.newBuilder();
        personBuilder.setId(1);
        personBuilder.setName("jeff");
        personBuilder.setEmail("123456@qq.com");
        PersonEntity.Person personEntity = personBuilder.build();
        queue.writeData(personEntity.toByteArray());
    }

    @Test
    public void test_read_data() throws InvalidProtocolBufferException {
        TradeDataQueue queue = new TradeDataQueue();
        queue.init();
        PersonEntity.Person p = PersonEntity.Person.parseFrom(queue.readByte());
        System.out.println(p.getName());
    }
}
