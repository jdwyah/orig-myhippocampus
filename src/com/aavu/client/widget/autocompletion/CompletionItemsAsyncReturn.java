package com.aavu.client.widget.autocompletion;

public interface CompletionItemsAsyncReturn {
	/**
	 * Handles an array of items. Called by
	 * <code>CompletionItemsAsync.getCompletionItems</code>
	 * @param items The array of compleition items
	 */
	public void itemReturn(String[] items);
}
