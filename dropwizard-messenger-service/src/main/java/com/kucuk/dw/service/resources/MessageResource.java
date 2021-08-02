package com.kucuk.dw.service.resources;

import com.kucuk.dw.service.OtherMessageServiceClientFactory;
import com.kucuk.dw.service.api.CreateMessageRequest;
import com.kucuk.dw.service.api.CreateMessageResponse;
import com.kucuk.dw.service.api.Message;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;

import static com.kucuk.dw.service.DropwizardServerApplication.MESSAGE_CONTENT_ARRAY;
import static com.kucuk.dw.service.DropwizardServerApplication.NO_MESSAGES;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    public MessageResource() {
    }

    @POST
    public CreateMessageResponse createMessage(CreateMessageRequest request) {

        String sha256hex = DigestUtils.sha256Hex(request.getRequestId() + request.getTitle() + request.getContent() + request.getAuthor());
        Double sampleDoubleField = request.getSampleDoubleField() + 1.234d;

        if (request.getSleepPeriod() > 0 && request.getSleepPeriod() < 6000) {
            try {
                Thread.sleep(request.getSleepPeriod());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (request.getSleepPeriod() < 0) {
            WebTarget target = OtherMessageServiceClientFactory.getTarget();
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

            CreateMessageRequest otherRequest = CreateMessageRequest.builder()
                    .requestId(Instant.now().toEpochMilli())
                    .author("ilker.kucuk@oracle.com")
                    .title("Quest For Performance")
                    .content("I don't know what I am doing :(")
                    .time(Instant.now().toEpochMilli())
                    .sleepPeriod(request.getSleepPeriod() * -1)
                    .sampleBooleanField(true)
                    .sampleDoubleField(1d)
                    .sampleIntegerField(1)
                    .messageCount(request.getMessageCount())
                    .build();

            Response otherResponse = invocationBuilder.post(Entity.entity(otherRequest, MediaType.APPLICATION_JSON));
            CreateMessageResponse entity = otherResponse.readEntity(CreateMessageResponse.class);
            sampleDoubleField = entity.getSampleDoubleField();
        }

        Message[] messages = new Message[request.getMessageCount()];
        for (int i = 0; i < request.getMessageCount(); i++) {
            messages[i] = Message.builder()
                    .id(Instant.now().toEpochMilli())
                    .content(MESSAGE_CONTENT_ARRAY[i % NO_MESSAGES])
                    .time(Instant.now().toEpochMilli())
                    .build();
        }
        return CreateMessageResponse.builder()
                .responseId(Instant.now().toEpochMilli())
                .hash(sha256hex)
                .time(Instant.now().toEpochMilli())
                .sampleBooleanField(!request.getSampleBooleanField())
                .sampleDoubleField(sampleDoubleField)
                .sampleIntegerField(request.getSampleIntegerField() * 2)
                .messages(messages)
                .build();
    }
}
