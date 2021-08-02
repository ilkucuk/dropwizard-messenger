package com.kucuk.dw.service.resources;

import com.kucuk.dw.service.BlockingServiceClientFactory;
import com.kucuk.dw.service.api.CreateMessageRequest;
import com.kucuk.dw.service.api.CreateMessageResponse;
import com.kucuk.dw.service.api.ListMessageRequest;
import com.kucuk.dw.service.api.ListMessageResponse;
import com.kucuk.dw.service.api.Message;
import com.kucuk.dw.service.dao.MessageDoa;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    public MessageResource() {
    }

    @POST
    public CreateMessageResponse createMessage(CreateMessageRequest request) {

        long blockingCallTimeStamp = makeBlockingCall(request.getBlockingCallPeriod());
        String sha256hex = DigestUtils.sha256Hex(request.getRequestId() + request.getTitle() + request.getContent() + request.getAuthor());
        return CreateMessageResponse.builder()
                .responseId(Instant.now().toEpochMilli())
                .hash(sha256hex)
                .time(blockingCallTimeStamp)
                .sampleBooleanField(!request.getSampleBooleanField())
                .sampleDoubleField(request.getSampleDoubleField() + 0.1)
                .sampleIntegerField(request.getSampleIntegerField() + 1)
                .build();
    }

    @GET
    public ListMessageResponse listMessages(ListMessageRequest request) {
        long blockingCallTimeStamp = makeBlockingCall(request.getBlockingCallPeriod());
        List<Message> messages = MessageDoa.getMessages(request.getPageSize());
        return ListMessageResponse.builder()
                .messages(messages)
                .hasNext(true)
                .nextPageToken(request.getPageToken() + "-Next")
                .time(blockingCallTimeStamp)
                .build();
    }

    private Long makeBlockingCall(Integer blockingPeriod) {
        if (blockingPeriod > 0 && blockingPeriod < 5001) {
            WebTarget target = BlockingServiceClientFactory.getTarget(blockingPeriod);
            Response otherResponse = target.request().get();
            return otherResponse.readEntity(Long.class);
        }
        return 0L;
    }
}
