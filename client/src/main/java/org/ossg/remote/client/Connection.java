package org.ossg.remote.client;

import javax.ws.rs.core.Response;

public interface Connection {
    Response GET(String path);
    Response POST(String path, String entity);
}