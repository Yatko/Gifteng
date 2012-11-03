package com.venefica.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.venefica.activity.R;
import com.venefica.market.Category;
import com.venefica.services.FilterDto;
import com.venefica.utils.VeneficaApplication;
import com.venefica.utils.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class FilterPanel extends RelativeLayout
{
	private final Button btnSale;
	private final Button btnWanted;
	private boolean wanted = false;
	private final EditText editMinPrice;
	private final EditText editMaxPrice;
	private final Button btnCategories;
	private final ToggleButton toggleCategories;
	private final ToggleButton toggleHavePhoto;
	private final SeekBar seekBarDistance;
	private final Button btnApplyFilter;
	private final Button btnDefaultFilter;
	private final Button btnCancel;
	private final Runnable closeCallback;
	private final ViewGroup parent;
	private boolean showing;
	private CharSequence[] selectedCategoryName;
	private List<Long> selectedCategoryId;
	private boolean[] selectionsCategory;
	private boolean change = false;
	private final DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener = new DialogInterface.OnMultiChoiceClickListener()
	{
		public void onClick(DialogInterface paramDialogInterface, int paramInt, boolean paramBoolean)
		{

		}
	};
	private final DialogInterface.OnClickListener positiveButtonClickListner = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface paramDialogInterface, int paramInt)
		{

		}
	};

	public FilterPanel(final Context context, final AttributeSet attrs, final ViewGroup parent, final Runnable closeCallback)
	{
		super(context, attrs);

		this.parent = parent;
		this.closeCallback = closeCallback;
		showing = false;

		LayoutInflater.from(context).inflate(R.layout.filter_layout, this, true);
		btnSale = (Button)findViewById(R.id.btnSale);
		btnWanted = (Button)findViewById(R.id.btnWanted);
		editMinPrice = (EditText)findViewById(R.id.editMinPrice);
		editMaxPrice = (EditText)findViewById(R.id.editMaxPrice);
		btnCategories = (Button)findViewById(R.id.btnCategories);
		toggleCategories = (ToggleButton)findViewById(R.id.toggleCategories);
		toggleHavePhoto = (ToggleButton)findViewById(R.id.toggleHavePhoto);
		seekBarDistance = (SeekBar)findViewById(R.id.seekBarDistance);
		btnApplyFilter = (Button)findViewById(R.id.btnApplyFilter);
		btnDefaultFilter = (Button)findViewById(R.id.btnDefaultFilter);
		btnCancel = (Button)findViewById(R.id.btnCancel);

		btnSale.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				setSaleWantedButton(false);
			}
		});

		btnWanted.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				setSaleWantedButton(true);
			}
		});

		btnCategories.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				AlertDialog dialog = new AlertDialog.Builder(getContext()).setMultiChoiceItems(selectedCategoryName, selectionsCategory, multiChoiceClickListener).setPositiveButton("Ok", positiveButtonClickListner).create();
				dialog.show();
			}
		});

		btnApplyFilter.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				change = true;
				hide();
			}
		});

		btnDefaultFilter.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				setDefaultFilter();
				change = true;
				hide();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				hide();
			}
		});

		createCategoryItem();

		seekBarDistance.setMax(100*1609);

		setDefaultFilter();
	}

	private void createCategoryItem()
	{
		List<Category> allCat = Category.GetAllCategory();
		selectedCategoryId = new ArrayList<Long>();
		List<CharSequence> catNameList = new ArrayList<CharSequence>();

		for (Category it : allCat)
		{
			if (it.getSubCategoryNumber() == 0)
			{
				catNameList.add(it.Desc);
				selectedCategoryId.add(it.Id);
			}
		}

		selectionsCategory = new boolean[catNameList.size()];
		selectedCategoryName = new CharSequence[catNameList.size()];
		for (int i = 0; i < selectionsCategory.length; i++)
		{
			selectionsCategory[i] = true;
			selectedCategoryName[i] = catNameList.get(i);
		}
	}

	public void show()
	{
		if (!showing)
		{
			parent.addView(this);
			showing = true;
			change = false;
		}
	}

	public void hide()
	{
		if (showing)
		{
			parent.removeView(this);

			if (closeCallback != null)
				closeCallback.run();

			showing = false;
		}
	}

	/** ��������� ���������� ������� � ���������� UI */
	private void setDefaultFilter()
	{
		setSaleWantedButton(true);

		editMinPrice.setText("");
		editMaxPrice.setText("");

		for (int i = 0; i < selectionsCategory.length; i++)
		{
			selectionsCategory[i] = true;
		}
		toggleCategories.setChecked(false);
		toggleHavePhoto.setChecked(false);
		seekBarDistance.setProgress(50);
	}

	private void setSaleWantedButton(boolean setWanted)
	{
		if (setWanted)
		{
			btnSale.setBackgroundResource(R.drawable.forsale_off);
			btnWanted.setBackgroundResource(R.drawable.wanted_on);
			wanted = setWanted;
		}
		else
		{
			btnSale.setBackgroundResource(R.drawable.forsale_on);
			btnWanted.setBackgroundResource(R.drawable.wanted_off);
			wanted = setWanted;
		}
	}

	public FilterDto getFilter()
	{
		final FilterDto filter = new FilterDto();

		filter.wanted = wanted;

		if (toggleCategories.isChecked())
		{
			filter.categories = new ArrayList<Long>();
			for (int i = 0; i < selectionsCategory.length; i++)
			{
				if (selectionsCategory[i])
					filter.categories.add(selectedCategoryId.get(i));
			}
		}

		filter.distance = seekBarDistance.getProgress();
		filter.latitude = VeneficaApplication.myLocation.getLatitude();
		filter.longitude = VeneficaApplication.myLocation.getLongitude();

		String minPrice = editMinPrice.getText().toString();
		if (minPrice.length() > 0)
			filter.minPrice = BigDecimal.valueOf(Double.parseDouble(minPrice.replace(',', '.')));

		String maxPrice = editMaxPrice.getText().toString();
		if (maxPrice.length() > 0)
			filter.maxPrice = BigDecimal.valueOf(Double.parseDouble(maxPrice.replace(',', '.')));

		filter.hasPhoto = toggleHavePhoto.isChecked();

		return filter;
	}

	public void setFilter(FilterDto filter)
	{
		setSaleWantedButton(filter.wanted);

		if (filter.minPrice != null && filter.minPrice.floatValue() != 0.0)
		{
			editMinPrice.setText(String.format("%.2f", filter.minPrice.floatValue()));
		}
		else
		{
			editMinPrice.setText("");
		}

		if (filter.maxPrice != null && filter.maxPrice.floatValue() != 0.0)
		{
			editMaxPrice.setText(String.format("%.2f", filter.maxPrice.floatValue()));
		}
		else
		{
			editMaxPrice.setText("");
		}

		toggleHavePhoto.setChecked(filter.hasPhoto);
		seekBarDistance.setProgress((int)filter.distance);

		if (filter.categories != null && filter.categories.size() > 0)
		{
			toggleCategories.setChecked(true);

			for (int i = 0; i < selectionsCategory.length; i++)
			{
				selectionsCategory[i] = false;
			}

			for (final long id : filter.categories)
			{
				int index = Utils.SearchInList(selectedCategoryId, new Utils.ISearchFilter<Long>()
				{
					public boolean Filter(Long item)
					{
						return item == id;
					}
				});
				if (index != -1)
					selectionsCategory[index] = true;
			}
		}
		else
		{
			toggleCategories.setChecked(false);
			for (int i = 0; i < selectionsCategory.length; i++)
			{
				selectionsCategory[i] = true;
			}
		}
	}

	public boolean isChange()
	{
		return change;
	}
}
