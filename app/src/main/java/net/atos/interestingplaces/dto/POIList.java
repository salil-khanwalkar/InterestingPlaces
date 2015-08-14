package net.atos.interestingplaces.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import net.atos.interestingplaces.pojo.PlaceOfInterest;

/**
 * Created by a551481 on 03-08-2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class POIList {
    @JsonProperty("list")
    List<PlaceOfInterest> mList;

    public List<PlaceOfInterest> getList() {
        return mList;
    }

    public void setList(final List<PlaceOfInterest> list) {
        mList = list;
    }
}
