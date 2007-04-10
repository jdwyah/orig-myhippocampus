/*
 * This source code is under public domain.
 * You may use this source code as you wish, even distribute it
 * with different licenssing terms.
 * 
 * Contribution of bug fixes and new features would be appreciated.
 * 
 * Oliver Albers <oliveralbers@gmail.com>
 */ 

package com.aavu.client.widget.autocompletion;

public interface CompletionItems {
    /**
     * Returns an array of all completion items matching
     * @param match The user-entered text all compleition items have to match
     * @return      Array of strings
     */
    public void getCompletionItems(String match, MatchesRequiring widget);
}
