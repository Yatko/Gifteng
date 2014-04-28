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
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
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
    public Ad getEager(Long adId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("id", adId));
        criteria.setFetchMode("adData", FetchMode.JOIN); //EAGER
        criteria.setFetchMode("creator", FetchMode.JOIN); //EAGER
        return (Ad) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Ad> get(int lastIndex, int numberAds) {
        Query query;
        
        boolean orderAsc = FilterDto.DEFAULT_ORDER_ASC;
        boolean checkDate = FilterDto.DEFAULT_CHECK_DATE;
        
        lastIndex = lastIndex < 0 ? 0 : lastIndex;
        numberAds = numberAds <= 0 || numberAds > MAX_ADS_TO_RETURN ? MAX_ADS_TO_RETURN : numberAds;

        String hql = ""
                + "from " + getDomainClassName() + " a where "
                + "a.deleted = false"
                + " and a.creator.deleted = false"
                + " and a.approved = true"
                + " and a.online = true"
                + (checkDate ? " and a.availableAt <= current_date() and a.expiresAt >= current_date()" : "")
                + " and a.adData.category.hidden = false"
                + " order by a.approvedAt " + (orderAsc ? "asc" : "desc") + ", a.createdAt desc"
                + "";
        query = createQuery(hql);
        
        query.setFirstResult(lastIndex);
        query.setMaxResults(numberAds);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Ad> get(int lastIndex, int numberAds, FilterDto filter) {
        if ( filter == null ) {
            return get(lastIndex, numberAds);
        }
        
        Boolean includePickup = filter.getIncludePickUp();
        Boolean includeShipping = filter.getIncludeShipping();
        String searchString = filter.getSearchString();
        List<Long> categories = filter.getCategories();
        Long distance = filter.getDistance();
        Double latitude = filter.getLatitude();
        Double longitude = filter.getLongitude();
        BigDecimal minPrice = filter.getMinPrice();
        BigDecimal maxPrice = filter.getMaxPrice();
        Boolean hasPhoto = filter.getHasPhoto();
        List<AdType> types = filter.getTypes();
        boolean orderAsc = filter.getOrderAsc() == null ? FilterDto.DEFAULT_ORDER_ASC : filter.getOrderAsc();
        boolean checkDate = filter.getCheckDate() == null ? FilterDto.DEFAULT_CHECK_DATE : filter.getCheckDate();
        boolean orderClosest = filter.getOrderClosest() == null ? FilterDto.DEFAULT_ORDER_CLOSEST : filter.getOrderClosest();
        boolean hasSearchString = (searchString != null && !searchString.trim().isEmpty());
        Point curPositon = (latitude != null && longitude != null) ? GeoUtils.createPoint(latitude, longitude) : null;
        lastIndex = lastIndex < 0 ? 0 : lastIndex;
        numberAds = numberAds <= 0 || numberAds > MAX_ADS_TO_RETURN ? MAX_ADS_TO_RETURN : numberAds;
        
        // Build query string
        String queryStr = ""
                + "select a "
                + "from " + getDomainClassName() + " a where "
                + "a.deleted = false"
                + " and a.creator.deleted = false"
                + " and a.approved = true"
                + " and a.online = true"
                + (checkDate ? " and a.availableAt <= current_date() and a.expiresAt >= current_date()" : "")
                + " and a.adData.category.hidden = false"
                //+ " and a.expired = false"
                //+ " and a.sold = false"
                + "";

        if ( includePickup != null ) {
            if ( includePickup ) {
                queryStr += " and a.adData.pickUp = true";
            } else {
                queryStr += " and (a.adData.pickUp = null or a.adData.pickUp = false)";
            }
        }
        if ( includeShipping != null ) {
            if ( includeShipping ) {
                queryStr += " and a.adData.shippingBox != null";
            } else {
                queryStr += " and a.adData.shippingBox = null";
            }
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

        queryStr += createWithinConditionSQL(filter);

        if (isPositiveOrZero(minPrice)) {
            queryStr += " and a.adData.price >= :minPrice";
        }

        if (isPositiveOrZero(maxPrice)) {
            queryStr += " and a.adData.price <= :maxPrice";
        }

        if (hasPhoto != null && hasPhoto) {
            queryStr += " and a.adData.mainImage is not null";
        }

        if (types != null && !types.isEmpty()) {
            queryStr += " and (";
            for ( int i = 0; i < types.size(); i++ ) {
                if ( i > 0 ) queryStr += " or";
                queryStr += " a.adData.type = :type" + i;
            }
            queryStr += ")";
        }

        if ( orderClosest && curPositon != null ) {
            queryStr += " order by " + GeoUtils.getHarvesineSQL(dbType) + " " + (orderAsc ? "asc" : "desc");
        } else {
            queryStr += " order by a.approvedAt " + (orderAsc ? "asc" : "desc") + ", a.createdAt desc";
        }
        
        Query query = createQuery(queryStr);

        // Bind parameters
        
        if (hasSearchString) {
            query.setParameter("searchstr", searchString != null ? searchString.toLowerCase() : "");
        }

        if (categories != null && !categories.isEmpty()) {
            query.setParameterList("categories", categories);
        }

        if (distance != null && distance > 0 && curPositon != null) {
            Double distanceInDecimalDegrees = distance.doubleValue(); //distance.doubleValue() / GeoUtils.METERS_IN_ONE_DEGREE;
            query.setParameter("maxDist", distanceInDecimalDegrees);
        }

        if ( curPositon != null ) {
            query.setParameter("curPos", curPositon, GeometryType.INSTANCE);
        }
        
        if (isPositiveOrZero(minPrice)) {
            query.setParameter("minPrice", minPrice);
        }

        if (isPositiveOrZero(maxPrice)) {
            query.setParameter("maxPrice", maxPrice);
        }

        if (types != null && !types.isEmpty()) {
            for ( int i = 0; i < types.size(); i++ ) {
                query.setParameter("type" + i, types.get(i));
            }
        }

        query.setFirstResult(lastIndex);
        query.setMaxResults(numberAds);
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
        ad.markAsApproved();
        updateEntity(ad);
    }
    
    @Override
    public void onlineAd(Ad ad) {
        ad.markAsOnline();
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
     * PostgreSQL/MySQL limitation:
     * - the dwithin() (postgis) function is a special one, inexistent
     * in case of MySQL for instance.
     * 
     * In PostgreSQL a shorter condition would be:
     * dwithin(a.adData.location, :curPos, :maxDist) = true
     * 
     * 
     * References:
     * http://stackoverflow.com/questions/5324908/correct-way-of-finding-distance-between-two-coordinates-using-spatial-function-i
     * http://www.scribd.com/doc/2569355/Geo-Distance-Search-with-MySQL
     * http://www.tech-problems.com/calculating-distance-in-mysql-using-spatial-point-type/
     * http://derickrethans.nl/spatial-indexes-mysql.html
     * 
     * @param filter
     * @return 
     */
    private String createWithinConditionSQL(FilterDto filter) {
        String queryStr = "";
        if ( isDataValidForWithin(filter) ) {
            queryStr += " and " + GeoUtils.getHarvesineSQL(dbType) + " <= :maxDist";
            if ( dbType == DBType.MYSQL ) {
                queryStr += " and x(a.adData.location) != 0 and y(a.adData.location) != 0";
            } else if ( dbType == DBType.POSTGRESQL ) {
                queryStr += " and st_x(a.adData.location) != 0 and st_y(a.adData.location) != 0";
            }
        }
        return queryStr;
    }
    
    private boolean isDataValidForWithin(FilterDto filter) {
        if (
            filter.getDistance() != null && filter.getDistance() > 0 &&
            filter.getLatitude() != null && filter.getLongitude() != null
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
