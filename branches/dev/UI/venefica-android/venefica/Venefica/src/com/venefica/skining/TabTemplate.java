package com.venefica.skining;

import android.view.View;

public interface TabTemplate
{
	public abstract void SetUnReadMessage(int num);

	public abstract void SetOnClickToBrowse(View.OnClickListener listener);

	public abstract void SetOnClickToPost(View.OnClickListener listener);

	public abstract void SetOnClickToMessage(View.OnClickListener listener);

	public abstract void SetOnClickToAccaunt(View.OnClickListener listener);
}
