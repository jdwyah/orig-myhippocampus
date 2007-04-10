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

import java.util.List;

import com.aavu.client.domain.dto.TopicIdentifier;

public interface MatchesRequiring {

        public void onMatch(String text);

        //public void setMatches(TopicIdentifier[] matches);
		//public void setMatches(String[] strings);

		//List<TopicIdentifier>
		public void setMatches(List list);

}
