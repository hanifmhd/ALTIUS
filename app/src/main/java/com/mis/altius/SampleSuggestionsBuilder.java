package com.mis.altius;

import android.content.Context;
import android.text.InputType;

import org.cryse.widget.persistentsearch.SearchItem;
import org.cryse.widget.persistentsearch.SearchSuggestionsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Hanifmhd on 9/12/2017.
 */

public class SampleSuggestionsBuilder implements SearchSuggestionsBuilder {
    private Context mContext;
    private List<SearchItem> mHistorySuggestions = new ArrayList<SearchItem>();
    ;

    public SampleSuggestionsBuilder(Context context) {
        this.mContext = context;
        //createHistorys();
    }

    private void createHistorys() {
        SearchItem item1 = new SearchItem(
                "Isaac Newton",
                "Isaac Newton",
                SearchItem.TYPE_SEARCH_ITEM_HISTORY
        );
        mHistorySuggestions.add(item1);
        SearchItem item2 = new SearchItem(
                "Albert Einstein",
                "Albert Einstein",
                SearchItem.TYPE_SEARCH_ITEM_HISTORY
        );
        mHistorySuggestions.add(item2);
        SearchItem item3 = new SearchItem(
                "John von Neumann",
                "John von Neumann",
                SearchItem.TYPE_SEARCH_ITEM_HISTORY
        );
        mHistorySuggestions.add(item3);
        SearchItem item4 = new SearchItem(
                "Alan Mathison Turing",
                "Alan Mathison Turing",
                SearchItem.TYPE_SEARCH_ITEM_HISTORY
        );
        mHistorySuggestions.add(item4);
    }

    @Override
    public Collection<SearchItem> buildEmptySearchSuggestion(int maxCount) {
        List<SearchItem> items = new ArrayList<SearchItem>();
        items.addAll(mHistorySuggestions);
        return items;
    }

    @Override
    public Collection<SearchItem> buildSearchSuggestion(int maxCount, String query) {
        List<SearchItem> items = new ArrayList<SearchItem>();
        SearchItem nameSuggestion = new SearchItem(
                "Search name: " + query,
                query,
                SearchItem.TYPE_SEARCH_ITEM_SUGGESTION);
        items.add(nameSuggestion);
//        else {
//            SearchItem citySuggestion = new SearchItem("Cari kota: " + query,
//                    query,
//                    SearchItem.TYPE_SEARCH_ITEM_SUGGESTION);
//            items.add(citySuggestion);
//        }
        for(SearchItem item : mHistorySuggestions) {
            if(item.getValue().startsWith(query)) {
                items.add(item);
            }
        }
        return items;
    }

//    public static boolean isNumeric(String str)
//    {
//        return str.matches("-?\\d+(.\\d+)?");
//    }
}