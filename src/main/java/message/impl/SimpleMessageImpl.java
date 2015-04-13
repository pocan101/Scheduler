package message.impl;

import message.interfaces.Message;

/**
 * Created by Jose Cortes on 4/9/2015.
 */
public final class SimpleMessageImpl implements Message {

    private final int group;
    private final long id;
    private final MESSAGE_TYPE type;
    private final String Headers;
    private final String Body;
    private volatile Boolean isCompleted;

    /**
     * Constructs a simple message
     *
     * @param group   of the message
     * @param id      of the message
     * @param headers of the message
     * @param body    of the message
     */
    public SimpleMessageImpl(final int group, final long id, final String headers, final String body) {
        this.id = id;
        Headers = headers;
        Body = body;
        this.isCompleted = (false);
        this.group = group;
        this.type = MESSAGE_TYPE.TYPE_INFORMATION;
    }

    /**
     * Contructs a simple messasge
     *
     * @param group   of the message
     * @param id      of the message
     * @param type    of the message
     * @param headers of the message. To emulate a real case scenario
     * @param body    of the message.To emulate a real case scenario
     */
    public SimpleMessageImpl(final int group, final long id, final MESSAGE_TYPE type, final String headers, final String body) {
        this.id = id;
        Headers = headers;
        Body = body;
        this.isCompleted = (false);
        this.group = group;
        this.type = type;
    }

    /**
     * Creates a new Message of type information with default body and header
     *
     * @param group of the message
     * @param id    of the message
     */
    public SimpleMessageImpl(final int group, final long id) {
        this.id = id;
        Headers = "DEFAULT HEADERS";
        Body = "DEFAULT BODY";
        this.isCompleted = (false);
        this.group = group;
        this.type = MESSAGE_TYPE.TYPE_INFORMATION;
    }

    /**
     * Creates a default message of a given type with default headers and body
     *
     * @param group of the message
     * @param id    of the message
     * @param type  of the message
     */
    public SimpleMessageImpl(final int group, final long id, final MESSAGE_TYPE type) {
        this.id = id;
        Headers = "DEFAULT HEADERS";
        Body = "DEFAULT BODY";
        this.isCompleted = (false);
        this.group = group;
        this.type = type;
    }

    /**
     * Mutable property to show whether the message is complete
     */
    public void completed() {
        isCompleted = (true);
    }

    /**
     * reports completion fo this message
     *
     * @return boolean
     */
    public Boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Gets the id of the message
     *
     * @return the id
     */
    public long getID() {
        return id;
    }

    /**
     * Gets message type
     *
     * @return mesasge type
     */
    public MESSAGE_TYPE getType() {
        return type;
    }

    /**
     * Gests body of the message
     *
     * @return body of the message
     */
    public String getBody() {
        return null;
    }

    /**
     * Gets the header of this message
     *
     * @return headers of this message
     */
    public String getHeaders() {
        return null;
    }

    /**
     * Gets group in which the message is in
     *
     * @return the group
     */
    public int getGroup() {
        return group;
    }
}
