package org.streampipes.rest.impl;

import com.google.gson.JsonSyntaxException;
import org.streampipes.manager.execution.status.PipelineStatusManager;
import org.streampipes.manager.operations.Operations;
import org.streampipes.model.client.exception.InvalidConnectionException;
import org.streampipes.model.client.messages.Notification;
import org.streampipes.model.client.messages.NotificationType;
import org.streampipes.model.client.messages.Notifications;
import org.streampipes.model.client.messages.SuccessMessage;
import org.streampipes.model.client.pipeline.PipelineOperationStatus;
import org.streampipes.rest.annotation.GsonWithIds;
import org.streampipes.rest.api.IPipeline;
import org.streampipes.manager.storage.StorageManager;
import org.streampipes.commons.exceptions.NoMatchingFormatException;
import org.streampipes.commons.exceptions.NoMatchingJsonSchemaException;
import org.streampipes.commons.exceptions.NoMatchingProtocolException;
import org.streampipes.commons.exceptions.NoMatchingSchemaException;
import org.streampipes.commons.exceptions.NoSuitableSepasAvailableException;
import org.streampipes.commons.exceptions.RemoteServerNotAccessibleException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.UUID;

@Path("/v2/users/{username}/pipelines")
public class Pipeline extends AbstractRestInterface implements IPipeline {


    @Override
    public Response getAvailable(String username) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Response getFavorites(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/own")
    @GsonWithIds
    @Override
    public Response getOwn(@PathParam("username") String username) {
        return ok(getUserService()
                .getOwnPipelines(username));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/system")
    @GsonWithIds
    @Override
    public Response getSystemPipelines() {
        return ok(getPipelineStorage().getSystemPipelines());
    }

    @Override
    public Response addFavorite(String username, String elementUri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response removeFavorite(String username, String elementUri) {
        // TODO Auto-generated method stub
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pipelineId}/status")
    @GsonWithIds
    @Override
    public Response getPipelineStatus(@PathParam("username") String username, @PathParam("pipelineId") String pipelineId) {
        return ok(PipelineStatusManager.getPipelineStatus(pipelineId, 5));
    }

    @DELETE
    @Path("/{pipelineId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GsonWithIds
    public Response removeOwn(@PathParam("username") String username, @PathParam("pipelineId") String elementUri) {
        StorageManager.INSTANCE.getPipelineStorageAPI().deletePipeline(elementUri);
        return statusMessage(Notifications.success("Pipeline deleted"));
    }

    @Override
    public String getAsJsonLd(String elementUri) {
        return null;
    }

    @GET
    @Path("/{pipelineId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GsonWithIds
    @Override
    public Response getElement(@PathParam("username") String username, @PathParam("pipelineId") String pipelineId) {
        return ok(StorageManager.INSTANCE.getPipelineStorageAPI().getPipeline(pipelineId));
    }

    @Path("/{pipelineId}/start")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @GsonWithIds
    public Response start(@PathParam("username") String username, @PathParam("pipelineId") String pipelineId) {
        try {
            org.streampipes.model.client.pipeline.Pipeline pipeline = getPipelineStorage()
                    .getPipeline(pipelineId);
            PipelineOperationStatus status = Operations.startPipeline(pipeline);
            return ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return statusMessage(Notifications.error(NotificationType.UNKNOWN_ERROR));
        }
    }

    @Path("/{pipelineId}/stop")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @GsonWithIds
    public Response stop(@PathParam("username") String username, @PathParam("pipelineId") String pipelineId) {
        try {
            org.streampipes.model.client.pipeline.Pipeline pipeline = getPipelineStorage().getPipeline(pipelineId);
            PipelineOperationStatus status = Operations.stopPipeline(pipeline);
            return ok(status);
        } catch
                (Exception e) {
            e.printStackTrace();
            return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @GsonWithIds
    public Response addPipeline(@PathParam("username") String username, org.streampipes.model.client.pipeline.Pipeline pipeline) {
        String pipelineId = UUID.randomUUID().toString();
        pipeline.setPipelineId(pipelineId);
        pipeline.setRunning(false);
        pipeline.setCreatedByUser(username);
        pipeline.setCreatedAt(new Date().getTime());
        //userService.addOwnPipeline(username, pipeline);
        Operations.storePipeline(pipeline);
        SuccessMessage message = Notifications.success(NotificationType.PIPELINE_STORAGE_SUCCESS);
        message.addNotification(new Notification("id", pipelineId));
        return ok(message);
    }

    @Path("/recommend")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @GsonWithIds
    public Response recommend(@PathParam("username") String email, org.streampipes.model.client.pipeline.Pipeline pipeline) {
        try {
            return ok(Operations.findRecommendedElements(email, pipeline));
        } catch (JsonSyntaxException e) {
            return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
        } catch (NoSuitableSepasAvailableException e) {
            return constructErrorMessage(new Notification(NotificationType.NO_SEPA_FOUND.title(), NotificationType.NO_SEPA_FOUND.description(), e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
        }
    }

    @Path("/update")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @GsonWithIds
    public Response update(org.streampipes.model.client.pipeline.Pipeline pipeline, @PathParam("username") String username) {
        try {
            return ok(Operations.validatePipeline(pipeline, true, username));
        } catch (JsonSyntaxException e) {
            return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
        } catch (NoMatchingSchemaException e) {
            return constructErrorMessage(new Notification(NotificationType.NO_VALID_CONNECTION.title(), NotificationType.NO_VALID_CONNECTION.description(), e.getMessage()));
        } catch (NoMatchingFormatException e) {
            return constructErrorMessage(new Notification(NotificationType.NO_MATCHING_FORMAT_CONNECTION.title(), NotificationType.NO_MATCHING_FORMAT_CONNECTION.description(), e.getMessage()));
        } catch (NoMatchingProtocolException e) {
            return constructErrorMessage(new Notification(NotificationType.NO_MATCHING_PROTOCOL_CONNECTION.title(), NotificationType.NO_MATCHING_PROTOCOL_CONNECTION.description(), e.getMessage()));
        } catch (RemoteServerNotAccessibleException e) {
            return constructErrorMessage(new Notification(NotificationType.REMOTE_SERVER_NOT_ACCESSIBLE.title(), NotificationType.REMOTE_SERVER_NOT_ACCESSIBLE.description(), e.getMessage()));
        } catch (NoMatchingJsonSchemaException e) {
            return constructErrorMessage(new Notification(NotificationType.REMOTE_SERVER_NOT_ACCESSIBLE.title(), NotificationType.REMOTE_SERVER_NOT_ACCESSIBLE.description(), e.getMessage()));
        }
        catch (InvalidConnectionException e) {
            return ok(e.getErrorLog());
        } catch (Exception e) {
            e.printStackTrace();
            return constructErrorMessage(new Notification(NotificationType.UNKNOWN_ERROR.title(), NotificationType.UNKNOWN_ERROR.description(), e.getMessage()));
        }
    }


    @PUT
    @Path("/{pipelineId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GsonWithIds
    @Override
    public Response overwritePipeline(@PathParam("username") String username, org.streampipes.model.client.pipeline.Pipeline pipeline) {
        StorageManager.INSTANCE.getPipelineStorageAPI().updatePipeline(pipeline);
        return statusMessage(Notifications.success("Pipeline modified"));
    }

}
