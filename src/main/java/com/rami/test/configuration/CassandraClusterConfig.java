package com.rami.test.configuration;

import com.datastax.driver.core.JdkSSLOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by ramistefanidis on 8/22/17.
 *
 * This Configuration class contains the Apache Cassandra Cluster Bean.
 *
 * Spring Boot Cassandra starter provides a cluster bean on its own,
 *
 * however this bean is requiered as we need to configure SSL for client to Node
 *
 * communication with Cassandra.
 */
@Configuration
public class CassandraClusterConfig extends AbstractCassandraConfiguration {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.username}")
    private String username;

    @Value("${spring.data.cassandra.password}")
    private String password;

    @Value("${ssl.keystore.keystoreLocation}")
    private String keystoreLocation;

    @Value("${ssl.keystore.keystorePassword}")
    private String keystorePassword;

    @Value("${ssl.trustStore.trustStoreLocation}")
    private String trustStoreLocation;

    @Value("${ssl.trustStore.trustStorePassword}")
    private String trustStorePassword;

    @Value("${ssl.cipherSuite}")
    private String cipherSuite;


    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }

    @Bean
    public CassandraClusterFactoryBean cluster() {

        final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();

        try {
            cluster.setContactPoints(contactPoints);
            cluster.setPort(port);
            cluster.setUsername(username);
            cluster.setPassword(password);
            cluster.setSslEnabled(true);
            cluster.setSslOptions(getSslOptions());
        } catch(Exception e) {
            LOG.info("Exception caught while creating CassandraClusterFactoryBean",e);
        }

        return cluster;
    }


    private JdkSSLOptions getSslOptions() {
        SSLContext sslcontext = null;

        try {
            final TrustManager[] trustManagers = getTrustManagers();
            final KeyManager[] keyManagers = getKeyManagers();

            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(keyManagers, trustManagers, new SecureRandom());
            LOG.info("Got SSL context");
        } catch (Exception e) {
            LOG.error("Exception occured",e);
        }

        final JdkSSLOptions sslOptions = JdkSSLOptions.builder()
                .withSSLContext(sslcontext).withCipherSuites(new String[]{cipherSuite})
                .build();

        LOG.info("Completed ssl options.");

        return sslOptions;
    }

    private TrustManager[] getTrustManagers() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        LOG.info("Begin Getting Trust Managers");

        final InputStream trustStoreLocationInputStream = CassandraClusterConfig.class.getResourceAsStream(trustStoreLocation);
        final KeyStore keystore = KeyStore.getInstance("JKS");
        final char[] trustStorePasswordCharArray = trustStorePassword.toCharArray();

        keystore.load(trustStoreLocationInputStream, trustStorePasswordCharArray);

        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);

        final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        LOG.info("Completed getting trust managers");
        return trustManagers;
    }

    private KeyManager[] getKeyManagers() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        LOG.info("Begin getting Key Managers");

        final InputStream keyStoreInputStream = CassandraClusterConfig.class.getResourceAsStream(keystoreLocation);
        final KeyStore keystore = KeyStore.getInstance("JKS");
        final char[] keystorePasswordCharachterArray = keystorePassword.toCharArray();
        keystore.load(keyStoreInputStream, keystorePasswordCharachterArray);

        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keystore, keystorePassword.toCharArray());

        final KeyManager[] keyManagers = kmf.getKeyManagers();

        LOG.info("Completed getting Key Managers.");

        return keyManagers;

    }




}
