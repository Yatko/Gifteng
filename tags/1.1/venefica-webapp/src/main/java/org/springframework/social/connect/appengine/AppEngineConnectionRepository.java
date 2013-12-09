///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.springframework.social.connect.appengine;
//
//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.FetchOptions;
//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.PreparedQuery;
//import com.google.appengine.api.datastore.Query;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.TreeMap;
//import org.springframework.security.crypto.encrypt.TextEncryptor;
//import org.springframework.social.connect.Connection;
//import org.springframework.social.connect.ConnectionData;
//import org.springframework.social.connect.ConnectionFactory;
//import org.springframework.social.connect.ConnectionFactoryLocator;
//import org.springframework.social.connect.ConnectionKey;
//import org.springframework.social.connect.ConnectionRepository;
//import org.springframework.social.connect.DuplicateConnectionException;
//import org.springframework.social.connect.NoSuchConnectionException;
//import org.springframework.social.connect.NotConnectedException;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
///**
// * Taken from: https://jira.springsource.org/browse/SOCIAL-73
// * 
// * User-specific connection repository created by the {@link AppEngineUsersConnectionRepository}.
// *
// * @author Geoffrey Chandler, geoffc@gmail.com
// */
//class AppEngineConnectionRepository implements ConnectionRepository {
//
//    private final String userId;
//    private final DatastoreService datastore;
//    private final ConnectionFactoryLocator connectionFactoryLocator;
//    private final TextEncryptor textEncryptor;
//
//    public AppEngineConnectionRepository(final DatastoreService datastore,
//            final String userId,
//            final ConnectionFactoryLocator connectionFactoryLocator,
//            final TextEncryptor textEncryptor) {
//        this.datastore = datastore;
//        this.userId = userId;
//        this.connectionFactoryLocator = connectionFactoryLocator;
//        this.textEncryptor = textEncryptor;
//    }
//
//    @Override
//    public MultiValueMap<String, Connection<?>> findAllConnections() {
//        final MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
//        final Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
//        for (String registeredProviderId : registeredProviderIds) {
//            connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
//        }
//        final Query q = new Query("UserConnection");
//        q.addFilter("userId", Query.FilterOperator.EQUAL, userId);
//        q.addSort("providerId");
//        q.addSort("rank");
//        final PreparedQuery pq = datastore.prepare(q);
//        for (final Entity rs : pq.asIterable()) {
//            final Connection<?> connection = mapUserConnectionEntityToConnection(rs);
//            final String providerId = connection.getKey().getProviderId();
//            if (connections.get(providerId).isEmpty()) {
//                connections.put(providerId, new LinkedList<Connection<?>>());
//            }
//            connections.add(providerId, connection);
//        }
//        return connections;
//    }
//
//    @Override
//    public List<Connection<?>> findConnections(final String providerId) {
//        final Query q = new Query("UserConnection");
//        q.addFilter("userId", Query.FilterOperator.EQUAL, userId);
//        q.addFilter("providerId", Query.FilterOperator.EQUAL, providerId);
//        q.addSort("rank");
//        final List<Connection<?>> connections = new ArrayList<Connection<?>>();
//        final PreparedQuery pq = datastore.prepare(q);
//        for (final Entity userConnectionEntity : pq.asIterable()) {
//            connections.add(mapUserConnectionEntityToConnection(userConnectionEntity));
//        }
//        return connections;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <A> List<Connection<A>> findConnections(final Class<A> apiType) {
//        final List<?> connections = findConnections(getProviderId(apiType));
//        return (List<Connection<A>>) connections;
//    }
//
//    @SuppressWarnings("unused")
//    @Override
//    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
//        if (providerUsers.isEmpty()) {
//            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
//        }
//        final SortedMap<String, SortedMap<Number, Connection<?>>> indexedConnections =
//                new TreeMap<String, SortedMap<Number, Connection<?>>>();
//        for (Map.Entry<String, List<String>> entry : providerUsers.entrySet()) {
//            final String providerId = entry.getKey();
//            final Query q = new Query("UserConnection");
//            q.addFilter("userId", Query.FilterOperator.EQUAL, userId);
//            q.addFilter("providerId", Query.FilterOperator.EQUAL, providerId);
//            q.addFilter("providerUserId", Query.FilterOperator.IN, entry.getValue());
//            final PreparedQuery pq = datastore.prepare(q);
//            for (final Entity e : pq.asIterable()) {
//                final Connection<?> connection = mapUserConnectionEntityToConnection(e);
//                if (indexedConnections.containsKey(providerId)) {
//                    indexedConnections.get(providerId).put((Number) e.getProperty("rank"), connection);
//                } else {
//                    final SortedMap<Number, Connection<?>> rankMap = new TreeMap<Number, Connection<?>>();
//                    rankMap.put((Number) e.getProperty("rank"), connection);
//                    indexedConnections.put(providerId, rankMap);
//                }
//            }
//        }
//        final MultiValueMap<String, Connection<?>> connectionsForUsers =
//                new LinkedMultiValueMap<String, Connection<?>>();
//        for (final SortedMap<Number, Connection<?>> providerIdMap : indexedConnections.values()) {
//            for (final Connection<?> connection : providerIdMap.values()) {
//                final String providerId = connection.getKey().getProviderId();
//                final List<String> userIds = providerUsers.get(providerId);
//                List<Connection<?>> connections = connectionsForUsers.get(providerId);
//                if (connections == null) {
//                    connections = new ArrayList<Connection<?>>(userIds.size());
//                    for (final String ignore : userIds) {
//                        connections.add(null);
//                    }
//                    connectionsForUsers.put(providerId, connections);
//                }
//                final String providerUserId = connection.getKey().getProviderUserId();
//                int connectionIndex = userIds.indexOf(providerUserId);
//                connections.set(connectionIndex, connection);
//            }
//        }
//        return connectionsForUsers;
//    }
//
//    @Override
//    public Connection<?> getConnection(final ConnectionKey connectionKey) {
//        final Entity e = getUniqueConnectionEntity(connectionKey.getProviderId(), connectionKey.getProviderUserId());
//        if (e == null) {
//            throw new NoSuchConnectionException(connectionKey);
//        }
//        return mapUserConnectionEntityToConnection(e);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <A> Connection<A> getConnection(final Class<A> apiType, final String providerUserId) {
//        final String providerId = getProviderId(apiType);
//        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <A> Connection<A> getPrimaryConnection(final Class<A> apiType) {
//        final String providerId = getProviderId(apiType);
//        final Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
//        if (connection == null) {
//            throw new NotConnectedException(providerId);
//        }
//        return connection;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <A> Connection<A> findPrimaryConnection(final Class<A> apiType) {
//        final String providerId = getProviderId(apiType);
//        return (Connection<A>) findPrimaryConnection(providerId);
//    }
//
//    @Override
//    public void addConnection(final Connection<?> connection) {
//        final ConnectionData data = connection.createData();
//        if (getUniqueConnectionEntity(data.getProviderId(), data.getProviderUserId(), true) != null) {
//            throw new DuplicateConnectionException(connection.getKey());
//        }
//        // select coalesce(max(rank) + 1, 1) as rank from UserConnection where userId = ? and providerId = ?)
//        final Query rankQuery = new Query("UserConnection");
//        rankQuery.addFilter("userId", Query.FilterOperator.EQUAL, userId);
//        rankQuery.addFilter("providerId", Query.FilterOperator.EQUAL, data.getProviderId());
//        rankQuery.addFilter("rank", Query.FilterOperator.NOT_EQUAL, null);
//        rankQuery.addSort("rank", Query.SortDirection.DESCENDING);
//        final List<Entity> highestRanking = datastore.prepare(rankQuery).asList(FetchOptions.Builder.withLimit(1));
//        final int rank = highestRanking.size() == 0 ? 1 : (Integer) highestRanking.get(0).getProperty("rank") + 1;
//
//        final Query q = new Query("UserConnection").setKeysOnly();
//        q.addFilter("userId", Query.FilterOperator.EQUAL, userId);
//        q.addFilter("providerId", Query.FilterOperator.EQUAL, data.getProviderId());
//        q.addFilter("rank", Query.FilterOperator.EQUAL, rank);
//        if (datastore.prepare(q).countEntities(FetchOptions.Builder.withLimit(1)) > 0) {
//            throw new DuplicateConnectionException(connection.getKey());
//        }
//
//        final Entity newUserConnection = new Entity("UserConnection");
//        newUserConnection.setProperty("userId", userId);
//        newUserConnection.setProperty("providerId", data.getProviderId());
//        newUserConnection.setProperty("providerUserId", data.getProviderUserId());
//        newUserConnection.setProperty("rank", rank);
//        newUserConnection.setProperty("displayName", data.getDisplayName());
//        newUserConnection.setProperty("profileUrl", data.getProfileUrl());
//        newUserConnection.setProperty("imageUrl", data.getImageUrl());
//        newUserConnection.setProperty("accessToken", encrypt(data.getAccessToken()));
//        newUserConnection.setProperty("secret", encrypt(data.getSecret()));
//        newUserConnection.setProperty("refreshToken", encrypt(data.getRefreshToken()));
//        newUserConnection.setProperty("expireTime", data.getExpireTime());
//        datastore.put(newUserConnection);
//    }
//
//    @Override
//    public void updateConnection(final Connection<?> connection) {
//        final ConnectionData data = connection.createData();
//        final Entity e = getUniqueConnectionEntity(data.getProviderId(), data.getProviderUserId());
//        if (e == null) {
//            return;
//        }
//        e.setProperty("displayName", data.getDisplayName());
//        e.setProperty("profileUrl", data.getProfileUrl());
//        e.setProperty("imageUrl", data.getImageUrl());
//        e.setProperty("accessToken", encrypt(data.getAccessToken()));
//        e.setProperty("secret", encrypt(data.getSecret()));
//        e.setProperty("refreshToken", encrypt(data.getRefreshToken()));
//        e.setProperty("expireTime", data.getExpireTime());
//        datastore.put(e);
//    }
//
//    @Override
//    public void removeConnections(final String providerId) {
//        final Query q = new Query("UserConnection").setKeysOnly();
//        q.addFilter("userId", Query.FilterOperator.EQUAL, userId);
//        q.addFilter("providerId", Query.FilterOperator.EQUAL, providerId);
//        final PreparedQuery pq = datastore.prepare(q);
//        final List<Key> keysToDelete = new ArrayList<Key>();
//        for (final Entity e : pq.asIterable()) {
//            keysToDelete.add(e.getKey());
//        }
//        datastore.delete(keysToDelete);
//    }
//
//    public void removeConnection(final ConnectionKey connectionKey) {
//        final Entity entityToDelete = getUniqueConnectionEntity(connectionKey.getProviderId(),
//                connectionKey.getProviderUserId(), true);
//        if (entityToDelete == null) {
//            return;
//        }
//        datastore.delete(entityToDelete.getKey());
//    }
//
//    // internal helpers
//    private Entity getUniqueConnectionEntity(final String providerId,
//            final String providerUserId) {
//
//        return getUniqueConnectionEntity(providerId, providerUserId, false);
//    }
//
//    private Entity getUniqueConnectionEntity(final String providerId,
//            final String providerUserId,
//            final boolean keysOnly) {
//
//        final Query q = new Query("UserConnection");
//        if (keysOnly) {
//            q.setKeysOnly();
//        }
//        q.addFilter("userId", Query.FilterOperator.EQUAL, userId);
//        q.addFilter("providerId", Query.FilterOperator.EQUAL, providerId);
//        q.addFilter("providerUserId", Query.FilterOperator.EQUAL, providerUserId);
//        return datastore.prepare(q).asSingleEntity();
//    }
//
//    private Connection<?> mapUserConnectionEntityToConnection(final Entity userConnectionEntity) {
//        final Long expireTime = (Long) userConnectionEntity.getProperty("expireTime");
//        final ConnectionData connectionData = new ConnectionData(
//                (String) userConnectionEntity.getProperty("providerId"),
//                (String) userConnectionEntity.getProperty("providerUserId"),
//                (String) userConnectionEntity.getProperty("displayName"),
//                (String) userConnectionEntity.getProperty("profileUrl"),
//                (String) userConnectionEntity.getProperty("imageUrl"),
//                decrypt((String) userConnectionEntity.getProperty("accessToken")),
//                decrypt((String) userConnectionEntity.getProperty("secret")),
//                decrypt((String) userConnectionEntity.getProperty("refreshToken")),
//                expireTime != null ? expireTime(expireTime) : null);
//
//        final ConnectionFactory<?> connectionFactory =
//                connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
//
//        return connectionFactory.createConnection(connectionData);
//    }
//
//    private Connection<?> findPrimaryConnection(final String providerId) {
//        final Query q = new Query("UserConnection");
//        q.addFilter("userId", Query.FilterOperator.EQUAL, userId);
//        q.addFilter("providerId", Query.FilterOperator.EQUAL, providerId);
//        q.addFilter("rank", Query.FilterOperator.EQUAL, 1);
//        final PreparedQuery pq = datastore.prepare(q);
//        final List<Entity> entities = pq.asList(FetchOptions.Builder.withLimit(1));
//        if (entities.size() > 0) {
//            return mapUserConnectionEntityToConnection(entities.get(0));
//        } else {
//            return null;
//        }
//    }
//
//    private String decrypt(final String encryptedText) {
//        return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
//    }
//
//    private static Long expireTime(final long expireTime) {
//        return expireTime == 0 ? null : expireTime;
//    }
//
//    private <A> String getProviderId(final Class<A> apiType) {
//        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
//    }
//
//    private String encrypt(final String text) {
//        return text != null ? textEncryptor.encrypt(text) : text;
//    }
//}
