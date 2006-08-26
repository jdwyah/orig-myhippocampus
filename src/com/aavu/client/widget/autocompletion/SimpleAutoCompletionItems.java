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

import java.util.ArrayList;

public class SimpleAutoCompletionItems implements CompletionItems {
  private Completable[] completions;

  public SimpleAutoCompletionItems(Completable[] items)
  {
    completions = items;
  }

  public void getCompletionItems(String match, MatchesRequiring widget) {
    ArrayList matches = new ArrayList();
    for (int i = 0; i < completions.length; i++) {
      if (completions[i].getCompleteStr().toLowerCase().startsWith(match.toLowerCase())) {
        matches.add(completions[i]);
      }
    }
    Completable[] returnMatches = new Completable[matches.size()];
    for(int i = 0; i < matches.size(); i++)
    {
      returnMatches[i] = (Completable) matches.get(i);
    }
    widget.setMatches(returnMatches);
    widget.onMatch( match ); 
  }
} 
