package com.venefica.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.venefica.services.FilterDto;
import com.venefica.services.AsyncServices.CallbackReturn;
import com.venefica.services.AsyncServices.GetAdByIdContext;
import com.venefica.services.AsyncServices.GetAdsContext;
import com.venefica.services.AsyncServices.ICallback;
import com.venefica.services.ServicesManager.GetAdsResult;
import com.venefica.services.ServicesManager.IResult;
import com.venefica.services.ServicesManager.SoapRequestResult;
import com.venefica.utils.Constants;
import com.venefica.utils.VeneficaApplication;

public class MarketEx
{
	//- - - - - SINGLETON - - - - -//
	/** Singleton instance */
	private static MarketEx instance;

	private MarketEx()
	{
	}

	public static MarketEx getInstance()
	{
		if (instance == null)
			instance = new MarketEx();

		return instance;
	}

	//- - - - - SINGLETON END - - - - -//

	//- - - - - VARIABLES - - - - -//
	/** Thread-safe Product List */
	List<Product> ProductsListSync = Collections.synchronizedList(new ArrayList<Product>());
	/** Thread-safe Product cache */
	Map<Long, Product> ProductsCache = Collections.synchronizedMap(new HashMap<Long, Product>());
	boolean WaitNewProduct = false;
	/**
	 * true - is listed in the latest product update list more Not required
	 */
	boolean ExistLastProduct = false;
	private FilterDto filter = new FilterDto();

	//- - - - - VARIABLES END - - - - -//

	/** Callback for a new list of products */
	public static interface UpdateProductsCallback
	{
		/**
		 * @param ExistLastProduct
		 *            - whether the list of latest product
		 * @param Products
		 *            - a new list of products
		 */
		public abstract void Callback(SoapRequestResult SoapResult, boolean ExistLastProduct, List<Product> Products);
	}

	public void UpdateCategory()
	{
		Category.DeleteAllCategories();
		VeneficaApplication.services.GetCategories(VeneficaApplication.authToken);
	}

	public void ClearCategory()
	{
		Category.DeleteAllCategories();
	}

	public void UpdateProductsList(final UpdateProductsCallback callback)
	{
		if (WaitNewProduct == false)
		{
			WaitNewProduct = true;

			long lastAdId = -1;
			if (ProductsListSync.isEmpty() == false)
			{
				lastAdId = ProductsListSync.get(ProductsListSync.size() - 1).Id;
			}

			VeneficaApplication.asyncServices.GetAds(new GetAdsContext(lastAdId, Constants.PRODUCT_LIST_CACHE_SIZE, filter, new ICallback()
			{
				int filterRev = filter.rev;

				public CallbackReturn Callback(IResult<?> result)
				{
					GetAdsResult ret = (GetAdsResult)result;

					switch (ret.SoapResult)
					{
						case Ok:
							GetAdsOk(ret, filterRev);
							callback.Callback(ret.SoapResult, ExistLastProduct, ProductsListSync);
							break;
						case Fault:
							callback.Callback(ret.SoapResult, false, null);
							Log.d("GetAdsCallback Warning!", "SoapResult == " + ret.SoapResult.toString());
							break;
						case SoapProblem:
							Log.d("GetAdsCallback Warning!", "SoapResult == " + ret.SoapResult.toString());
							break;
					}

					WaitNewProduct = false;
					return CallbackReturn.Ok;
				}
			}));
		}
	}

	public void setFilter(FilterDto filter)
	{
		this.filter = filter;
		ClearProductsList();
	}

	public FilterDto getFilter()
	{
		return filter;
	}

	public void setFilterSearchString(String search)
	{
		getFilter().searchString = search;
		setFilter(getFilter());
	}

	/**
	 * @return <b>true</b> - product was found in the cache<br>
	 *         <b>false</b> - product is not found in the cache, will be made
	 *         Asynchronous request
	 */
	public void GetProductById(final long adId, final ICallback callback)
	{
		VeneficaApplication.asyncServices.GetAdById(new GetAdByIdContext(adId, callback));
	}

	public List<Product> GetProducts()
	{
		return ProductsListSync;
	}

	public int GetNumProduct()
	{
		return ProductsListSync.size();
	}

	public boolean ExistLastProduct()
	{
		return ExistLastProduct;
	}

	/** Cleans the list of products and a list of products by */
	public void ClearProductsList()
	{
		ProductsListSync.clear();
		ProductsCache.clear();
		WaitNewProduct = false;
		ExistLastProduct = false;
	}

	private void GetAdsOk(GetAdsResult ret, int filterRev)
	{
		if (filterRev == getFilter().rev)
		{
			if (ret.Return == null)
			{
				Log.e("GetAdsCallback Alert!", "ret.Return == null, WTF?");
				return;
			}
			else if (ret.SoapResult == SoapRequestResult.Ok && ret.Return.size() < Constants.PRODUCT_LIST_CACHE_SIZE)
			{//received less than they wanted, then there is the latest product
				ExistLastProduct = true;
			}
			else
			{
				ExistLastProduct = false;
			}

			if (ret.Return.size() == 0)
			{
				Log.d("GetAdsCallback Alert!", "ret.Return.size() == 0");
			}
			else if (ProductsListSync.addAll(ret.Return) == false)
				Log.d("GetAdsCallback Alert!", "ProductsListSync.addAll fail");
			else
			{
				for (Product ad : ret.Return)
				{
					ProductsCache.put(Long.valueOf(ad.Id), ad);
				}
			}
		}
		else
		{
			Log.d("GetAdsCallback Alert!", "filterRev != getFilter().rev");
		}
	}
}
