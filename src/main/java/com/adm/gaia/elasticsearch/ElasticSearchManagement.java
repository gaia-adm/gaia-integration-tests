package com.adm.gaia.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adm.gaia.webhook.GaiaConfiguration;

/**
 * Created by kornfeld on 31/03/2016.
 */
@Component
public class ElasticSearchManagement {
    
    private static final Logger _logger = LoggerFactory.getLogger(ElasticSearchManagement.class);

        @Autowired
        private GaiaConfiguration _config;
    private Client _client;
    
    private void init() {

        try {
            System.out.println("Going to print host...");
            System.out.println("getByName " + InetAddress.getByName(_config.getGaiaESHost()));
            _client =
                    TransportClient.builder().build().addTransportAddress(
                            new InetSocketTransportAddress(
                                    InetAddress.getByName(_config.getGaiaESHost()),
                                    _config.getGaiaESPort()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        _logger.info("elasticsearch client node initialized");
    }
    
    public Client getClient() {
        
        if (_client == null) {
            init();
        }
        
        return _client;
    }
    
    @PreDestroy
    private void destroy() {
        
        if (_client != null) {
            _client.close();
        }
    }
}
