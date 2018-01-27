package org.ossg.remote.client.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.ossg.remote.client.Configuration;
import org.ossg.remote.client.Connection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DefaultConnection implements Connection{

    private Configuration configuration;

    public DefaultConnection(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    public Response GET(String path){
        String url = configuration.getBaseUrl() + path;
        ResteasyWebTarget target = getClient().target(url);
        if (configuration.isAuthenticated()){
            target.register(new BasicAuthentication(configuration.getUsername(), configuration.getPassword()));
        }
        return target.request().get();
    }

    @Override
    public Response POST(String path, String entity) {
        String url = configuration.getBaseUrl() + path;
        ResteasyWebTarget target = getClient().target(url);
        if (configuration.isAuthenticated()){
            target.register(new BasicAuthentication(configuration.getUsername(), configuration.getPassword()));
        }
        return target.request().post(Entity.entity(entity, "application/json"));
    }

    private ResteasyClient getClient(){
        ResteasyClient client = null;
        if (configuration.getProtocol().equals(Configuration.SECURE_HTTP)){
            try {
                ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(createUntrustedCertHttpClient());
                client = new ResteasyClientBuilder().httpEngine(engine).build();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        } else {
            client = new ResteasyClientBuilder().build();
        }
        return client;
    }

    private HttpClient createUntrustedCertHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder b = HttpClientBuilder.create();

        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        b.setSslcontext( sslContext);
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager( socketFactoryRegistry);
        b.setConnectionManager( connMgr);
        HttpClient client = b.build();
        return client;
    }

}
