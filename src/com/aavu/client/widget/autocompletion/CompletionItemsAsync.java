package com.aavu.client.widget.autocompletion;


public interface CompletionItemsAsync {
	/**
	 * Makes a call to find all completion items matching.
	 * @param match The user-entered text all completion items have to match
	 * @param asyncReturn  The object invoked when the async call returns correctly
	 */
	public void getCompletionItems(String match,
			MatchesRequiring asyncReturn);

} 

