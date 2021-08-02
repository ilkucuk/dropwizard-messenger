package com.kucuk.dw.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;

@Path("/block")
@Produces(MediaType.APPLICATION_JSON)
public class BlockingResource {

    @GET
    @Path("/{milliseconds}")
    public Long block(@PathParam("milliseconds") int milliseconds) {
        if (milliseconds > 0) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Instant.now().toEpochMilli();
    }
}
