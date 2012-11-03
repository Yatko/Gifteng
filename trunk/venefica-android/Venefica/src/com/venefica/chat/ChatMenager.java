package com.venefica.chat;

public class ChatMenager
{
	//- - - - - SINGLETON - - - - -//
	/** singleton instance */
	/*private static ChatMenager instance;

	private ChatMenager()
	{
	}

	public static ChatMenager getInstance()
	{
		if (instance == null)
			instance = new ChatMenager();

		return instance;
	}

	//- - - - - SINGLETON END - - - - -//

	public abstract class IListUpdater
	{
		protected List<Message> MessageList;
		protected Runnable Callback;
		protected boolean WaitUpdate = false;
		protected boolean ExistLastMessage = false;

		public IListUpdater(List<Message> MessageList)
		{
			this.MessageList = MessageList;

			if (MessageList == null)
				throw new RuntimeException("MessageList == null");
		}

		public void SetCallback(Runnable Callback)
		{
			this.Callback = Callback;
		}

		public abstract void Update();

		protected void UpdateListView()
		{
			if (Callback == null)
				throw new RuntimeException("Callback == null");
			else
				Callback.run();
		}

		public void ClearList()
		{
			MessageList.clear();
			//WaitUpdate = false;
			ExistLastMessage = false;
		}
	}

	public class ListUpdaterByAdId extends IListUpdater implements ICallback
	{
		long adId;
		GetMessagesByAdContext Context;

		public ListUpdaterByAdId(List<Message> MessageList, long adId)
		{
			super(MessageList);

			this.adId = adId;
		}

		@Override
		public void Update()
		{
			if (WaitUpdate || ExistLastMessage)
				return;

			WaitUpdate = true;

			long lastId = -1;
			if (MessageList.size() > 0)
			{
				lastId = MessageList.get(MessageList.size() - 1).id;
			}

			Context = new GetMessagesByAdContext(adId, lastId, Constants.MESSAGE_LIST_CACHE_SIZE, this);
			VeneficaApplication.asyncServices.GetMessagesByAd(Context);
		}

		public CallbackReturn Callback(IResult<?> result)
		{
			if (result instanceof GetMessagesByAdResult)
			{
				GetMessagesByAdResult ret = (GetMessagesByAdResult)result;
				switch (ret.SoapResult)
				{
					case Ok:
					{
						MessageList.addAll(ret.Return);
						UpdateListView();
						if (ret.Return.size() < Constants.MESSAGE_LIST_CACHE_SIZE)
							ExistLastMessage = true;
						break;
					}

					case Fault:
					{
						Log.d("ListUpdaterByAdId.Callback Warning", "SoapProblem");
						break;
					}

					case SoapProblem:
					{
						Log.d("ListUpdaterByAdId.Callback Warning", "SoapProblem");
						break;
					}
				}
			}

			WaitUpdate = false;
			return CallbackReturn.Ok;
		}
	}
	
	public class ListUpdaterByConversationId extends IListUpdater implements ICallback
	{
		long �onversationId;
		GetMessagesByConversationContext Context;

		public ListUpdaterByConversationId(List<Message> MessageList, long �onversationId)
		{
			super(MessageList);

			this.�onversationId = �onversationId;
		}

		@Override
		public void Update()
		{
			if (WaitUpdate)
				return;
			
			WaitUpdate = true;

			Context = new GetMessagesByConversationContext(�onversationId, this);
			VeneficaApplication.asyncServices.GetMessagesByConversation(Context);
		}

		public CallbackReturn Callback(IResult<?> result)
		{
			if (result instanceof GetMessagesByConversationResult)
			{
				GetMessagesByConversationResult ret = (GetMessagesByConversationResult)result;
				switch (ret.SoapResult)
				{
					case Ok:
					{
						MessageList.addAll(ret.Return);
						UpdateListView();
						break;
					}

					case Fault:
					{
						Log.d("ListUpdaterByAdId.Callback Warning", "SoapProblem");
						break;
					}

					case SoapProblem:
					{
						Log.d("ListUpdaterByAdId.Callback Warning", "SoapProblem");
						break;
					}
				}
			}

			WaitUpdate = false;
			return CallbackReturn.Ok;
		}
	}

	public MessageListAdapter GetAdapterByAdId(Context context, long AdId)
	{
		MessageListAdapter result = null;

		List<Message> list = new ArrayList<Message>();
		result = new MessageListAdapter(context, R.layout.message_row, list);
		result.SetUpdater(new ListUpdaterByAdId(list, AdId));

		return result;
	}
	
	public MessageListAdapter GetAdapterByConversationId(Context context, long conversationId)
	{
		MessageListAdapter result = null;

		List<Message> list = new ArrayList<Message>();
		result = new MessageListAdapter(context, R.layout.message_row, list);
		result.SetUpdater(new ListUpdaterByConversationId(list, conversationId));

		return result;
	}*/
}
