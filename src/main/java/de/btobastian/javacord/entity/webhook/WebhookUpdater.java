package de.btobastian.javacord.entity.webhook;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.channel.ServerTextChannel;
import de.btobastian.javacord.entity.webhook.impl.ImplWebhook;
import de.btobastian.javacord.util.FileContainer;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update webhooks.
 */
public class WebhookUpdater {

    /**
     * The webhook to update.
     */
    protected final Webhook webhook;

    /**
     * The reason for the update.
     */
    private String reason = null;

    /**
     * The name to update.
     */
    protected String name = null;

    /**
     * The channel to update.
     */
    protected ServerTextChannel channel = null;

    /**
     * The avatar to update.
     */
    private FileContainer avatar = null;

    /**
     * Whether the avatar should be updated or not.
     */
    protected boolean updateAvatar = false;

    /**
     * Creates a new webhook updater.
     *
     * @param webhook The webhook to update.
     */
    public WebhookUpdater(Webhook webhook) {
        this.webhook = webhook;
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Queues the channel to be updated.
     *
     * @param channel The new channel of the webhook.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setChannel(ServerTextChannel channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(BufferedImage avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(BufferedImage avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(File avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(Icon avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(URL avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar);
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(byte[] avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(byte[] avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     * This method assumes the file type is "png"!
     *
     * @param avatar The avatar to set.
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(InputStream avatar) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, "png");
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be updated.
     *
     * @param avatar The avatar to set.
     * @param fileType The type of the avatar, e.g. "png" or "jpg".
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater setAvatar(InputStream avatar, String fileType) {
        this.avatar = (avatar == null) ? null : new FileContainer(avatar, fileType);
        updateAvatar = true;
        return this;
    }

    /**
     * Queues the avatar to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public WebhookUpdater removeAvatar() {
        this.avatar = null;
        updateAvatar = true;
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return The updated webhook or the current instance if no updates were queued.
     */
    public CompletableFuture<Webhook> update() {
        boolean patchWebhook = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
            patchWebhook = true;
        }
        if (channel != null) {
            body.put("channel_id", channel.getIdAsString());
            patchWebhook = true;
        }
        if (updateAvatar) {
            if (avatar == null) {
                body.putNull("avatar");
            }
            patchWebhook = true;
        }
        if (patchWebhook) {
            if (avatar != null) {
                return avatar.asByteArray(webhook.getApi()).thenAccept(bytes -> {
                    String base64Avatar = "data:image/" + avatar.getFileType() + ";base64," +
                            Base64.getEncoder().encodeToString(bytes);
                    body.put("avatar", base64Avatar);
                }).thenCompose(aVoid -> new RestRequest<Webhook>(webhook.getApi(), RestMethod.PATCH, RestEndpoint.WEBHOOK)
                        .setUrlParameters(webhook.getIdAsString())
                        .setBody(body)
                        .setAuditLogReason(reason)
                        .execute(result -> new ImplWebhook(webhook.getApi(), result.getJsonBody())));
            }
            return new RestRequest<Webhook>(webhook.getApi(), RestMethod.PATCH, RestEndpoint.WEBHOOK)
                    .setUrlParameters(webhook.getIdAsString())
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> new ImplWebhook(webhook.getApi(), result.getJsonBody()));
        } else {
            return CompletableFuture.completedFuture(webhook);
        }
    }

}