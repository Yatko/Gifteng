///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.springframework.social.connect.appengine;
//
//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.PreparedQuery;
//import com.google.appengine.api.datastore.Query;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import org.springframework.security.crypto.encrypt.TextEncryptor;
//import org.springframework.social.connect.Connection;
//import org.springframework.social.connect.ConnectionFactoryLocator;
//import org.springframework.social.connect.ConnectionKey;
//import org.springframework.social.connect.ConnectionRepository;
//import org.springframework.social.connect.UsersConnectionRepository;
//
///**
// * Taken from: https://jira.springsource.org/browse/SOCIAL-73
// * 
// * {@link UsersConnectionRepository} that uses the AppEngine Datastore API to persist connection data to BigTable.
// *
// * @author Geoffrey Chandler, geoffc@gmail.com
// */
//public class AppEngineUsersConnectionRepository implements UsersConnectionRepository {
//
//    private final ConnectionFactoryLocator connectionFactoryLocator;
//    private final TextEncryptor textEncryptor;
//    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//
//    public AppEngineUsersConnectionRepository(final ConnectionFactoryLocator connectionFactoryLocator,
//            final TextEncryptor textEncryptor) {
//
//        this.connectionFactoryLocator = connectionFactoryLocator;
//        this.textEncryptor = textEncryptor;
//    }
//
//    @Override
//    public List<String> findUserIdsWithConnection(Connection<?> connection) {
//        ConnectionKey key = connection.getKey();
//
//        // The Query interface assembles a query
//        Query q = new Query("UserConnection");
//        q.addFilter("providerId", Query.FilterOperator.EQUAL, key.getProviderId());
//        q.addFilter("providerUserId", Query.FilterOperator.EQUAL, key.getProviderUserId());
//
//        // PreparedQuery contains the methods for fetching query results
//        // from the datastore
//        PreparedQuery pq = datastore.prepare(q);
//
//        List<String> localUserIds = new ArrayList<String>();
//        for (Entity result : pq.asIterable()) {
//            localUserIds.add((String) result.getProperty("userId"));
//        }
//
//        return localUserIds;
//    }
//
//    @Override
//    public Set<String> findUserIdsConnectedTo(final String providerId, final Set<String> providerUserIds) {
//
//        final Query q = new Query("UserConnection");
//        q.addFilter("providerId", Query.FilterOperator.EQUAL, providerId);
//        q.addFilter("providerUserId", Query.FilterOperator.IN, providerUserIds);
//        final Set<String> localUserIds = new HashSet<String>();
//
//        final PreparedQuery pq = datastore.prepare(q);
//
//        for (final Entity entity : pq.asIterable()) {
//            localUserIds.add((String) entity.getProperty("userId"));
//        }
//
//        return localUserIds;
//    }
//
//    @Override
//    public ConnectionRepository createConnectionRepository(String userId) {
//        if (userId == null) {
//            throw new IllegalArgumentException("userId cannot be null");
//        }
//        return new AppEngineConnectionRepository(datastore, userId, connectionFactoryLocator, textEncryptor);
//    }
//}
