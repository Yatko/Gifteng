package com.venefica.dao;

import com.venefica.model.Ad;
import com.venefica.common.GeoUtils;
import com.venefica.model.AdType;
import com.venefica.model.BusinessUserData;
import com.venefica.model.MemberUserData;
import com.venefica.service.dto.FilterDto;
import com.vividsolutions.jts.geom.Point;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.spatial.GeometryType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link AdDao} interface.
 * 
 * @author Sviatoslav Grebenchukov
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class AdDaoImpl extends DaoBase<Ad> implements AdDao {

    private static final int MAX_ADS_TO_RETURN = 100;
    //private static final double METERS_IN_ONE_DEGREE = 111319.9;
    
    private static final Log log = LogFactory.getLog(AdDaoImpl.class);

    @Override
    public Long save(Ad ad) {
        ad.setCreatedAt(new Date());
        return saveEntity(ad);
    }
    
    @Override
    public void update(Ad ad) {
        updateEntity(ad);
    }
    
    @Override
    public Ad get(Long adId) {
        if (adId == null) {
            throw new NullPointerException("adId");
        }

        return getEntity(adId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ad> get(Long lastAdId, int numberAds) {
        List<Ad> ads;
        
        boolean orderAsc = false;
        numberAds = numberAds > MAX_ADS_TO_RETURN ? MAX_ADS_TO_RETURN : numberAds;

        if (lastAdId < 0) {
            ads = createQuery(""
                    + "from " + getDomainClassName() + " a where "
                    + "a.deleted = false"
                    + " and a.creator.deleted = false"
                    + " and a.approved = true"
                    + " and a.online = true"
                    + " and a.adData.category.hidden = false"
                    + " order by a.approvedAt " + (orderAsc ? "asc" : "desc") + ", a.createdAt desc"
                    + "")
                    .setMaxResults(numberAds)
                    .list();
        } else {
            Ad lastAd = get(lastAdId);
            Date lastApprovedAt = lastAd.getApprovedAt();
            
            ads = createQuery(""
                    + "from " + getDomainClassName() + " a where "
                    + "a.deleted = false"
                    + " and a.creator.deleted = false"
                    + " and a.approved = true"
                    + " and a.online = true"
                    + " and a.adData.category.hidden = false"
                    //+ " and a.id " + (orderAsc ? ">" : "<") + " :lastId"
                    + " and a.approvedAt " + (orderAsc ? ">" : "<") + " :lastApprovedAt"
                    + " order by a.approvedAt " + (orderAsc ? "asc" : "desc") + ", a.createdAt desc"
                    + "")
                    //.setParameter("lastId", lastAdId)
                    .setParameter("lastApprovedAt", lastApprovedAt)
                    .setMaxResults(numberAds)
                    .list();
        }
        return ads;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Ad> get(Long lastAdId, int numberAds, FilterDto filter) {
        if ( filter == null ) {
            return get(lastAdId, numberAds);
        }
        
        //NOTE:
        //as the ordering is based on approvedAt field the lastAdId cannot be used
        //directly, instead it should be queried it's approvedAt value and used it
        //in the where clause
        
        Ad lastAd = null;
        if ( lastAdId >= 0 ) {
            lastAd = get(lastAdId);
        }
        
        String searchString = filter.getSearchString();
        List<Long> categories = filter.getCategories();
        Long distance = filter.getDistance();
        Double latitude = filter.getLatitude();
        Double longitude = filter.getLongitude();
        BigDecimal minPrice = filter.getMinPrice();
        BigDecimal maxPrice = filter.getMaxPrice();
        Boolean hasPhoto = filter.getHasPhoto();
        AdType type = filter.getType();
        boolean orderAsc = filter.getOrderAsc() == null ? false : filter.getOrderAsc();
        boolean hasSearchString = (searchString != null && !searchString.trim().isEmpty());
        
        // Build query string
        String queryStr = ""
                + "select distinct a "
                + "from " + getDomainClassName() + " a where "
                + "a.deleted = false"
                + " and a.creator.deleted = false"
                + " and a.approved = true"
                + " and a.online = true"
                //+ " and a.expired = false"
                //+ " and a.sold = false"
                + "";

        if (lastAdId >= 0 && lastAd != null) {
            //queryStr += " and a.id " + (orderAsc ? ">" : "<") + " :lastId";
            queryStr += " and a.approvedAt " + (orderAsc ? ">" : "<") + " :lastApprovedAt";
        }

        if (hasSearchString) {
            queryStr += " and "
                    + "("
                    + "lower(a.adData.title) like '%' || :searchstr || '%' or "
                    + "lower(a.adData.subtitle) like '%' || :searchstr || '%' or "
                    + "lower(a.adData.description) like '%' || :searchstr || '%' or "
                    + "lower(a.adData.category.name) like '%' || :searchstr || '%' or "
                    + "a.creator.userData in (select ud from " + MemberUserData.class.getSimpleName() + " ud where lower(ud.firstName) like '%' || :searchstr || '%' or lower(ud.lastName) like '%' || :searchstr || '%') or "
                    + "a.creator.userData in (select ud from " + BusinessUserData.class.getSimpleName() + " ud where lower(ud.businessName) like '%' || :searchstr || '%' or lower(ud.contactName) like '%' || :searchstr || '%')"
                    + ")";
        }

        if (categories != null && !categories.isEmpty()) {
            queryStr += " and a.adData.category.id in (:categories)";
        }

        if ( dbType == DBType.MYSQL ) {
            queryStr += createSqlPartForMysql(filter);
        } else if ( dbType == DBType.POSTGRESQL ) {
            queryStr += createSqlPartForPostgresql(filter);
        }

        if (isPositiveOrZero(minPrice)) {
            queryStr += " and a.adData.price >= :minPrice";
        }

        if (isPositiveOrZero(maxPrice)) {
            queryStr += " and a.adData.price <= :maxPrice";
        }

        if (hasPhoto != null && hasPhoto) {
            queryStr += " and a.adData.mainImage is not null";
        }

        if (type != null) {
            queryStr += " and a.adData.type = :type";
        }

        //queryStr += " order by a.id " + (orderAsc ? "asc" : "desc") + "";
        queryStr += " order by a.approvedAt " + (orderAsc ? "asc" : "desc") + ", a.createdAt desc";
        
        Query query = createQuery(queryStr);

        // Bind parameters
        if (lastAdId >= 0 && lastAd != null) {
            //query.setParameter("lastId", lastAdId);
            Date lastApprovedAt = lastAd.getApprovedAt();
            query.setParameter("lastApprovedAt", lastApprovedAt);
        }

        if (hasSearchString) {
            query.setParameter("searchstr", searchString != null ? searchString.toLowerCase() : "");
        }

        if (categories != null && !categories.isEmpty()) {
            query.setParameterList("categories", categories);
        }

        if (distance != null && distance > 0 && longitude != null && latitude != null) {
            Point curPositon = GeoUtils.createPoint(longitude, latitude);
            //Double distanceInDecimalDegrees = distance.doubleValue() / METERS_IN_ONE_DEGREE;
            Double distanceInDecimalDegrees = distance.doubleValue();
            query.setParameter("curpos", curPositon, GeometryType.INSTANCE);
            query.setParameter("maxdist", distanceInDecimalDegrees);
        }

        if (isPositiveOrZero(minPrice)) {
            query.setParameter("minPrice", minPrice);
        }

        if (isPositiveOrZero(maxPrice)) {
            query.setParameter("maxPrice", maxPrice);
        }

        if (type != null) {
            query.setParameter("type", type);
        }

        numberAds = numberAds > MAX_ADS_TO_RETURN ? MAX_ADS_TO_RETURN : numberAds;

        if (numberAds > 0) {
            query.setMaxResults(numberAds);
        }

        return query.list();
    }
    
    @Override
    public List<Ad> getExpiredAds() {
        List<Ad> ads = createQuery(""
                + "from " + getDomainClassName() + " a where "
                + "a.expires = true and "
                + "a.expiresAt < current_date() and "
                + "a.expired = false and "
                + "a.deleted = false and "
                + "a.creator.deleted = false and "
                + "a.sold = false and "
                + "a.adData.quantity > 0"
                + "").list();
        return ads;
    }
    
    @Override
    public void markOnlineAds() {
        // @formatter:off		
        int numRows = createQuery(""
                + "update " + getDomainClassName() + " a "
                + "set "
                + "a.online = true, "
                + "a.onlinedAt = :onlinedAt "
                + "where "
                + "a.deleted = false and "
                + "a.creator.deleted = false and "
                + "a.expired = false and "
                + "a.online = false and "
                + "a.approved = true"
                + "")
                .setParameter("onlinedAt", new Date())
                .executeUpdate();
        // @formatter:on

        if (numRows > 0) {
            log.info(numRows + " ads marked as online.");
        }
    }
    
    @Override
    public void approveAd(Ad ad) {
        ad.setApproved(true);
        ad.setApprovedAt(new Date());
        updateEntity(ad);
    }
    
    @Override
    public void onlineAd(Ad ad) {
        ad.setOnline(true);
        ad.setOnlinedAt(new Date());
        updateEntity(ad);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ad> getByUser(Long userId, int numberAds) {
        Query query = createQuery(""
                + "from " + getDomainClassName() + " a where "
                + "a.creator.id = :userId and "
                + "a.adData.category.hidden = false and "
                + "a.deleted = false and "
                + "a.creator.deleted = false "
                + "order by a.id desc"
                + "")
                .setParameter("userId", userId);
        
        if ( numberAds < Integer.MAX_VALUE ) {
            query.setMaxResults(numberAds);
        }
        
        return query.list();
    }
    
    @Override
    public List<Ad> getUnapprovedAds() {
        return createQuery(""
                + "from " + getDomainClassName() + " a where "
                + "a.deleted = false and "
                + "a.creator.deleted = false and "
                + "a.approved = false "
                + "order by a.id desc"
                + "")
                .list();
    }
    
    @Override
    public List<Ad> getOfflineAds() {
        return createQuery(""
                + "from " + getDomainClassName() + " a "
                + "where "
                + "a.deleted = false and "
                + "a.creator.deleted = false and "
                + "a.expired = false and "
                + "a.online = false and "
                + "a.approved = true "
                + "order by a.id desc"
                + "")
                .list();
    }

    /**
     * This is a PostgreSQL dependent implementation.
     * The dwithin() (postgis) function is a special one, inexistent in case of
     * MySQL for instance.
     * 
     * @param filter
     * @return 
     */
    private String createSqlPartForPostgresql(FilterDto filter) {
        String queryStr = "";
        if ( isDataValidForDWithin(filter) ) {
            queryStr += " and dwithin(a.adData.location, :curpos, :maxdist) = true";
            queryStr += " and st_x(a.adData.location) != 0 and st_y(a.adData.location) != 0";
        }
        return queryStr;
    }
    
    /**
     * This is a Mysql implementation.
     * 
     * Please refer to:
     * http://stackoverflow.com/questions/5324908/correct-way-of-finding-distance-between-two-coordinates-using-spatial-function-i
     * http://www.scribd.com/doc/2569355/Geo-Distance-Search-with-MySQL
     * http://www.tech-problems.com/calculating-distance-in-mysql-using-spatial-point-type/
     * 
     * @param filter
     * @return 
     */
    private String createSqlPartForMysql(FilterDto filter) {
        String queryStr = "";
        if ( isDataValidForDWithin(filter) ) {
            //NOTE:
            //- maybe the best accurate soltion, but needs to be tested
            //- reference: http://derickrethans.nl/spatial-indexes-mysql.html
            
            //queryStr += " and (2 * ASIN(SQRT((SIN((radians(y(a.adData.location)) - radians(y(:curpos))) / 2) * SIN((radians(y(a.adData.location)) - radians(y(:curpos))) / 2)) + COS(radians(y(a.adData.location))) * COS(radians(y(:curpos))) * SIN((radians(x(a.adData.location)) - radians(x(:curpos))) / 2) * SIN((radians(x(a.adData.location)) - radians(x(:curpos))) / 2))) * 6371.01) <= :maxdist";
            //queryStr += " and x(a.adData.location) != 0 and y(a.adData.location) != 0";
            
            queryStr += " and glength(linestring(a.adData.location, GeomFromText(astext(:curpos)))) <= :maxdist";
            queryStr += " and x(a.adData.location) != 0 and y(a.adData.location) != 0";
        }
        return queryStr;
    }
    
    private boolean isDataValidForDWithin(FilterDto filter) {
        if (
                filter.getDistance() != null &&
                filter.getDistance() > 0 &&
                filter.getLatitude() != null &&
                filter.getLongitude() != null
        ) {
            return true;
        }
        return false;
    }
    
    private boolean isPositiveOrZero(BigDecimal number) {
        if (number != null && number.compareTo(BigDecimal.ZERO) >= 0) {
            return true;
        }
        return false;
    }
}
